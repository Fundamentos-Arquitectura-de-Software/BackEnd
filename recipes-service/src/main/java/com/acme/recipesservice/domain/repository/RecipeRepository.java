package com.acme.recipesservice.domain.repository;

import com.acme.recipesservice.domain.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository {
    List<Recipe> findAll();
    /** Recetas visibles para un usuario: las base (sin dueño) + las propias. */
    List<Recipe> findVisibleTo(Long userId);
    long countOwnedBy(Long userId);
    Optional<Recipe> findById(Long id);
    Recipe save(Recipe recipe);
    boolean hasAny();
    void deleteById(Long id);
}
