package com.acme.backendfreshsense.inventory.interfaces.rest.resources;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotBlank(message = "El nombre es obligatorio") String name,
        String description,
        @NotBlank(message = "La categoría es obligatoria") String category,
        @NotNull(message = "La cantidad es obligatoria") @Min(value = 0, message = "La cantidad no puede ser negativa") Integer quantity,
        String imageUrl
) {}
