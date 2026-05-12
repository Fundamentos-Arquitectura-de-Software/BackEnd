package com.acme.backendfreshsense.recipes.domain.repository;

import com.acme.backendfreshsense.recipes.domain.model.aggregates.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository {
    List<Recipe> findAll();
    Optional<Recipe> findById(Long id);
    Recipe save(Recipe recipe);
    boolean hasAny();
}
