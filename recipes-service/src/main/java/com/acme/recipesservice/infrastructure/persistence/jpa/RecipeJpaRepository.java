package com.acme.recipesservice.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeJpaRepository extends JpaRepository<RecipeEntity, Long> {
    /** Recetas visibles para un usuario: las base (user_id null) + las suyas. */
    List<RecipeEntity> findByUserIdIsNullOrUserId(Long userId);
    long countByUserId(Long userId);
}
