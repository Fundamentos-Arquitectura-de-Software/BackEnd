package com.acme.backendfreshsense.notifications.domain.model;

public class NotificationPreference {

    private Long id;
    private Long userId;
    private boolean inAppEnabled;
    private boolean emailEnabled;
    private boolean pushEnabled;
    private String quietStart; // HH:mm, e.g. "22:00"
    private String quietEnd;   // HH:mm, e.g. "08:00"

    public NotificationPreference() {}

    public NotificationPreference(Long userId) {
        this.userId = userId;
        this.inAppEnabled = true;
        this.emailEnabled = false;
        this.pushEnabled = false;
    }

    public Long getId()           { return id; }
    public void setId(Long id)    { this.id = id; }
    public Long getUserId()       { return userId; }
    public boolean isInAppEnabled()  { return inAppEnabled; }
    public void setInAppEnabled(boolean v)  { this.inAppEnabled = v; }
    public boolean isEmailEnabled()  { return emailEnabled; }
    public void setEmailEnabled(boolean v)  { this.emailEnabled = v; }
    public boolean isPushEnabled()   { return pushEnabled; }
    public void setPushEnabled(boolean v)   { this.pushEnabled = v; }
    public String getQuietStart()    { return quietStart; }
    public void setQuietStart(String v)     { this.quietStart = v; }
    public String getQuietEnd()      { return quietEnd; }
    public void setQuietEnd(String v)       { this.quietEnd = v; }
}
