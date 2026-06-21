package com.acme.backendfreshsense.monitoring.infrastructure.persistence.config;

import com.acme.backendfreshsense.monitoring.application.service.DeviceService;
import com.acme.backendfreshsense.monitoring.application.service.MonitoringService;
import com.acme.backendfreshsense.monitoring.domain.repository.DeviceRepository;
import com.acme.backendfreshsense.monitoring.domain.repository.MonitoringReadingRepository;
import com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa.DeviceJpaRepository;
import com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa.MonitoringJpaRepository;
import com.acme.backendfreshsense.monitoring.infrastructure.persistence.adapter.DeviceRepositoryAdapter;
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

    @Bean
    public DeviceRepository deviceRepository(DeviceJpaRepository jpa) {
        return new DeviceRepositoryAdapter(jpa);
    }

    @Bean
    public DeviceService deviceService(DeviceRepository deviceRepository) {
        return new DeviceService(deviceRepository);
    }
}
