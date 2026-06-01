package com.acme.backendfreshsense.recipes.infrastructure.web;

import com.acme.backendfreshsense.recipes.application.dto.CreateRecipeRequest;
import com.acme.backendfreshsense.recipes.application.dto.RecipeResponse;
import com.acme.backendfreshsense.recipes.infrastructure.feign.RecipesFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/recipes", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Recipes", description = "Recetas personalizadas")
public class RecipesController {

    private final RecipesFeignClient recipesFeignClient;

    @GetMapping
    @Operation(summary = "Listar recetas estándar (todos los usuarios)")
    public List<RecipeResponse> getAll() {
        return recipesFeignClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getById(@PathVariable Long id) {
        return recipesFeignClient.getById(id);
    }

    /** US19 — Recetas exclusivas premium (requiere USER_PREMIUM o ADMIN). */
    @GetMapping("/premium")
    @PreAuthorize("hasAnyRole('USER_PREMIUM', 'ADMIN')")
    @Operation(summary = "US19: Recetas exclusivas para suscriptores premium")
    public ResponseEntity<List<RecipeResponse>> getPremium() {
        return recipesFeignClient.getPremium();
    }

    @PostMapping(consumes = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear receta (solo ADMIN)")
    public ResponseEntity<RecipeResponse> create(@RequestBody CreateRecipeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipesFeignClient.create(request));
    }
}
