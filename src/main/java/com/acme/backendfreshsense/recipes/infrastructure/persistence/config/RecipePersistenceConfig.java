package com.acme.backendfreshsense.recipes.infrastructure.persistence.config;

import com.acme.backendfreshsense.recipes.application.service.RecipeService;
import com.acme.backendfreshsense.recipes.domain.repository.RecipeRepository;
import com.acme.backendfreshsense.recipes.infrastructure.persistence.adapter.RecipeRepositoryAdapter;
import com.acme.backendfreshsense.recipes.infrastructure.persistence.jpa.RecipeJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecipePersistenceConfig {

    @Bean
    public RecipeRepository recipeRepository(RecipeJpaRepository jpa) {
        return new RecipeRepositoryAdapter(jpa);
    }

    @Bean
    public RecipeService recipeService(RecipeRepository repo) {
        return new RecipeService(repo);
    }
}
