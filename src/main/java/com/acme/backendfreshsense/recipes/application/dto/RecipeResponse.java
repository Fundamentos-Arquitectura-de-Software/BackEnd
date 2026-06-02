package com.acme.backendfreshsense.recipes.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "Datos de una receta del catálogo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResponse {

    @Schema(description = "Identificador único de la receta", example = "1")
    private Long id;

    @Schema(description = "Título de la receta", example = "Ensalada de tomate y pepino")
    private String title;

    @Schema(description = "Descripción breve de la receta", example = "Receta fresca para aprovechar verduras próximas a vencer")
    private String description;

    @Schema(description = "URL de imagen de la receta", example = "https://example.com/ensalada.jpg")
    private String image;

    @Schema(description = "Puntuación de la receta (1–5)", example = "4")
    private Integer rating;

    @Schema(description = "Nivel de dificultad", example = "Easy", allowableValues = {"Easy", "Medium", "Hard"})
    private String level;

    @Schema(description = "Tipo de dieta", example = "Vegan", allowableValues = {"Vegetarian", "Vegan", "Omnivore"})
    private String type;

    @Schema(description = "Tiempo estimado de preparación", example = "10 min")
    private String time;

    @Schema(description = "Lista de ingredientes", example = "[\"Tomate\", \"Pepino\", \"Aceite de oliva\", \"Sal\"]")
    private List<String> ingredients;

    @Schema(description = "Pasos de preparación ordenados", example = "[\"Cortar tomate y pepino\", \"Mezclar con aceite y sal\", \"Servir frío\"]")
    private List<String> steps;
}
