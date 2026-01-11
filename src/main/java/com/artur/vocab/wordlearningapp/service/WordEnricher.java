package com.artur.vocab.wordlearningapp.service;

import com.artur.vocab.wordlearningapp.domain.entity.WordEnrichmentEntity;

public interface WordEnricher {

    WordEnrichmentEntity enrich(String text, String context);
}