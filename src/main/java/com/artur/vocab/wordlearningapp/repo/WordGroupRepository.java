package com.artur.vocab.wordlearningapp.repo;

import com.artur.vocab.wordlearningapp.domain.entity.WordGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordGroupRepository extends JpaRepository<WordGroupEntity, Long> {

    Optional<WordGroupEntity> findByGroupKey(String groupKey);
}
