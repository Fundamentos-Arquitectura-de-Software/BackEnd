package com.acme.recipesservice.application.service;

import com.acme.recipesservice.application.dto.CreateRecipeRequest;
import com.acme.recipesservice.application.dto.RecipeResponse;
import com.acme.recipesservice.infrastructure.ia.OpenAiRecipeClient;
import com.acme.recipesservice.infrastructure.ia.OpenAiRecipeClient.GeneratedRecipe;
import com.acme.recipesservice.infrastructure.image.PexelsImageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeBatchGenerationService {

    private final OpenAiRecipeClient openAiRecipeClient;
    private final PexelsImageClient pexelsImageClient;
    private final RecipeService recipeService;

    /**
     * Genera y persiste el catálogo completo:
     * 5 desayunos, 10 almuerzos, 5 snacks — cada uno con imagen real de Pexels.
     */
    public List<RecipeResponse> generateFullCatalog() {
        List<RecipeResponse> saved = new ArrayList<>();

        saved.addAll(generateAndSave("Breakfast", 5));
        saved.addAll(generateAndSave("Meals", 10));
        saved.addAll(generateAndSave("Snacks", 5));

        return saved;
    }

    private List<RecipeResponse> generateAndSave(String type, int count) {
        List<GeneratedRecipe> generated = openAiRecipeClient.generateBatch(type, count);
        List<RecipeResponse> result = new ArrayList<>();

        for (GeneratedRecipe g : generated) {
            String imageUrl = pexelsImageClient.searchImage(g.imageQuery());

            CreateRecipeRequest requestWithImage = CreateRecipeRequest.builder()
                    .title(g.recipe().getTitle())
                    .description(g.recipe().getDescription())
                    .image(imageUrl)
                    .rating(g.recipe().getRating())
                    .level(g.recipe().getLevel())
                    .type(g.recipe().getType())
                    .time(g.recipe().getTime())
                    .ingredients(g.recipe().getIngredients())
                    .steps(g.recipe().getSteps())
                    .build();

            result.add(recipeService.create(requestWithImage));
        }
        return result;
    }
}
