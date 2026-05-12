package com.acme.backendfreshsense.challenges.domain.repository;

import com.acme.backendfreshsense.challenges.domain.model.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {
    Optional<Enrollment> findByChallengeIdAndUserId(Long challengeId, Long userId);
    List<Enrollment> findByChallengeIdAndStatus(Long challengeId, String status);
    Enrollment save(Enrollment enrollment);
}
