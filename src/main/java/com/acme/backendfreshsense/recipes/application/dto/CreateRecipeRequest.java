package com.acme.backendfreshsense.recipes.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 .,-]+$", message = "Solo se aceptan letras, números, y guiones.")
    private String title;

    @Schema(description = "Descripción breve de la receta y su propósito", example = "Ideal para plátanos maduros que no se van a consumir")
    @NotBlank(message = "La descripción es obligatoria.")
    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres.")
    private String description;

    @Schema(description = "URL de imagen representativa de la receta", example = "https://example.com/smoothie.jpg")
    @Pattern(regexp = "^(https?://).+$", message = "La URL debe comenzar con http:// o https://")
    private String image;

    @Schema(description = "Puntuación de la receta (1–5)", example = "5")
    @NotNull(message = "La calificación es obligatoria.")
    private Integer rating;

    @Schema(description = "Nivel de dificultad de preparación", example = "Easy", allowableValues = {"Easy", "Medium", "Hard"})
    @NotBlank(message = "El nivel es obligatorio.")
    private String level;

    @Schema(description = "Tipo de dieta que sigue la receta", example = "Vegetarian", allowableValues = {"Vegetarian", "Vegan", "Omnivore"})
    @NotBlank(message = "El tipo es obligatorio.")
    private String type;

    @Schema(description = "Tiempo estimado de preparación", example = "5 min")
    private String time;

    @Schema(description = "Lista de ingredientes necesarios", example = "[\"Plátano\", \"Leche\", \"Miel\"]")
    @NotEmpty(message = "La receta debe incluir al menos un ingrediente.")
    private List<String> ingredients;

    @Schema(description = "Pasos secuenciales de preparación", example = "[\"Pelar plátano\", \"Licuar con leche y miel\", \"Servir frío\"]")
    @NotEmpty(message = "La receta debe incluir al menos un paso.")
    private List<String> steps;
}
