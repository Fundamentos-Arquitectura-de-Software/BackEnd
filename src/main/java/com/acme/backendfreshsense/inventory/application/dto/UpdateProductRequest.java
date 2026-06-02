package com.acme.backendfreshsense.inventory.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "Datos para actualizar parcialmente un producto del inventario")
public record UpdateProductRequest(
        @Schema(description = "Nueva cantidad del producto en inventario (≥ 0)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(value = 0, message = "La cantidad no puede ser negativa") Integer quantity
) {}
