package com.acme.backendfreshsense.monitoring.application.dto;

import jakarta.validation.constraints.NotNull;

public record MonitoringReadingRequest(
        @NotNull Double temperature,
        @NotNull Double humidity,
        @NotNull Double ethyleneLevel,
        @NotNull Double oxygenLevel,
        @NotNull Double ripeness,
        @NotNull Double cleanliness
) {}
