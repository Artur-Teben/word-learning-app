package com.artur.vocab.wordlearningapp.service;

import com.artur.vocab.wordlearningapp.domain.entity.WordEnrichmentEntity;
import com.artur.vocab.wordlearningapp.domain.entity.WordEntity;
import com.artur.vocab.wordlearningapp.domain.enums.ProcessingStatus;
import com.artur.vocab.wordlearningapp.web.dto.CreateWordRequest;
import com.artur.vocab.wordlearningapp.mapper.WordMapper;
import com.artur.vocab.wordlearningapp.repo.WordRepository;
import com.artur.vocab.wordlearningapp.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final WordMapper wordMapper;
    private final WordEnricher wordEnricher;

    @Transactional
    public WordEntity addWord(CreateWordRequest request) {
        String normalizedText = StringUtils.normalizeSpace(request.getText());
        request.setText(normalizedText);

        Optional<WordEntity> existing = wordRepository.findByText(normalizedText);

        if (existing.isPresent()) {
            WordEntity word = existing.get();

            if (word.getProcessingStatus() == ProcessingStatus.FAILED) {
                return enrichAndSave(word);
            }
            return word;
        }

        return enrichAndSave(wordMapper.toEntity(request));
    }

    @Transactional
    public WordEntity getWord(Long id) {
        WordEntity word = wordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Word not found: " + id));

        if (word.getProcessingStatus() == ProcessingStatus.FAILED) {
            return enrichAndSave(word);
        }
        return word;
    }

    private WordEntity enrichAndSave(WordEntity word) {
        try {
            WordEnrichmentEntity enrichment = wordEnricher.enrich(word.getText(), word.getContext());
            enrichment.setWord(word);
            word.setEnrichment(enrichment);
            word.setProcessingStatus(ProcessingStatus.READY);
        } catch (Exception e) {
            word.setProcessingStatus(ProcessingStatus.FAILED);
        }
        return wordRepository.save(word);
    }
}
