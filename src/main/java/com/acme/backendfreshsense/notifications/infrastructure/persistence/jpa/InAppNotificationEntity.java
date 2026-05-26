package com.acme.backendfreshsense.notifications.infrastructure.persistence.jpa;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "in_app_notifications")
public class InAppNotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(length = 50)
    private String alertType;

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    protected InAppNotificationEntity() {}

    public InAppNotificationEntity(Long userId, String title, String message, String alertType) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.alertType = alertType;
    }

    public Long getId()           { return id; }
    public Long getUserId()       { return userId; }
    public String getTitle()      { return title; }
    public String getMessage()    { return message; }
    public String getAlertType()  { return alertType; }
    public boolean isRead()       { return read; }
    public void setRead(boolean r) { this.read = r; }
    public Instant getCreatedAt() { return createdAt; }
}
