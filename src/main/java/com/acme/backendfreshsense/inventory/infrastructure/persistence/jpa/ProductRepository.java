package com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa;

import com.acme.backendfreshsense.inventory.domain.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
