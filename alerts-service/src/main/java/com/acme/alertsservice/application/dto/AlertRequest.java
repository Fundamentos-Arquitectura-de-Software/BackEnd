package com.acme.alertsservice.application.dto;

public record AlertRequest(
        Long userId,
        String title,
        String message,
        String severity,
        String source,
        String state,
        String timeAgo
) {}
