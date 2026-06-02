package com.acme.backendfreshsense.reports.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para registrar una acción sobre un producto en el historial")
public record HistoryCreateRequestDto(
        @Schema(description = "ID del producto sobre el que se realizó la acción", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El productId es obligatorio") Long productId,

        @Schema(description = "Nombre del producto", example = "Leche entera", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El nombre del producto es obligatorio") String productName,

        @Schema(description = "Categoría del producto", example = "Lácteos", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "La categoría es obligatoria") String category,

        @Schema(description = "Acción realizada sobre el producto: CONSUMED (consumido) o DISCARDED (descartado/desperdiciado)", example = "CONSUMED", allowableValues = {"CONSUMED", "DISCARDED"}, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "La acción es obligatoria") String action,

        @Schema(description = "Cantidad de unidades afectadas (≥ 0)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "La cantidad es obligatoria") @Min(value = 0, message = "La cantidad no puede ser negativa") Integer quantity
) {}
