package com.acme.backendfreshsense.recipes.infrastructure.web;

import com.acme.backendfreshsense.recipes.application.dto.CreateRecipeRequest;
import com.acme.backendfreshsense.recipes.application.dto.RecipeResponse;
import com.acme.backendfreshsense.recipes.application.service.RecipeService;
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

    @PostMapping(consumes = "application/json")
    public ResponseEntity<RecipeResponse> create(@RequestBody CreateRecipeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.create(request));
    }
}
