package com.acme.recipesservice.infrastructure.persistence.config;

import com.acme.recipesservice.application.service.RecipeService;
import com.acme.recipesservice.domain.repository.RecipeRepository;
import com.acme.recipesservice.infrastructure.persistence.adapter.RecipeRepositoryAdapter;
import com.acme.recipesservice.infrastructure.persistence.jpa.RecipeJpaRepository;
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
