package com.acme.backendfreshsense.achievements.infrastructure.persistence.config;

import com.acme.backendfreshsense.achievements.application.service.AchievementService;
import com.acme.backendfreshsense.achievements.domain.repository.AchievementRepository;
import com.acme.backendfreshsense.achievements.infrastructure.persistence.adapter.AchievementRepositoryAdapter;
import com.acme.backendfreshsense.achievements.infrastructure.persistence.jpa.AchievementJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AchievementPersistenceConfig {

    @Bean
    public AchievementRepository achievementRepository(AchievementJpaRepository jpa) {
        return new AchievementRepositoryAdapter(jpa);
    }

    @Bean
    public AchievementService achievementService(AchievementRepository repo) {
        return new AchievementService(repo);
    }
}