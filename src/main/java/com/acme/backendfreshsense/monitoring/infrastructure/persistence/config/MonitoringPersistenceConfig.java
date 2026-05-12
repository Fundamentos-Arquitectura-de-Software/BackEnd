package com.acme.backendfreshsense.monitoring.infrastructure.persistence.config;

import com.acme.backendfreshsense.monitoring.application.service.MonitoringService;
import com.acme.backendfreshsense.monitoring.domain.repository.MonitoringReadingRepository;
import com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa.MonitoringJpaRepository;
import com.acme.backendfreshsense.monitoring.infrastructure.persistence.adapter.MonitoringReadingRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitoringPersistenceConfig {

    @Bean
    public MonitoringReadingRepository monitoringReadingRepository(MonitoringJpaRepository jpa) {
        return new MonitoringReadingRepositoryAdapter(jpa);
    }

    @Bean
    public MonitoringService monitoringService(MonitoringReadingRepository repo) {
        return new MonitoringService(repo);
    }
}
