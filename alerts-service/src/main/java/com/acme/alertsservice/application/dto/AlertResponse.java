package com.acme.alertsservice.application.dto;

public record AlertResponse(
        Long id,
        String title,
        String message,
        String severity,
        String source,
        String state,
        String timeAgo
) {}
