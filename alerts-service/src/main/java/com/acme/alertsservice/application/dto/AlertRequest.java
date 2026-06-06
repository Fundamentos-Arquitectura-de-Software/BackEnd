package com.acme.alertsservice.application.dto;

public record AlertRequest(
        String title,
        String message,
        String severity,
        String source,
        String state,
        String timeAgo
) {}
