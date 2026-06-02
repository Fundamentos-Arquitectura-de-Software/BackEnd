package com.acme.backendfreshsense.notifications.infrastructure.persistence.jpa;

import jakarta.persistence.*;

@Entity
@Table(name = "notification_preferences")
public class NotificationPreferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private boolean inAppEnabled = true;

    @Column(nullable = false)
    private boolean emailEnabled = false;

    @Column(nullable = false)
    private boolean pushEnabled = false;

    @Column(length = 5)
    private String quietStart;

    @Column(length = 5)
    private String quietEnd;

    protected NotificationPreferenceEntity() {}

    public NotificationPreferenceEntity(Long userId) {
        this.userId = userId;
    }

    public Long getId()            { return id; }
    public Long getUserId()        { return userId; }
    public boolean isInAppEnabled() { return inAppEnabled; }
    public void setInAppEnabled(boolean v)  { this.inAppEnabled = v; }
    public boolean isEmailEnabled() { return emailEnabled; }
    public void setEmailEnabled(boolean v)  { this.emailEnabled = v; }
    public boolean isPushEnabled()  { return pushEnabled; }
    public void setPushEnabled(boolean v)   { this.pushEnabled = v; }
    public String getQuietStart()   { return quietStart; }
    public void setQuietStart(String v)     { this.quietStart = v; }
    public String getQuietEnd()     { return quietEnd; }
    public void setQuietEnd(String v)       { this.quietEnd = v; }
}
