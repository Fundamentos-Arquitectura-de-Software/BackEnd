package com.acme.backendfreshsense.challenges.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentJpaRepository extends JpaRepository<EnrollmentEntity, Long> {
    List<EnrollmentEntity> findByChallengeIdAndStatus(Long challengeId, String status);
    List<EnrollmentEntity> findByUserIdAndStatus(Long userId, String status);
    Optional<EnrollmentEntity> findByChallengeIdAndUserId(Long challengeId, Long userId);
}
