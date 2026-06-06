package com.acme.backendfreshsense.inventory.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

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
        String imageUrl
) {}
