package com.artur.vocab.wordlearningapp.service;

import com.artur.vocab.wordlearningapp.config.property.OpenAiProperties;
import com.artur.vocab.wordlearningapp.domain.entity.CategoryEntity;
import com.artur.vocab.wordlearningapp.domain.entity.WordEnrichmentEntity;
import com.artur.vocab.wordlearningapp.repo.CategoryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Primary
@Service
@RequiredArgsConstructor
public class OpenAiWordEnricher implements WordEnricher {

    private final RestClient openAiRestClient;
    private final ObjectMapper objectMapper;
    private final OpenAiProperties properties;
    private final CategoryRepository categoryRepository;

    @Override
    public WordEnrichmentEntity enrich(String text, String context) {
        String prompt = buildPrompt(text, context);

        Map<String, Object> request = Map.of(
                "model", properties.model(),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt()),
                        Map.of("role", "user", "content", prompt)
                )
        );


        long start = System.currentTimeMillis();

        String responseBody = openAiRestClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(String.class);

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("OpenAI API call took " + elapsed + "ms for: " + text);

        return parseResponse(responseBody, text);
    }

    private String systemPrompt() {
        return """
            You are a vocabulary assistant for an English learner (native Ukrainian speaker).
            
            Given a word or phrase, return a JSON object with these exact fields:
            {
              "lemma": "base form of the word/phrase",
              "partOfSpeech": "noun/verb/adjective/adverb/phrase/other",
              "meaningEn": "clear, concise explanation in English (1-2 sentences)",
              "translationUk": "Ukrainian translation",
              "transcription": "IPA transcription, e.g. /wɜːrd/",
              "commonness": "VERY_COMMON/COMMON/MODERATE/UNCOMMON/RARE",
              "cefrLevel": "A1/A2/B1/B2/C1/C2",
              "usageTags": ["FORMAL", "INFORMAL", "SLANG", "TECHNICAL", "ARCHAIC", "LITERARY", "VULGAR", "DIALECT", "JARGON"],
              "synonyms": ["synonym1", "synonym2"],
              "categoryCode": "CATEGORY_CODE"
            }
            
            Category must be one of:
            GENERAL, PEOPLE, EMOTIONS, COMMUNICATION, WORK, EDUCATION, HOME, FOOD, HEALTH, SPORT, TRAVEL, NATURE, TIME, MONEY, LAW, TECH, ART, MEDIA, CLOTHING, CITY, ACTIONS, DESCRIPTIONS, OBJECTS, SOCIAL, MENTAL
            
            Rules:
            - Return ONLY valid JSON, no markdown, no explanation
            - meaningEn: practical meaning useful for reading comprehension
            - translationUk: natural Ukrainian, not word-by-word
            - commonness: how often a typical native speaker uses this
            - usageTags: include only applicable tags, empty array if none
            - synonyms: 2-5 common synonyms, empty array if none
            - For phrases: treat as single unit, lemma is the base form of the phrase
            - categoryCode: pick the single best matching category from the list above, use GENERAL if unsure
            """;
    }

    private String buildPrompt(String text, String context) {
        if (context == null || context.isBlank()) {
            return "Word: " + text;
        }
        return "Word: " + text + "\nContext: " + context;
    }

    private WordEnrichmentEntity parseResponse(String responseBody, String originalText) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            JsonNode data = objectMapper.readTree(content);

            String categoryCode = data.path("categoryCode").asText("GENERAL");
            Long categoryId = categoryRepository.findByCode(categoryCode)
                    .or(() -> categoryRepository.findByCode("GENERAL"))
                    .map(CategoryEntity::getId)
                    .orElse(null);

            return WordEnrichmentEntity.builder()
                    .lemma(data.path("lemma").asText(originalText.toLowerCase()))
                    .partOfSpeech(data.path("partOfSpeech").asText(null))
                    .meaningEn(data.path("meaningEn").asText())
                    .translationUk(data.path("translationUk").asText())
                    .transcription(data.path("transcription").asText(null))
                    .commonness(data.path("commonness").asText("COMMON"))
                    .cefrLevel(data.path("cefrLevel").asText(null))
                    .usageTags(toCommaSeparated(data.path("usageTags")))
                    .synonyms(toCommaSeparated(data.path("synonyms")))
                    .model(properties.model())
                    .rawAiResponse(responseBody)
                    .enrichedAt(Instant.now())
                    .categoryId(categoryId)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OpenAI response", e);
        }
    }

    private String toCommaSeparated(JsonNode arrayNode) {
        if (arrayNode == null || !arrayNode.isArray() || arrayNode.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayNode.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(arrayNode.get(i).asText());
        }
        return sb.toString();
    }
}
