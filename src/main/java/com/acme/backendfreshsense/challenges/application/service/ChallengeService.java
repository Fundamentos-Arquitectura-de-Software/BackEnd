package com.acme.backendfreshsense.challenges.application.service;

import com.acme.backendfreshsense.challenges.application.dto.ChallengeDto;
import com.acme.backendfreshsense.challenges.application.dto.LeaderboardEntryDto;
import com.acme.backendfreshsense.challenges.domain.model.Challenge;
import com.acme.backendfreshsense.challenges.domain.model.Enrollment;
import com.acme.backendfreshsense.challenges.domain.repository.ChallengeRepository;
import com.acme.backendfreshsense.challenges.domain.repository.EnrollmentRepository;
import com.acme.backendfreshsense.shared.infrastructure.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepo;
    private final EnrollmentRepository enrollmentRepo;

    public List<ChallengeDto> getAllChallenges() {
        return challengeRepo.findAll().stream().map(this::toDto).toList();
    }

    public void enroll(Long challengeId, Long userId) {
        challengeRepo.findById(challengeId)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge no encontrado: " + challengeId));

        enrollmentRepo.findByChallengeIdAndUserId(challengeId, userId).ifPresent(e -> {
            if ("enrolled".equals(e.getStatus())) {
                throw new IllegalStateException("Ya estás inscrito en este desafío");
            }
        });

        Enrollment enrollment = Enrollment.builder()
                .challengeId(challengeId)
                .userId(userId)
                .progress(0)
                .status("enrolled")
                .joinedAt(LocalDateTime.now())
                .build();
        enrollmentRepo.save(enrollment);
    }

    public void leave(Long challengeId, Long userId) {
        Enrollment enrollment = enrollmentRepo.findByChallengeIdAndUserId(challengeId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada"));
        enrollment.setStatus("left");
        enrollment.setLeftAt(LocalDateTime.now());
        enrollmentRepo.save(enrollment);
    }

    public void updateProgress(Long challengeId, Long userId, int progress) {
        Enrollment enrollment = enrollmentRepo.findByChallengeIdAndUserId(challengeId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada"));
        int clamped = Math.max(0, Math.min(100, progress));
        enrollment.setProgress(clamped);
        enrollmentRepo.save(enrollment);
    }

    public List<LeaderboardEntryDto> getLeaderboard(Long challengeId) {
        List<Enrollment> enrollments = enrollmentRepo.findByChallengeIdAndStatus(challengeId, "enrolled");
        AtomicInteger rank = new AtomicInteger(1);
        return enrollments.stream()
                .sorted((a, b) -> b.getProgress() - a.getProgress())
                .map(e -> new LeaderboardEntryDto(e.getUserId(), e.getChallengeId(), e.getProgress(), rank.getAndIncrement()))
                .toList();
    }

    private ChallengeDto toDto(Challenge c) {
        return new ChallengeDto(
                c.getId(), c.getTitle(), c.getDescription(), c.getRewardPts(),
                c.getStartAt(), c.getEndAt(), c.getGoalType(), c.getGoalTarget(),
                c.getStatus(), c.getBannerUrl()
        );
    }
}
