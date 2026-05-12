package com.acme.backendfreshsense.reports.infrastructure.persistence.config;

import com.acme.backendfreshsense.reports.application.service.HistoryService;
import com.acme.backendfreshsense.reports.domain.repository.HistoryRepository;
import com.acme.backendfreshsense.reports.infrastructure.persistence.HistoryJpaRepository;
import com.acme.backendfreshsense.reports.infrastructure.persistence.HistoryRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportsPersistenceConfig {

    @Bean
    public HistoryRepository historyRepository(HistoryJpaRepository jpa) {
        return new HistoryRepositoryAdapter(jpa);
    }

    @Bean
    public HistoryService historyService(HistoryRepository historyRepository) {
        return new HistoryService(historyRepository);
    }
}
