package com.acme.backendfreshsense.recipes.interfaces.rest;

import com.acme.backendfreshsense.recipes.application.internal.RecipeService;
import com.acme.backendfreshsense.recipes.interfaces.rest.resources.CreateRecipeResource;
import com.acme.backendfreshsense.recipes.interfaces.rest.resources.RecipeResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/recipes", produces = "application/json")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://127.0.0.1:4200"
})
public class RecipesController {

    private final RecipeService recipeService;

    @GetMapping
    public List<RecipeResource> getAll() {
        return recipeService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResource> getById(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getById(id));
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<RecipeResource> create(@RequestBody CreateRecipeResource resource) {
        RecipeResource created = recipeService.create(resource);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
