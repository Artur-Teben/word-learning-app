package com.artur.vocab.wordlearningapp.service;

import com.artur.vocab.wordlearningapp.domain.entity.WordEnrichmentEntity;
import com.artur.vocab.wordlearningapp.domain.entity.WordEntity;
import com.artur.vocab.wordlearningapp.domain.enums.ProcessingStatus;
import com.artur.vocab.wordlearningapp.web.dto.CreateWordRequest;
import com.artur.vocab.wordlearningapp.mapper.WordMapper;
import com.artur.vocab.wordlearningapp.validation.WordValidator;
import com.artur.vocab.wordlearningapp.repo.WordRepository;
import com.artur.vocab.wordlearningapp.web.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final WordValidator wordValidator;
    private final WordMapper wordMapper;
    private final WordEnricher wordEnricher;

    @Transactional
    public WordEntity addWord(CreateWordRequest request) {
        String normalizedText = StringUtils.normalizeSpace(request.getText());
        request.setText(normalizedText);

        wordValidator.validateUniqueness(normalizedText);

        WordEntity word = wordMapper.toEntity(request);

        try {
            WordEnrichmentEntity enrichment = wordEnricher.enrich(normalizedText, request.getContext());
            enrichment.setWord(word);
            word.setEnrichment(enrichment);
            word.setProcessingStatus(ProcessingStatus.READY);
        } catch (Exception e) {
            word.setProcessingStatus(ProcessingStatus.FAILED);
        }

        return wordRepository.save(word);
    }

    public WordEntity getWord(Long id) {
        return wordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Word not found: " + id));
    }
}
