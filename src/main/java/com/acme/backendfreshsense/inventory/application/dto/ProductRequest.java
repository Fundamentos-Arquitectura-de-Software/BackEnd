package com.acme.backendfreshsense.inventory.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Datos para registrar un producto en el inventario")
public record ProductRequest(
        @Schema(description = "Nombre del producto", example = "Manzana Fuji", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 .,-]+$", message = "Solo se aceptan letras, números, y guiones.")
        String name,

        @Schema(description = "Descripción adicional del producto (opcional)", example = "Manzanas importadas de Chile", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres.")
        String description,

        @Schema(description = "Categoría del producto", example = "Frutas", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "La categoría es obligatoria")
        @Size(max = 50, message = "La categoría no puede superar los 50 caracteres.")
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 .,-]+$", message = "Solo se aceptan letras, números, y guiones.")
        String category,

        @Schema(description = "Cantidad disponible en inventario (≥ 0)", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 0, message = "La cantidad no puede ser negativa")
        Integer quantity,

        @Schema(description = "URL de imagen del producto (opcional)", example = "https://example.com/manzana.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @Pattern(regexp = "^(https?://).+$", message = "La URL debe comenzar con http:// o https://")
        String imageUrl,

        @Schema(description = "Fecha de vencimiento del producto (yyyy-MM-dd)", example = "2026-07-20", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "La fecha de vencimiento es obligatoria")
        LocalDate expirationDate
) {}
