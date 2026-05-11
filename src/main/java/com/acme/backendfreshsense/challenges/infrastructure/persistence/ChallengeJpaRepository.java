package com.acme.backendfreshsense.challenges.infrastructure.persistence;

import com.acme.backendfreshsense.challenges.domain.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeJpaRepository extends JpaRepository<Challenge, Long> {
}
