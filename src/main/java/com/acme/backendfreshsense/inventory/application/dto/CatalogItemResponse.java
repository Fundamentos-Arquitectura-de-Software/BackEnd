package com.acme.backendfreshsense.inventory.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Alimento del catálogo general, listo para agregarse al inventario")
public record CatalogItemResponse(
        @Schema(description = "Identificador del ítem", example = "1")
        Long id,

        @Schema(description = "Nombre del alimento", example = "Manzana")
        String name,

        @Schema(description = "Descripción corta", example = "Fruta fresca de estación")
        String description,

        @Schema(description = "Categoría de umbrales del Edge", example = "Frutas",
                allowableValues = {"Frutas", "Verduras", "Lácteos", "Carnes", "Proteínas", "Panadería", "Snacks"})
        String category,

        @Schema(description = "URL de imagen referencial", example = "https://images.pexels.com/photos/1510392/pexels-photo-1510392.jpeg")
        String imageUrl
) {}
