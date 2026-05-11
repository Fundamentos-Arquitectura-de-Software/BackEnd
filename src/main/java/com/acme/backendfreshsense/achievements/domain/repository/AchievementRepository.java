package com.acme.backendfreshsense.achievements.domain.repository;

import com.acme.backendfreshsense.achievements.domain.model.Achievement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AchievementRepository {
    List<Achievement> findByUserId(Long userId);
    Optional<Achievement> findByUserIdAndName(Long userId, String name);
    Optional<Achievement> findById(UUID id);
    boolean existsByUserId(Long userId);
    Achievement save(Achievement achievement);
    List<Achievement> saveAll(List<Achievement> achievements);
}
