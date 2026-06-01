package com.acme.recipesservice.application.service;

import com.acme.recipesservice.application.dto.CreateRecipeRequest;
import com.acme.recipesservice.application.dto.RecipeResponse;
import com.acme.recipesservice.domain.model.Recipe;
import com.acme.recipesservice.domain.repository.RecipeRepository;
import com.acme.recipesservice.infrastructure.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public List<RecipeResponse> getAll() {
        return recipeRepository.findAll().stream().map(this::toResponse).toList();
    }

    public RecipeResponse getById(Long id) {
        return recipeRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id " + id));
    }

    public RecipeResponse create(CreateRecipeRequest request) {
        Recipe recipe = Recipe.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .imageUrl(request.getImage())
                .rating(request.getRating())
                .level(request.getLevel())
                .type(request.getType())
                .time(request.getTime())
                .ingredients(request.getIngredients())
                .steps(request.getSteps())
                .build();
        return toResponse(recipeRepository.save(recipe));
    }

    private RecipeResponse toResponse(Recipe recipe) {
        return RecipeResponse.builder()
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
