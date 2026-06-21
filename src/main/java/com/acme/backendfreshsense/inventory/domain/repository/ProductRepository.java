package com.acme.backendfreshsense.inventory.domain.repository;

import com.acme.backendfreshsense.inventory.domain.model.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    List<Product> findByUserId(Long userId);
    Optional<Product> findByIdAndUserId(Long id, Long userId);
    void deleteById(Long id);
}
