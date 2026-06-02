package com.acme.backendfreshsense.recipes.infrastructure.web;

import com.acme.backendfreshsense.recipes.application.dto.CreateRecipeRequest;
import com.acme.backendfreshsense.recipes.application.dto.RecipeResponse;
import com.acme.backendfreshsense.recipes.application.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    private final RecipeService recipeService;

    @GetMapping
    @Operation(summary = "Listar recetas estándar (todos los usuarios)")
    public List<RecipeResponse> getAll() {
        return recipeService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getById(id));
    }

    /** US19 — Recetas exclusivas premium (requiere USER_PREMIUM o ADMIN). */
    @GetMapping("/premium")
    @PreAuthorize("hasAnyRole('USER_PREMIUM', 'ADMIN')")
    @Operation(summary = "US19: Recetas exclusivas para suscriptores premium")
    public ResponseEntity<List<RecipeResponse>> getPremium() {
        // Filtra recetas de nivel avanzado (campo level)
        List<RecipeResponse> premium = recipeService.getAll().stream()
                .filter(r -> "advanced".equalsIgnoreCase(r.getLevel())
                          || "expert".equalsIgnoreCase(r.getLevel())
                          || "difícil".equalsIgnoreCase(r.getLevel()))
                .toList();
        return ResponseEntity.ok(premium);
    }

    @PostMapping(consumes = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear receta (solo ADMIN)")
    public ResponseEntity<RecipeResponse> create(@Valid @RequestBody CreateRecipeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.create(request));
    }
}
