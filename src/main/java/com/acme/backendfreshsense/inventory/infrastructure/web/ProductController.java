package com.acme.backendfreshsense.inventory.infrastructure.web;

import com.acme.backendfreshsense.inventory.application.dto.ProductRequest;
import com.acme.backendfreshsense.inventory.application.dto.ProductResponse;
import com.acme.backendfreshsense.inventory.application.dto.UpdateProductRequest;
import com.acme.backendfreshsense.inventory.application.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(request, currentUserId()));
    }

    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.getAll(currentUserId());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Long currentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }
}
