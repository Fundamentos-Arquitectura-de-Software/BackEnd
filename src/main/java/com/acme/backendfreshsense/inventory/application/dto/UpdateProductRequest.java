package com.acme.backendfreshsense.inventory.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

@Schema(description = "Datos para actualizar parcialmente un producto del inventario. Solo se aplican los campos presentes (no nulos).")
public record UpdateProductRequest(
        @Schema(description = "Nuevo nombre del producto", example = "Manzana Fuji")
        String name,

        @Schema(description = "Nueva descripción", example = "Manzanas importadas")
        String description,

        @Schema(description = "Nueva categoría", example = "Frutas")
        String category,

        @Schema(description = "Nueva cantidad del producto en inventario (≥ 0)", example = "5")
        @Min(value = 0, message = "La cantidad no puede ser negativa")
        Integer quantity,

        @Schema(description = "Nueva URL de imagen", example = "https://example.com/manzana.jpg")
        String imageUrl,

        @Schema(description = "Nueva fecha de vencimiento (yyyy-MM-dd)", example = "2026-07-20")
        LocalDate expirationDate
) {}
