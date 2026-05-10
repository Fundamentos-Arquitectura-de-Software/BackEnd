package com.acme.backendfreshsense.alerts.interfaces.rest.resources;

public record AlertRequest(
        String title,
        String message,
        String severity,
        String source,
        String state,
        String timeAgo
) {}
