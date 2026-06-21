package com.acme.backendfreshsense.monitoring.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Dispositivo IoT registrado. El secretKey solo se devuelve al registrar.")
public record DeviceResponse(
        @Schema(description = "ID interno", example = "1")
        Long id,

        @Schema(description = "Identificador del dispositivo", example = "esp32-freshsense-1")
        String deviceId,

        @Schema(description = "Nombre amigable", example = "Refrigerador cocina")
        String name,

        @Schema(description = "Clave secreta del dispositivo (cabecera X-Device-Key). Solo se muestra al registrar.", example = "a1b2c3...")
        String secretKey,

        @Schema(description = "Fecha de registro")
        LocalDateTime registeredAt
) {}
