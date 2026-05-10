package com.acme.backendfreshsense.reports.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HistoryCreateRequestDto(
        @NotNull(message = "El productId es obligatorio") Long productId,
        @NotBlank(message = "El nombre del producto es obligatorio") String productName,
        @NotBlank(message = "La categoría es obligatoria") String category,
        @NotBlank(message = "La acción es obligatoria") String action,
        @NotNull(message = "La cantidad es obligatoria") @Min(value = 0, message = "La cantidad no puede ser negativa") Integer quantity
) {}
