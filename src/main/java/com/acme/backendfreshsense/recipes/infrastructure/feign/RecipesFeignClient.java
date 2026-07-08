package com.acme.backendfreshsense.recipes.infrastructure.feign;

import com.acme.backendfreshsense.recipes.application.dto.CreateRecipeRequest;
import com.acme.backendfreshsense.recipes.application.dto.RecipeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Cliente Feign que delega las llamadas al microservicio recipes-service.
 * Eureka resuelve "recipes-service" al host/puerto real en tiempo de ejecución.
 */
@FeignClient(name = "recipes-service")
public interface RecipesFeignClient {

    @GetMapping("/api/recipes")
    List<RecipeResponse> getAll();

    @GetMapping("/api/recipes/{id}")
    ResponseEntity<RecipeResponse> getById(@PathVariable("id") Long id);

    @GetMapping("/api/recipes/premium")
    ResponseEntity<List<RecipeResponse>> getPremium();

    @PostMapping(value = "/api/recipes", consumes = "application/json")
    RecipeResponse create(@RequestBody CreateRecipeRequest request);

    @PostMapping("/api/recipes/generate-batch")
    List<RecipeResponse> generateBatch();
}
