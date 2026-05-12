package com.acme.backendfreshsense.challenges.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.challenges.domain.model.Enrollment;
import com.acme.backendfreshsense.challenges.domain.repository.EnrollmentRepository;
import com.acme.backendfreshsense.challenges.infrastructure.persistence.EnrollmentJpaRepository;

import java.util.List;
import java.util.Optional;

public class EnrollmentRepositoryAdapter implements EnrollmentRepository {

    private final EnrollmentJpaRepository jpa;

    public EnrollmentRepositoryAdapter(EnrollmentJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Enrollment> findByChallengeIdAndUserId(Long challengeId, Long userId) {
        return jpa.findByChallengeIdAndUserId(challengeId, userId);
    }

    @Override
    public List<Enrollment> findByChallengeIdAndStatus(Long challengeId, String status) {
        return jpa.findByChallengeIdAndStatus(challengeId, status);
    }

    @Override
    public Enrollment save(Enrollment enrollment) {
        return jpa.save(enrollment);
    }
}
