package com.acme.backendfreshsense.monitoring.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para registrar un dispositivo IoT del usuario")
public record RegisterDeviceRequest(
        @Schema(description = "Identificador único del dispositivo (impreso/configurado en el ESP32)", example = "esp32-freshsense-1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(max = 100)
        String deviceId,

        @Schema(description = "Nombre amigable del dispositivo", example = "Refrigerador cocina")
        @Size(max = 100)
        String name
) {}
