package com.artur.vocab.wordlearningapp.service;

import com.artur.vocab.wordlearningapp.domain.entity.WordGroupEntity;
import com.artur.vocab.wordlearningapp.repo.WordGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final WordGroupRepository wordGroupRepository;

    public Long resolveGroup(Long categoryId, String groupKey, String groupName) {
        if (categoryId == null || groupKey == null || groupKey.isBlank()) {
            return null;
        }

        Optional<WordGroupEntity> existingByKey = wordGroupRepository.findByGroupKey(groupKey);

        if (existingByKey.isPresent()) {
            return existingByKey.get().getId();
        }

        if (groupName != null) {
            Optional<WordGroupEntity> similarGroup = wordGroupRepository.findAll().stream()
                    .filter(g -> isSimilar(g.getName(), groupName) || isSimilar(g.getGroupKey(), groupKey))
                    .findFirst();

            if (similarGroup.isPresent()) {
                return similarGroup.get().getId();
            }
        }

        WordGroupEntity wordGroup = WordGroupEntity.builder()
                .categoryId(categoryId)
                .groupKey(groupKey)
                .name(groupName != null ? groupName : groupKey)
                .createdAt(Instant.now())
                .build();

        return wordGroupRepository.save(wordGroup).getId();
    }

    public WordGroupEntity getGroup(Long groupId) {
        if (groupId == null) {
            return null;
        }
        return wordGroupRepository.findById(groupId).orElse(null);
    }

    private boolean isSimilar(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        String normalizedA = a.toLowerCase().replaceAll("[^a-z]", "");
        String normalizedB = b.toLowerCase().replaceAll("[^a-z]", "");
        return normalizedA.contains(normalizedB) || normalizedB.contains(normalizedA);
    }
}
