package com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogItemJpaRepository extends JpaRepository<CatalogItemEntity, Long> {

    List<CatalogItemEntity> findByNameContainingIgnoreCaseOrderByName(String name);

    List<CatalogItemEntity> findByCategoryIgnoreCaseOrderByName(String category);

    List<CatalogItemEntity> findByCategoryIgnoreCaseAndNameContainingIgnoreCaseOrderByName(String category, String name);

    List<CatalogItemEntity> findAllByOrderByName();

    boolean existsByNameIgnoreCase(String name);
}
