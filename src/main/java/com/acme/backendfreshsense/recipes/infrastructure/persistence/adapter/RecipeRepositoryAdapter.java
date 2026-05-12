package com.acme.backendfreshsense.recipes.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.recipes.domain.model.aggregates.Recipe;
import com.acme.backendfreshsense.recipes.domain.repository.RecipeRepository;
import com.acme.backendfreshsense.recipes.infrastructure.persistence.jpa.RecipeEntity;
import com.acme.backendfreshsense.recipes.infrastructure.persistence.jpa.RecipeJpaRepository;

import java.util.List;
import java.util.Optional;

public class RecipeRepositoryAdapter implements RecipeRepository {

    private final RecipeJpaRepository jpa;

    public RecipeRepositoryAdapter(RecipeJpaRepository jpa) {
        this.jpa = jpa;
    }

    private static Recipe toDomain(RecipeEntity e) {
        return Recipe.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .imageUrl(e.getImageUrl())
                .rating(e.getRating())
                .level(e.getLevel())
                .type(e.getType())
                .time(e.getTime())
                .ingredients(e.getIngredients())
                .steps(e.getSteps())
                .build();
    }

    private static RecipeEntity toEntity(Recipe r) {
        return RecipeEntity.builder()
                .id(r.getId())
                .title(r.getTitle())
                .description(r.getDescription())
                .imageUrl(r.getImageUrl())
                .rating(r.getRating())
                .level(r.getLevel())
                .type(r.getType())
                .time(r.getTime())
                .ingredients(r.getIngredients())
                .steps(r.getSteps())
                .build();
    }

    @Override
    public List<Recipe> findAll() {
        return jpa.findAll().stream().map(RecipeRepositoryAdapter::toDomain).toList();
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return jpa.findById(id).map(RecipeRepositoryAdapter::toDomain);
    }

    @Override
    public Recipe save(Recipe recipe) {
        return toDomain(jpa.save(toEntity(recipe)));
    }

    @Override
    public boolean hasAny() {
        return jpa.count() > 0;
    }
}
