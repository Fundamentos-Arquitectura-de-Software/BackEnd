package com.acme.backendfreshsense.inventory.application.service;

import com.acme.backendfreshsense.inventory.application.dto.ProductRequest;
import com.acme.backendfreshsense.inventory.application.dto.ProductResponse;
import com.acme.backendfreshsense.inventory.application.dto.UpdateProductRequest;
import com.acme.backendfreshsense.inventory.domain.model.entities.Product;
import com.acme.backendfreshsense.inventory.domain.repository.ProductRepository;
import com.acme.backendfreshsense.shared.infrastructure.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

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
                .build();
        return map(productRepository.save(product));
    }

    public List<ProductResponse> getAll(Long userId) {
        return productRepository.findAll().stream()
                .filter(p -> userId.equals(p.getUserId()))
                .map(this::map)
                .toList();
    }

    public ProductResponse update(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        if (request.quantity() != null) {
            product.setQuantity(request.quantity());
        }
        return map(productRepository.save(product));
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductResponse map(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getQuantity(),
                product.getImageUrl()
        );
    }
}
