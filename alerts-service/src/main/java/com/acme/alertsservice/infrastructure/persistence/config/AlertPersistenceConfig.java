package com.acme.alertsservice.infrastructure.persistence.config;

import com.acme.alertsservice.application.service.AlertService;
import com.acme.alertsservice.domain.repository.AlertRepository;
import com.acme.alertsservice.infrastructure.persistence.adapter.AlertRepositoryAdapter;
import com.acme.alertsservice.infrastructure.persistence.jpa.AlertJpaRepository;
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
