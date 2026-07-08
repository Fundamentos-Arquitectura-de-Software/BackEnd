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

    /** Con userId devuelve las base + las propias del usuario; sin userId, todo el catálogo. */
    @GetMapping
    public List<RecipeResponse> getAll(@RequestParam(required = false) Long userId) {
        return recipeService.getVisibleTo(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getById(id));
    }

    /** Devuelve recetas de nivel avanzado — la seguridad (PREMIUM/ADMIN) se aplica en el monolito */
    @GetMapping("/premium")
    public ResponseEntity<List<RecipeResponse>> getPremium(@RequestParam(required = false) Long userId) {
        List<RecipeResponse> premium = recipeService.getVisibleTo(userId).stream()
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
     * Genera recetas con IA (5 desayunos, 10 almuerzos, 5 snacks) PRIVADAS del usuario,
     * cada una con una imagen real buscada en Pexels, y las persiste en la BD.
     */
    @PostMapping("/generate-batch")
    public ResponseEntity<List<RecipeResponse>> generateBatch(@RequestParam Long userId) {
        List<RecipeResponse> generated = recipeBatchGenerationService.generateFullCatalog(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(generated);
    }
}
