package com.acme.recipesservice.infrastructure.web;

import com.acme.recipesservice.application.dto.CreateRecipeRequest;
import com.acme.recipesservice.application.dto.RecipeResponse;
import com.acme.recipesservice.application.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/recipes", produces = "application/json")
@RequiredArgsConstructor
public class RecipesController {

    private final RecipeService recipeService;

    @GetMapping
    public List<RecipeResponse> getAll() {
        return recipeService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getById(id));
    }

    /** Devuelve recetas de nivel avanzado — la seguridad (PREMIUM/ADMIN) se aplica en el monolito */
    @GetMapping("/premium")
    public ResponseEntity<List<RecipeResponse>> getPremium() {
        List<RecipeResponse> premium = recipeService.getAll().stream()
                .filter(r -> "advanced".equalsIgnoreCase(r.getLevel())
                        || "expert".equalsIgnoreCase(r.getLevel())
                        || "difícil".equalsIgnoreCase(r.getLevel()))
                .toList();
        return ResponseEntity.ok(premium);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<RecipeResponse> create(@RequestBody CreateRecipeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.create(request));
    }
}
