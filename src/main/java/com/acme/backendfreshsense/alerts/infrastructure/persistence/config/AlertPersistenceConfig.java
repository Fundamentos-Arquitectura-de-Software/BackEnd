package com.acme.backendfreshsense.alerts.infrastructure.persistence.config;

import com.acme.backendfreshsense.alerts.application.service.AlertService;
import com.acme.backendfreshsense.alerts.domain.repository.AlertRepository;
import com.acme.backendfreshsense.alerts.infrastructure.persistence.adapter.AlertRepositoryAdapter;
import com.acme.backendfreshsense.alerts.infrastructure.persistence.jpa.AlertJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlertPersistenceConfig {

    @Bean
    public AlertRepository alertRepository(AlertJpaRepository jpa) {
        return new AlertRepositoryAdapter(jpa);
    }

    @Bean
    public AlertService alertService(AlertRepository repo) {
        return new AlertService(repo);
    }
}
