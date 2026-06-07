package com.acme.recipesservice.infrastructure.web;

import com.acme.recipesservice.application.dto.RecipeResponse;
import com.acme.recipesservice.application.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipesController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecipeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @Test
    void getAll_ReturnsOkAndRecipeList_WhenRecipesExist() throws Exception {
        // Arrange
        RecipeResponse recipe = RecipeResponse.builder()
                .id(1L)
                .title("Ensalada Fresh")
                .description("Mezclar vegetales e incorporar el aderezo")
                .image("https://image.com/ensalada.png")
                .rating(5)
                .level("Fácil")
                .type("Saludable")
                .time("15 min")
                .ingredients(List.of("Lechuga", "Tomate"))
                .steps(List.of("Lavar verduras", "Mezclar todo"))
                .build();

        when(recipeService.getAll()).thenReturn(List.of(recipe));

        // Act
        mockMvc.perform(get("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Ensalada Fresh"));
    }
}