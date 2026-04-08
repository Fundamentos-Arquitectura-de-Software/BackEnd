package com.acme.backendfreshsense.recipes.infrastructure.persistence.jpa;

import com.acme.backendfreshsense.recipes.domain.model.aggregates.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
