package com.artur.vocab.wordlearningapp.domain.entity;

import com.artur.vocab.wordlearningapp.domain.enums.LearningStatus;
import com.artur.vocab.wordlearningapp.domain.enums.ProcessingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;


@Getter
@Setter
@Entity
@Table(name = "word")
public class WordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "context")
    private String context;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", nullable = false)
    private ProcessingStatus processingStatus = ProcessingStatus.NEW;

    @Enumerated(EnumType.STRING)
    @Column(name = "learning_status", nullable = false)
    private LearningStatus learningStatus = LearningStatus.TO_REVIEW;

    @Column(name = "group_id")
    private Long groupId;

    @OneToOne(mappedBy = "word", cascade = CascadeType.ALL)
    private WordEnrichmentEntity enrichment;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
