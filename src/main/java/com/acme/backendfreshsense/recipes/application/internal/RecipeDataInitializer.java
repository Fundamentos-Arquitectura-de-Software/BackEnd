package com.acme.backendfreshsense.recipes.application.internal;

import com.acme.backendfreshsense.recipes.domain.model.aggregates.Recipe;
import com.acme.backendfreshsense.recipes.infrastructure.persistence.jpa.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecipeDataInitializer implements CommandLineRunner {

    private final RecipeRepository recipeRepository;

    @Override
    public void run(String... args) {
        if (recipeRepository.count() > 0) return;

        Recipe waffles = Recipe.builder()
                .title("Crispy Waffles with Berries")
                .description("Light and crispy waffles topped with fresh berries and honey.")
                .imageUrl("https://images.pexels.com/photos/376464/pexels-photo-376464.jpeg?auto=compress&cs=tinysrgb&w=960&h=720&dpr=1")
                .rating(5)
                .level("Easy")
                .type("Breakfast")
                .time("20 min")
                .ingredients(List.of(
                        "2 cups all-purpose flour",
                        "2 eggs",
                        "1 3/4 cups milk",
                        "1/2 cup melted butter",
                        "1 tbsp sugar",
                        "Fresh berries",
                        "Honey to taste"
                ))
                .steps(List.of(
                        "Preheat the waffle iron.",
                        "Mix flour, sugar, eggs, milk and melted butter.",
                        "Cook until golden and crispy.",
                        "Serve with berries and honey."
                ))
                .build();

        Recipe salad = Recipe.builder()
                .title("Quinoa & Avocado Green Salad")
                .description("Fresh salad with avocado, quinoa and crunchy vegetables.")
                .imageUrl("https://images.pexels.com/photos/1640772/pexels-photo-1640772.jpeg?auto=compress&cs=tinysrgb&w=960&h=720&dpr=1")
                .rating(4)
                .level("Easy")
                .type("Meals")
                .time("15 min")
                .ingredients(List.of(
                        "1 cup cooked quinoa",
                        "1 avocado",
                        "1 cucumber",
                        "Cherry tomatoes",
                        "Lettuce mix"
                ))
                .steps(List.of(
                        "Combine lettuce, quinoa, cucumber and tomatoes.",
                        "Add avocado.",
                        "Dress with olive oil, lemon, salt and pepper."
                ))
                .build();

        Recipe smoothie = Recipe.builder()
                .title("Banana & Spinach Smoothie")
                .description("Smoothie packed with fiber and vitamins.")
                .imageUrl("https://images.pexels.com/photos/4040705/pexels-photo-4040705.jpeg?auto=compress&cs=tinysrgb&w=960&h=720&dpr=1")
                .rating(5)
                .level("Easy")
                .type("Snacks")
                .time("10 min")
                .ingredients(List.of(
                        "1 banana",
                        "1 handful spinach",
                        "1 cup plant-based milk",
                        "1 tbsp oats"
                ))
                .steps(List.of(
                        "Blend all ingredients until smooth.",
                        "Serve cold."
                ))
                .build();

        recipeRepository.saveAll(List.of(waffles, salad, smoothie));
    }
}
