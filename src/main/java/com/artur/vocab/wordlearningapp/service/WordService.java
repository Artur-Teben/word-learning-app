package com.artur.vocab.wordlearningapp.service;

import com.artur.vocab.wordlearningapp.web.dto.CreateWordRequest;
import com.artur.vocab.wordlearningapp.mapper.WordMapper;
import com.artur.vocab.wordlearningapp.validation.WordValidator;
import com.artur.vocab.wordlearningapp.repo.WordRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WordService {

    private final WordRepository wordRepository;
    private final WordValidator wordValidator;
    private final WordMapper wordMapper;

    public WordService(WordRepository wordRepository, WordValidator wordValidator, WordMapper wordMapper) {
        this.wordRepository = wordRepository;
        this.wordValidator = wordValidator;
        this.wordMapper = wordMapper;
    }

    @Transactional
    public void addWord(CreateWordRequest request) {
        String normalizedText = StringUtils.normalizeSpace(request.getText());
        request.setText(normalizedText);

        wordValidator.validateUniqueness(normalizedText);

        wordRepository.save(wordMapper.toEntity(request));
    }
}
