package com.acme.backendfreshsense.inventory.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.inventory.domain.model.entities.CatalogItem;
import com.acme.backendfreshsense.inventory.domain.repository.CatalogItemRepository;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa.CatalogItemEntity;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa.CatalogItemJpaRepository;

import java.util.List;

public class CatalogItemRepositoryAdapter implements CatalogItemRepository {

    private final CatalogItemJpaRepository jpa;

    public CatalogItemRepositoryAdapter(CatalogItemJpaRepository jpa) {
        this.jpa = jpa;
    }

    private static CatalogItem toDomain(CatalogItemEntity e) {
        return CatalogItem.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .category(e.getCategory())
                .imageUrl(e.getImageUrl())
                .build();
    }

    private static CatalogItemEntity toEntity(CatalogItem item) {
        return CatalogItemEntity.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .category(item.getCategory())
                .imageUrl(item.getImageUrl())
                .build();
    }

    @Override
    public List<CatalogItem> search(String name, String category) {
        boolean hasName = name != null && !name.isBlank();
        boolean hasCategory = category != null && !category.isBlank();

        List<CatalogItemEntity> found;
        if (hasName && hasCategory) {
            found = jpa.findByCategoryIgnoreCaseAndNameContainingIgnoreCaseOrderByName(category.trim(), name.trim());
        } else if (hasName) {
            found = jpa.findByNameContainingIgnoreCaseOrderByName(name.trim());
        } else if (hasCategory) {
            found = jpa.findByCategoryIgnoreCaseOrderByName(category.trim());
        } else {
            found = jpa.findAllByOrderByName();
        }
        return found.stream().map(CatalogItemRepositoryAdapter::toDomain).toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpa.existsByNameIgnoreCase(name);
    }

    @Override
    public CatalogItem save(CatalogItem item) {
        return toDomain(jpa.save(toEntity(item)));
    }
}
