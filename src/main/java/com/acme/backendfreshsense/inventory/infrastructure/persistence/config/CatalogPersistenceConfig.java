package com.acme.backendfreshsense.inventory.infrastructure.persistence.config;

import com.acme.backendfreshsense.inventory.application.service.CatalogService;
import com.acme.backendfreshsense.inventory.domain.repository.CatalogItemRepository;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.adapter.CatalogItemRepositoryAdapter;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa.CatalogItemJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogPersistenceConfig {

    @Bean
    public CatalogItemRepository catalogItemRepository(CatalogItemJpaRepository jpa) {
        return new CatalogItemRepositoryAdapter(jpa);
    }

    @Bean
    public CatalogService catalogService(CatalogItemRepository repo) {
        return new CatalogService(repo);
    }
}
