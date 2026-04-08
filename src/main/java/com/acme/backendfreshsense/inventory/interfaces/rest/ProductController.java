package com.acme.backendfreshsense.inventory.interfaces.rest;

import com.acme.backendfreshsense.inventory.application.internal.ProductService;
import com.acme.backendfreshsense.inventory.interfaces.rest.resources.ProductRequest;
import com.acme.backendfreshsense.inventory.interfaces.rest.resources.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200") // para Angular
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request) {
        ProductResponse created = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }
}
