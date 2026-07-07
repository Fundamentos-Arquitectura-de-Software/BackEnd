package com.acme.backendfreshsense.inventory.application.service;

import com.acme.backendfreshsense.inventory.application.dto.ProductRequest;
import com.acme.backendfreshsense.inventory.application.dto.ProductResponse;
import com.acme.backendfreshsense.inventory.application.dto.UpdateProductRequest;
import com.acme.backendfreshsense.inventory.domain.model.entities.Product;
import com.acme.backendfreshsense.inventory.domain.repository.ProductRepository;
import com.acme.backendfreshsense.shared.infrastructure.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse create(Long userId, ProductRequest request) {
        Product product = Product.builder()
                .userId(userId)
                .name(request.name())
                .description(request.description())
                .category(request.category())
                .quantity(request.quantity())
                .imageUrl(request.imageUrl())
                .expirationDate(request.expirationDate())
                .createdAt(LocalDateTime.now())
                .build();
        return map(productRepository.save(product));
    }

    public List<ProductResponse> getAll(Long userId) {
        return productRepository.findByUserId(userId).stream()
                .map(this::map)
                .toList();
    }

    public ProductResponse update(Long userId, Long id, UpdateProductRequest request) {
        Product product = productRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        if (request.name() != null)        product.setName(request.name());
        if (request.description() != null) product.setDescription(request.description());
        if (request.category() != null)    product.setCategory(request.category());
        if (request.quantity() != null)    product.setQuantity(request.quantity());
        if (request.imageUrl() != null)    product.setImageUrl(request.imageUrl());
        if (request.expirationDate() != null) product.setExpirationDate(request.expirationDate());
        return map(productRepository.save(product));
    }

    public void delete(Long userId, Long id) {
        Product product = productRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        productRepository.deleteById(product.getId());
    }

    private ProductResponse map(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getQuantity(),
                product.getImageUrl(),
                product.getExpirationDate(),
                product.getCreatedAt()
        );
    }
}
