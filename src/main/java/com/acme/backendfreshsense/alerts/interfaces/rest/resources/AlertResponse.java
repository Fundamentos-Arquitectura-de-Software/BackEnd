package com.acme.backendfreshsense.alerts.interfaces.rest.resources;

public record AlertResponse(
        Long id,
        String title,
        String message,
        String severity,
        String source,
        String state,
        String timeAgo
) {}
