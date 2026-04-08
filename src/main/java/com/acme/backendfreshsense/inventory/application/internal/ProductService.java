package com.acme.backendfreshsense.inventory.application.internal;

import com.acme.backendfreshsense.inventory.domain.model.entities.Product;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa.ProductRepository;
import com.acme.backendfreshsense.inventory.interfaces.rest.resources.ProductRequest;
import com.acme.backendfreshsense.inventory.interfaces.rest.resources.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse create(ProductRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .category(request.category())
                .quantity(request.quantity())
                .imageUrl(request.imageUrl())
                .build();

        product = productRepository.save(product);
        return map(product);
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
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
