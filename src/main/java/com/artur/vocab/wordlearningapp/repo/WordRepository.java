package com.artur.vocab.wordlearningapp.repo;

import com.artur.vocab.wordlearningapp.domain.entity.WordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<WordEntity, Long> {

    List<WordEntity> findAllByOrderByCreatedAtDesc();
    Optional<WordEntity> findByText(String text);
}
