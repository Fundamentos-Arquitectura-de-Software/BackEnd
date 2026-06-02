package com.acme.backendfreshsense.inventory.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para registrar un producto en el inventario")
public record ProductRequest(
        @Schema(description = "Nombre del producto", example = "Manzana Fuji", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El nombre es obligatorio") String name,

        @Schema(description = "Descripción adicional del producto (opcional)", example = "Manzanas importadas de Chile", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String description,

        @Schema(description = "Categoría del producto", example = "Frutas", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "La categoría es obligatoria") String category,

        @Schema(description = "Cantidad disponible en inventario (≥ 0)", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "La cantidad es obligatoria") @Min(value = 0, message = "La cantidad no puede ser negativa") Integer quantity,

        @Schema(description = "URL de imagen del producto (opcional)", example = "https://example.com/manzana.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String imageUrl
) {}
