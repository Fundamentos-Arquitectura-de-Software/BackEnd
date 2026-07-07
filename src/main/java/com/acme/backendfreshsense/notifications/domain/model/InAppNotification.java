package com.acme.backendfreshsense.notifications.domain.model;

import java.time.Instant;

public class InAppNotification {

    private Long id;
    private Long userId;
    private String title;
    private String message;
    private String alertType;
    private boolean read;
    private Instant createdAt;

    public InAppNotification() {}

    public InAppNotification(Long userId, String title, String message, String alertType) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.alertType = alertType;
        this.read = false;
        this.createdAt = Instant.now();
    }

    public Long getId()           { return id; }
    public void setId(Long id)    { this.id = id; }
    public Long getUserId()       { return userId; }
    public String getTitle()      { return title; }
    public String getMessage()    { return message; }
    public String getAlertType()  { return alertType; }
    public boolean isRead()       { return read; }
    public void setRead(boolean read) { this.read = read; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
