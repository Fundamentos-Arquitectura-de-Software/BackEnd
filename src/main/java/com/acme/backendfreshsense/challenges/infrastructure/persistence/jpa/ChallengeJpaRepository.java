package com.acme.backendfreshsense.challenges.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeJpaRepository extends JpaRepository<ChallengeEntity, Long> {
}
