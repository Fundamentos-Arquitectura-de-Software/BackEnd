package com.acme.backendfreshsense.achievements.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AchievementJpaRepository extends JpaRepository<AchievementEntity, UUID> {
    List<AchievementEntity> findByUserId(UUID userId);
    Optional<AchievementEntity> findByUserIdAndName(UUID userId, String name);
    boolean existsByUserId(UUID userId);
}