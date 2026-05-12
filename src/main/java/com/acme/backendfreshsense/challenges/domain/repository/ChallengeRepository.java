package com.acme.backendfreshsense.challenges.domain.repository;

import com.acme.backendfreshsense.challenges.domain.model.Challenge;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository {
    List<Challenge> findAll();
    Optional<Challenge> findById(Long id);
}
