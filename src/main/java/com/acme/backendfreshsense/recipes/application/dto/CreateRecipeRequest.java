package com.acme.backendfreshsense.recipes.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "Datos para crear una nueva receta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecipeRequest {

    @Schema(description = "Título de la receta", example = "Smoothie de plátano y leche")
    private String title;

    @Schema(description = "Descripción breve de la receta y su propósito", example = "Ideal para plátanos maduros que no se van a consumir")
    private String description;

    @Schema(description = "URL de imagen representativa de la receta", example = "https://example.com/smoothie.jpg")
    private String image;

    @Schema(description = "Puntuación de la receta (1–5)", example = "5")
    private Integer rating;

    @Schema(description = "Nivel de dificultad de preparación", example = "Easy", allowableValues = {"Easy", "Medium", "Hard"})
    private String level;

    @Schema(description = "Tipo de dieta que sigue la receta", example = "Vegetarian", allowableValues = {"Vegetarian", "Vegan", "Omnivore"})
    private String type;

    @Schema(description = "Tiempo estimado de preparación", example = "5 min")
    private String time;

    @Schema(description = "Lista de ingredientes necesarios", example = "[\"Plátano\", \"Leche\", \"Miel\"]")
    private List<String> ingredients;

    @Schema(description = "Pasos secuenciales de preparación", example = "[\"Pelar plátano\", \"Licuar con leche y miel\", \"Servir frío\"]")
    private List<String> steps;
}
