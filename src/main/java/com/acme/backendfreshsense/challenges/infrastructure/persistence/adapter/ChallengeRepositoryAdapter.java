package com.acme.backendfreshsense.challenges.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.challenges.domain.model.Challenge;
import com.acme.backendfreshsense.challenges.domain.repository.ChallengeRepository;
import com.acme.backendfreshsense.challenges.infrastructure.persistence.ChallengeJpaRepository;

import java.util.List;
import java.util.Optional;

public class ChallengeRepositoryAdapter implements ChallengeRepository {

    private final ChallengeJpaRepository jpa;

    public ChallengeRepositoryAdapter(ChallengeJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Challenge> findAll() {
        return jpa.findAll();
    }

    @Override
    public Optional<Challenge> findById(Long id) {
        return jpa.findById(id);
    }
}
