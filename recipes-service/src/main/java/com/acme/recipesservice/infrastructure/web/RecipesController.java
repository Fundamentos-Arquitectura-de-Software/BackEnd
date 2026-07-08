package com.acme.recipesservice.infrastructure.web;

import com.acme.recipesservice.application.dto.CreateRecipeRequest;
import com.acme.recipesservice.application.dto.RecipeResponse;
import com.acme.recipesservice.application.service.RecipeBatchGenerationService;
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
    private final RecipeBatchGenerationService recipeBatchGenerationService;

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

    /**
     * Genera un catálogo completo con IA (5 desayunos, 10 almuerzos, 5 snacks),
     * cada uno con una imagen real buscada en Pexels, y lo persiste en la BD.
     */
    @PostMapping("/generate-batch")
    public ResponseEntity<List<RecipeResponse>> generateBatch() {
        List<RecipeResponse> generated = recipeBatchGenerationService.generateFullCatalog();
        return ResponseEntity.status(HttpStatus.CREATED).body(generated);
    }
}
