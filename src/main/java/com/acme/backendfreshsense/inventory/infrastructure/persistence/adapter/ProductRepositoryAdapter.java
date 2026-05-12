package com.acme.backendfreshsense.inventory.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.inventory.domain.model.entities.Product;
import com.acme.backendfreshsense.inventory.domain.repository.ProductRepository;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa.ProductEntity;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa.ProductJpaRepository;

import java.util.List;
import java.util.Optional;

public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository jpa;

    public ProductRepositoryAdapter(ProductJpaRepository jpa) {
        this.jpa = jpa;
    }

    private static Product toDomain(ProductEntity e) {
        return Product.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .category(e.getCategory())
                .quantity(e.getQuantity())
                .imageUrl(e.getImageUrl())
                .userId(e.getUserId())
                .build();
    }

    private static ProductEntity toEntity(Product p) {
        return ProductEntity.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .category(p.getCategory())
                .quantity(p.getQuantity())
                .imageUrl(p.getImageUrl())
                .userId(p.getUserId())
                .build();
    }

    @Override
    public Product save(Product product) {
        return toDomain(jpa.save(toEntity(product)));
    }

    @Override
    public List<Product> findByUserId(Long userId) {
        return jpa.findByUserId(userId).stream().map(ProductRepositoryAdapter::toDomain).toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpa.findById(id).map(ProductRepositoryAdapter::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }
}
