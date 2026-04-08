package com.acme.backendfreshsense.achievements.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.achievements.domain.model.Achievement;
import com.acme.backendfreshsense.achievements.domain.repository.AchievementRepository;
import com.acme.backendfreshsense.achievements.infrastructure.persistence.jpa.AchievementEntity;
import com.acme.backendfreshsense.achievements.infrastructure.persistence.jpa.AchievementJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AchievementRepositoryAdapter implements AchievementRepository {

    private final AchievementJpaRepository jpa;

    public AchievementRepositoryAdapter(AchievementJpaRepository jpa) {
        this.jpa = jpa;
    }

    private static AchievementEntity toEntity(Achievement a) {
        return new AchievementEntity(a.getId(), a.getUserId(), a.getName(), a.getCompletionPercentage(), a.isDefault());
    }

    private static Achievement toDomain(AchievementEntity e) {
        return new Achievement(e.getId(), e.getUserId(), e.getName(), e.getCompletionPercentage(), e.isDefault());
    }

    @Override
    public List<Achievement> findByUserId(UUID userId) {
        return jpa.findByUserId(userId).stream().map(AchievementRepositoryAdapter::toDomain).toList();
    }

    @Override
    public Optional<Achievement> findByUserIdAndName(UUID userId, String name) {
        return jpa.findByUserIdAndName(userId, name).map(AchievementRepositoryAdapter::toDomain);
    }

    @Override
    public Optional<Achievement> findById(UUID id) {
        return jpa.findById(id).map(AchievementRepositoryAdapter::toDomain);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return jpa.existsByUserId(userId);
    }

    @Override
    public Achievement save(Achievement achievement) {
        AchievementEntity saved = jpa.save(toEntity(achievement));
        return toDomain(saved);
    }

    @Override
    public List<Achievement> saveAll(List<Achievement> achievements) {
        List<AchievementEntity> saved = jpa.saveAll(achievements.stream().map(AchievementRepositoryAdapter::toEntity).toList());
        return saved.stream().map(AchievementRepositoryAdapter::toDomain).toList();
    }
}