package com.artur.vocab.wordlearningapp.service;

import com.artur.vocab.wordlearningapp.domain.entity.CategoryEntity;
import com.artur.vocab.wordlearningapp.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Long resolveCategory(String categoryCode) {
        return categoryRepository.findByCode(categoryCode)
                .or(() -> categoryRepository.findByCode("GENERAL"))
                .map(CategoryEntity::getId)
                .orElse(null);
    }

    public CategoryEntity getCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.findById(categoryId).orElse(null);
    }
}
