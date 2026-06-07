package com.acme.recipesservice.application.service;

import com.acme.recipesservice.application.dto.CreateRecipeRequest;
import com.acme.recipesservice.application.dto.RecipeResponse;
import com.acme.recipesservice.domain.model.Recipe;
import com.acme.recipesservice.domain.repository.RecipeRepository;
import com.acme.recipesservice.infrastructure.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void getAll() {
        // Arrange
        Recipe recipe = Recipe.builder()
                .id(1L)
                .title("Ensalada de Quinua")
                .level("Básico")
                .build();
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));

        // Act
        List<RecipeResponse> responses = recipeService.getAll();

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Ensalada de Quinua", responses.get(0).getTitle());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void getById_Success() {
        // Arrange
        Long recipeId = 1L;
        Recipe recipe = Recipe.builder()
                .id(recipeId)
                .title("Sopa de Verduras")
                .build();
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        // Act
        RecipeResponse response = recipeService.getById(recipeId);

        // Assert
        assertNotNull(response);
        assertEquals("Sopa de Verduras", response.getTitle());
        verify(recipeRepository, times(1)).findById(recipeId);
    }

    @Test
    void getById_ThrowsResourceNotFoundException() {
        // Arrange
        Long recipeId = 99L;
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            recipeService.getById(recipeId);
        });

        // Assert
        assertNotNull(exception);
        verify(recipeRepository, times(1)).findById(recipeId);
    }

    @Test
    void create() {
        // Arrange
        CreateRecipeRequest request = mock(CreateRecipeRequest.class);
        when(request.getTitle()).thenReturn("Lasaña de Berenjena");
        when(request.getDescription()).thenReturn("Receta saludable");
        when(request.getLevel()).thenReturn("Avanzado");

        Recipe savedRecipe = Recipe.builder()
                .id(1L)
                .title("Lasaña de Berenjena")
                .description("Receta saludable")
                .level("Avanzado")
                .build();

        when(recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);

        // Act
        RecipeResponse response = recipeService.create(request);

        // Assert
        assertNotNull(response);
        assertEquals("Lasaña de Berenjena", response.getTitle());
        assertEquals("Avanzado", response.getLevel());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
}