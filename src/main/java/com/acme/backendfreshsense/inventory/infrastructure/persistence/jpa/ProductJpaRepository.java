package com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByUserId(Long userId);
    Optional<ProductEntity> findByIdAndUserId(Long id, Long userId);
}
