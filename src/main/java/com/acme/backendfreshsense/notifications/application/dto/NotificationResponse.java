package com.acme.backendfreshsense.notifications.application.dto;

import java.time.Instant;

public class NotificationResponse {

    private Long id;
    private String title;
    private String message;
    private String alertType;
    private boolean read;
    private Instant createdAt;

    public NotificationResponse(Long id, String title, String message, String alertType, boolean read, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.alertType = alertType;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Long getId()          { return id; }
    public String getTitle()     { return title; }
    public String getMessage()   { return message; }
    public String getAlertType() { return alertType; }
    public boolean isRead()      { return read; }
    public Instant getCreatedAt() { return createdAt; }
}
