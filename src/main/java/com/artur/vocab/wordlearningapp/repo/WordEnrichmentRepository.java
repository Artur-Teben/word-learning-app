package com.artur.vocab.wordlearningapp.repo;

import com.artur.vocab.wordlearningapp.domain.entity.WordEnrichmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordEnrichmentRepository extends JpaRepository<WordEnrichmentEntity, Long> {
}
