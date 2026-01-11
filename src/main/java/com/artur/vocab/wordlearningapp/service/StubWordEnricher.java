package com.artur.vocab.wordlearningapp.service;

import com.artur.vocab.wordlearningapp.domain.entity.WordEnrichmentEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class StubWordEnricher implements WordEnricher {

    @Override
    public WordEnrichmentEntity enrich(String text, String context) {
        WordEnrichmentEntity enrichment = new WordEnrichmentEntity();
        enrichment.setLemma(text.toLowerCase());
        enrichment.setPartOfSpeech("noun");
        enrichment.setMeaningEn("Stub meaning for: " + text);
        enrichment.setTranslationUk("Заглушка для: " + text);
        enrichment.setTranscription("/stʌb/");
        enrichment.setCommonness("COMMON");
        enrichment.setCefrLevel("B1");
        enrichment.setUsageTags("INFORMAL");
        enrichment.setSynonyms("example,sample");
        enrichment.setModel("stub");
        enrichment.setRawAiResponse(null);
        enrichment.setEnrichedAt(Instant.now());
        return enrichment;
    }
}