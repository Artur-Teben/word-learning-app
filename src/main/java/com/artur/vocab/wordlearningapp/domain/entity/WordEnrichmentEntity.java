package com.artur.vocab.wordlearningapp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "word_enrichment")
public class WordEnrichmentEntity {

    @Id
    @Column(name = "word_id")
    private Long wordId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "word_id")
    private WordEntity word;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "lemma", nullable = false)
    private String lemma;

    @Column(name = "part_of_speech")
    private String partOfSpeech;

    @Column(name = "meaning_en", nullable = false)
    private String meaningEn;

    @Column(name = "translation_uk", nullable = false)
    private String translationUk;

    @Column(name = "transcription")
    private String transcription;

    @Column(name = "commonness", nullable = false)
    private String commonness;

    @Column(name = "cefr_level")
    private String cefrLevel;

    @Column(name = "usage_tags")
    private String usageTags;

    @Column(name = "synonyms")
    private String synonyms;

    @Column(name = "model")
    private String model;

    @Column(name = "raw_ai_response")
    private String rawAiResponse;

    @Column(name = "enriched_at", nullable = false)
    private Instant enrichedAt;
}
