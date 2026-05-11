package com.acme.backendfreshsense.challenges.infrastructure.persistence;

import com.acme.backendfreshsense.challenges.domain.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentJpaRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByChallengeIdAndStatus(Long challengeId, String status);
    List<Enrollment> findByUserIdAndStatus(Long userId, String status);
    Optional<Enrollment> findByChallengeIdAndUserId(Long challengeId, Long userId);
}
