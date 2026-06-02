package com.acme.backendfreshsense.inventory.application.dto;


import jakarta.validation.constraints.*;

public record ProductRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        @Pattern(
                regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 .,-]+$",
                message = "Solo se aceptan letras, números, y guiones."
        )
        String name,

        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres.")
        String description,

        @NotBlank(message = "La categoría es obligatoria")
        @Size(max = 50, message = "La categoría no puede superar los 50 caracteres.")
        @Pattern(
                regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 .,-]+$",
                message = "Solo se aceptan letras, números, y guiones."
        )
        String category,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 0, message = "La cantidad no puede ser negativa")
        Integer quantity,

        @Pattern(
                regexp = "^(https?://).+$",
                message = "La URL debe comenzar con http:// o https://"
        )
        String imageUrl
) {}
