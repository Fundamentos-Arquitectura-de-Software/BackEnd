package com.acme.backendfreshsense.challenges.infrastructure.persistence.config;

import com.acme.backendfreshsense.challenges.application.service.ChallengeService;
import com.acme.backendfreshsense.challenges.domain.repository.ChallengeRepository;
import com.acme.backendfreshsense.challenges.domain.repository.EnrollmentRepository;
import com.acme.backendfreshsense.challenges.infrastructure.persistence.ChallengeJpaRepository;
import com.acme.backendfreshsense.challenges.infrastructure.persistence.EnrollmentJpaRepository;
import com.acme.backendfreshsense.challenges.infrastructure.persistence.adapter.ChallengeRepositoryAdapter;
import com.acme.backendfreshsense.challenges.infrastructure.persistence.adapter.EnrollmentRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChallengePersistenceConfig {

    @Bean
    public ChallengeRepository challengeRepository(ChallengeJpaRepository jpa) {
        return new ChallengeRepositoryAdapter(jpa);
    }

    @Bean
    public EnrollmentRepository enrollmentRepository(EnrollmentJpaRepository jpa) {
        return new EnrollmentRepositoryAdapter(jpa);
    }

    @Bean
    public ChallengeService challengeService(ChallengeRepository challengeRepo, EnrollmentRepository enrollmentRepo) {
        return new ChallengeService(challengeRepo, enrollmentRepo);
    }
}
