package com.acme.backendfreshsense.recipes.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecipeRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100)
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 .,-]+$",
            message = "Solo se aceptan letras, números, y guiones."
    )
    private String title;

    @NotBlank(message = "La descripción es obligatoria.")
    @Size(max = 1000, message = "El título no puede superar los 1000 caracteres.")
    private String description;

    @Pattern(
            regexp = "^(https?://).+$",
            message = "La URL debe comenzar con http:// o https://"
    )
    private String image;

    @NotNull(message = "La calificación es obligatoria.")
    private Integer rating;

    @NotBlank(message = "El novel es obligatorio.")
    private String level;

    @NotBlank(message = "El tipo es obligatorio.")
    private String type;

    private String time;

    @NotEmpty(message = "La receta de de incluir al menos un ingrediente.")
    private List<String> ingredients;

    @NotEmpty(message = "La receta debe de incluir al menos un paso.")
    private List<String> steps;
}
