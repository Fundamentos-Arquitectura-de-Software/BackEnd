package com.acme.backendfreshsense.inventory.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Datos de un producto del inventario")
public record ProductResponse(
        @Schema(description = "Identificador único del producto", example = "5")
        Long id,

        @Schema(description = "Nombre del producto", example = "Manzana Fuji")
        String name,

        @Schema(description = "Descripción adicional del producto", example = "Manzanas importadas de Chile")
        String description,

        @Schema(description = "Categoría del producto", example = "Frutas")
        String category,

        @Schema(description = "Cantidad disponible en inventario", example = "12")
        Integer quantity,

        @Schema(description = "URL de imagen del producto", example = "https://example.com/manzana.jpg")
        String imageUrl,

        @Schema(description = "Fecha de vencimiento del producto (null en productos antiguos)", example = "2026-07-20")
        LocalDate expirationDate,

        @Schema(description = "Fecha y hora en que se agregó el producto al sistema", example = "2026-07-07T18:30:00")
        LocalDateTime createdAt
) {}
