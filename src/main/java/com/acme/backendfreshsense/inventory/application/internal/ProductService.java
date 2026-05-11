package com.acme.backendfreshsense.inventory.application.internal;

import com.acme.backendfreshsense.inventory.domain.model.entities.Product;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa.ProductRepository;
import com.acme.backendfreshsense.inventory.interfaces.rest.resources.ProductRequest;
import com.acme.backendfreshsense.inventory.interfaces.rest.resources.ProductResponse;
import com.acme.backendfreshsense.inventory.interfaces.rest.resources.UpdateProductRequest;
import com.acme.backendfreshsense.shared.infrastructure.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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

    public ProductResponse update(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        if (request.quantity() != null) {
            product.setQuantity(request.quantity());
        }

        product = productRepository.save(product);
        return map(product);
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
