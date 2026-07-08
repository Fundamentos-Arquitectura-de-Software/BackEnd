package com.acme.recipesservice.application.service;

import com.acme.recipesservice.application.dto.CreateRecipeRequest;
import com.acme.recipesservice.application.dto.RecipeResponse;
import com.acme.recipesservice.infrastructure.ia.OpenAiRecipeClient;
import com.acme.recipesservice.infrastructure.ia.OpenAiRecipeClient.GeneratedRecipe;
import com.acme.recipesservice.infrastructure.image.PexelsImageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecipeBatchGenerationService {

    /** Tope del catálogo: la generación nunca lo hace crecer más allá de este número. */
    private static final int MAX_CATALOG_SIZE = 100;

    /** Imagen genérica de comida (Pexels, verificada) si la búsqueda no encuentra nada. */
    private static final String FALLBACK_IMAGE =
            "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=800";

    private final OpenAiRecipeClient openAiRecipeClient;
    private final PexelsImageClient pexelsImageClient;
    private final RecipeService recipeService;

    /**
     * Genera y persiste el catálogo completo:
     * 5 desayunos, 10 almuerzos, 5 snacks — cada uno con imagen real de Pexels.
     * A la IA se le pasan los títulos ya existentes para que no los repita, y además
     * se descarta cualquier generado cuyo título ya exista (comparación sin tildes/mayúsculas).
     */
    public List<RecipeResponse> generateFullCatalog() {
        List<String> existingTitles = recipeService.getAll().stream()
                .map(RecipeResponse::getTitle)
                .toList();
        Set<String> knownTitles = new HashSet<>(existingTitles.stream().map(this::normalize).toList());

        // Tope de catálogo: solo se genera lo que quepa hasta MAX_CATALOG_SIZE.
        int available = MAX_CATALOG_SIZE - existingTitles.size();
        if (available <= 0) return List.of();
        int breakfast = Math.min(5, available);
        int meals = Math.min(10, available - breakfast);
        int snacks = Math.min(5, available - breakfast - meals);

        List<RecipeResponse> saved = new ArrayList<>();
        saved.addAll(generateAndSave("Breakfast", breakfast, existingTitles, knownTitles));
        saved.addAll(generateAndSave("Meals", meals, existingTitles, knownTitles));
        saved.addAll(generateAndSave("Snacks", snacks, existingTitles, knownTitles));
        return saved;
    }

    private List<RecipeResponse> generateAndSave(String type, int count,
                                                 List<String> existingTitles, Set<String> knownTitles) {
        if (count <= 0) return List.of();
        List<GeneratedRecipe> generated = openAiRecipeClient.generateBatch(type, count, existingTitles);
        List<RecipeResponse> result = new ArrayList<>();

        for (GeneratedRecipe g : generated) {
            // Cinturón de seguridad anti-duplicados: si el título ya existe, se descarta.
            String key = normalize(g.recipe().getTitle());
            if (key.isBlank() || !knownTitles.add(key)) continue;

            CreateRecipeRequest requestWithImage = CreateRecipeRequest.builder()
                    .title(g.recipe().getTitle())
                    .description(g.recipe().getDescription())
                    .image(resolveImage(g))
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

    /** Busca imagen con el query de la IA; si no hay resultado, reintenta simplificado y cae a una genérica. */
    private String resolveImage(GeneratedRecipe g) {
        String imageUrl = pexelsImageClient.searchImage(g.imageQuery());
        if (imageUrl == null || imageUrl.isBlank()) {
            String simpler = g.imageQuery().split("\\s+")[0];
            imageUrl = pexelsImageClient.searchImage(simpler + " food");
        }
        return (imageUrl == null || imageUrl.isBlank()) ? FALLBACK_IMAGE : imageUrl;
    }

    /** Minúsculas y sin tildes, para comparar títulos de forma robusta. */
    private String normalize(String text) {
        if (text == null) return "";
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
    }
}
