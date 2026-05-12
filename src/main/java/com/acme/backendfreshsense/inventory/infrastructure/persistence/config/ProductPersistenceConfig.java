package com.acme.backendfreshsense.inventory.infrastructure.persistence.config;

import com.acme.backendfreshsense.inventory.application.service.ProductService;
import com.acme.backendfreshsense.inventory.domain.repository.ProductRepository;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.adapter.ProductRepositoryAdapter;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa.ProductJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductPersistenceConfig {

    @Bean
    public ProductRepository productRepository(ProductJpaRepository jpa) {
        return new ProductRepositoryAdapter(jpa);
    }

    @Bean
    public ProductService productService(ProductRepository repo) {
        return new ProductService(repo);
    }
}
