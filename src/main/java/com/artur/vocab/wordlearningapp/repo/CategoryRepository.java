package com.artur.vocab.wordlearningapp.repo;

import com.artur.vocab.wordlearningapp.domain.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByCode(String code);
}
