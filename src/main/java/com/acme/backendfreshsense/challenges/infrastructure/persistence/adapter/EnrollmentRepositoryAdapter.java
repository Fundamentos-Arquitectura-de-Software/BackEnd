package com.acme.backendfreshsense.challenges.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.challenges.domain.model.Enrollment;
import com.acme.backendfreshsense.challenges.domain.repository.EnrollmentRepository;
import com.acme.backendfreshsense.challenges.infrastructure.persistence.jpa.EnrollmentEntity;
import com.acme.backendfreshsense.challenges.infrastructure.persistence.jpa.EnrollmentJpaRepository;

import java.util.List;
import java.util.Optional;

public class EnrollmentRepositoryAdapter implements EnrollmentRepository {

    private final EnrollmentJpaRepository jpa;

    public EnrollmentRepositoryAdapter(EnrollmentJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Enrollment> findByChallengeIdAndUserId(Long challengeId, Long userId) {
        return jpa.findByChallengeIdAndUserId(challengeId, userId).map(this::toDomain);
    }

    @Override
    public List<Enrollment> findByChallengeIdAndStatus(Long challengeId, String status) {
        return jpa.findByChallengeIdAndStatus(challengeId, status).stream().map(this::toDomain).toList();
    }

    @Override
    public Enrollment save(Enrollment enrollment) {
        EnrollmentEntity saved = jpa.save(toEntity(enrollment));
        return toDomain(saved);
    }

    private Enrollment toDomain(EnrollmentEntity e) {
        return Enrollment.builder()
                .id(e.getId())
                .challengeId(e.getChallengeId())
                .userId(e.getUserId())
                .progress(e.getProgress())
                .status(e.getStatus())
                .joinedAt(e.getJoinedAt())
                .leftAt(e.getLeftAt())
                .build();
    }

    private EnrollmentEntity toEntity(Enrollment e) {
        return EnrollmentEntity.builder()
                .id(e.getId())
                .challengeId(e.getChallengeId())
                .userId(e.getUserId())
                .progress(e.getProgress())
                .status(e.getStatus())
                .joinedAt(e.getJoinedAt())
                .leftAt(e.getLeftAt())
                .build();
    }
}
