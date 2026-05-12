package com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByUserId(Long userId);
}
