package com.artur.vocab.wordlearningapp.validation.exception;

public class WordAlreadyExistsException extends RuntimeException {

    public WordAlreadyExistsException(String text) {
        super("Word already exists: " + text);
    }
}
