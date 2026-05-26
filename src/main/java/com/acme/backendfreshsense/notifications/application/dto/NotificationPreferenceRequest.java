package com.acme.backendfreshsense.notifications.application.dto;

public class NotificationPreferenceRequest {

    private boolean inAppEnabled = true;
    private boolean emailEnabled = false;
    private boolean pushEnabled = false;
    private String quietStart;
    private String quietEnd;

    public NotificationPreferenceRequest() {}

    public boolean isInAppEnabled()  { return inAppEnabled; }
    public boolean isEmailEnabled()  { return emailEnabled; }
    public boolean isPushEnabled()   { return pushEnabled; }
    public String getQuietStart()    { return quietStart; }
    public String getQuietEnd()      { return quietEnd; }
}
