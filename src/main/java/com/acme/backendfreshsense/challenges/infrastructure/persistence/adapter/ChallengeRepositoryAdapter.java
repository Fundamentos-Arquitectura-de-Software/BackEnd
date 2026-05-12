package com.acme.backendfreshsense.challenges.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.challenges.domain.model.Challenge;
import com.acme.backendfreshsense.challenges.domain.repository.ChallengeRepository;
import com.acme.backendfreshsense.challenges.infrastructure.persistence.jpa.ChallengeEntity;
import com.acme.backendfreshsense.challenges.infrastructure.persistence.jpa.ChallengeJpaRepository;

import java.util.List;
import java.util.Optional;

public class ChallengeRepositoryAdapter implements ChallengeRepository {

    private final ChallengeJpaRepository jpa;

    public ChallengeRepositoryAdapter(ChallengeJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Challenge> findAll() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Challenge> findById(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    private Challenge toDomain(ChallengeEntity e) {
        return Challenge.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .rewardPts(e.getRewardPts())
                .startAt(e.getStartAt())
                .endAt(e.getEndAt())
                .goalType(e.getGoalType())
                .goalTarget(e.getGoalTarget())
                .status(e.getStatus())
                .bannerUrl(e.getBannerUrl())
                .build();
    }
}
