package com.artur.vocab.wordlearningapp.validation;


import com.artur.vocab.wordlearningapp.repo.WordRepository;
import com.artur.vocab.wordlearningapp.exception.WordAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WordValidator {

    private final WordRepository wordRepository;

    public void validateUniqueness(String normalizedText) {
        if (wordRepository.findByText(normalizedText).isPresent()) {
            throw new WordAlreadyExistsException(normalizedText);
        }
    }
}