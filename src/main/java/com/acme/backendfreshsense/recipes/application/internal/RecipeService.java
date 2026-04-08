package com.acme.backendfreshsense.recipes.application.internal;

import com.acme.backendfreshsense.recipes.domain.model.aggregates.Recipe;
import com.acme.backendfreshsense.recipes.infrastructure.persistence.jpa.RecipeRepository;
import com.acme.backendfreshsense.recipes.interfaces.rest.resources.CreateRecipeResource;
import com.acme.backendfreshsense.recipes.interfaces.rest.resources.RecipeResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public List<RecipeResource> getAll() {
        return recipeRepository.findAll()
                .stream()
                .map(this::toResource)
                .toList();
    }

    public RecipeResource getById(Long id) {
        return recipeRepository.findById(id)
                .map(this::toResource)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id " + id));
    }

    public RecipeResource create(CreateRecipeResource resource) {
        Recipe recipe = Recipe.builder()
                .title(resource.getTitle())
                .description(resource.getDescription())
                .imageUrl(resource.getImage())
                .rating(resource.getRating())
                .level(resource.getLevel())
                .type(resource.getType())
                .time(resource.getTime())
                .ingredients(resource.getIngredients())
                .steps(resource.getSteps())
                .build();

        Recipe saved = recipeRepository.save(recipe);
        return toResource(saved);
    }

    private RecipeResource toResource(Recipe recipe) {
        return RecipeResource.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .image(recipe.getImageUrl())
                .rating(recipe.getRating())
                .level(recipe.getLevel())
                .type(recipe.getType())
                .time(recipe.getTime())
                .ingredients(recipe.getIngredients())
                .steps(recipe.getSteps())
                .build();
    }
}
