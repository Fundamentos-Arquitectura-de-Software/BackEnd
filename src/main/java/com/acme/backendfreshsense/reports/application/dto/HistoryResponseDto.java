package com.acme.backendfreshsense.reports.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Entrada del historial de acciones sobre el inventario")
public record HistoryResponseDto(
        @Schema(description = "Identificador único de la entrada en el historial", example = "1")
        Long id,

        @Schema(description = "ID del producto sobre el que se realizó la acción", example = "3")
        Long productId,

        @Schema(description = "Nombre del producto", example = "Leche entera")
        String productName,

        @Schema(description = "Categoría del producto", example = "Lácteos")
        String category,

        @Schema(description = "Acción realizada: CONSUMED o DISCARDED", example = "CONSUMED", allowableValues = {"CONSUMED", "DISCARDED"})
        String action,

        @Schema(description = "Cantidad de unidades afectadas", example = "1")
        Integer quantity,

        @Schema(description = "Fecha y hora en que se registró la acción (ISO 8601)", example = "2026-05-30T08:15:00")
        LocalDateTime date
) { }
