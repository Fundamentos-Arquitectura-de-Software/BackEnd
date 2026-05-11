package com.acme.backendfreshsense.achievements.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AchievementJpaRepository extends JpaRepository<AchievementEntity, UUID> {
    List<AchievementEntity> findByUserId(Long userId);
    Optional<AchievementEntity> findByUserIdAndName(Long userId, String name);
    boolean existsByUserId(Long userId);
}
