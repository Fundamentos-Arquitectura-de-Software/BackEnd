package com.acme.recipesservice.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeJpaRepository extends JpaRepository<RecipeEntity, Long> {
}
