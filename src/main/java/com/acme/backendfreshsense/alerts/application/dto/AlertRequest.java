package com.acme.backendfreshsense.alerts.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlertRequest(
        @NotBlank
        @Size(max = 100, message = "El título no puede superar los 100 caracteres.")
        String title,

        @NotBlank
        @Size(max = 500, message = "El mensaje no puede superar los 500 caracteres.")
        String message,

        @NotBlank
        @Size(max = 30)
        String severity,

        @NotBlank
        @Size(max = 100, message = "La fuente no puede superar los 100 caracteres.")
        String source,

        @NotBlank
        @Size(max = 30)
        String state,

        @NotBlank
        @Size(max = 50)
        String timeAgo
) {}
