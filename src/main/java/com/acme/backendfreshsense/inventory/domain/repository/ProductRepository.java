package com.acme.backendfreshsense.inventory.domain.repository;

import com.acme.backendfreshsense.inventory.domain.model.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    List<Product> findAll();
    Optional<Product> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
}
