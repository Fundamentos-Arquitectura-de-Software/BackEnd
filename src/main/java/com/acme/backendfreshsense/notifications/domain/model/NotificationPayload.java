package com.acme.backendfreshsense.notifications.domain.model;

public class NotificationPayload {

    private Long userId;
    private String title;
    private String message;
    private String alertType;
    private ChannelType channelType;

    public NotificationPayload(Long userId, String title, String message, String alertType, ChannelType channelType) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.alertType = alertType;
        this.channelType = channelType;
    }

    public Long getUserId()       { return userId; }
    public String getTitle()      { return title; }
    public String getMessage()    { return message; }
    public String getAlertType()  { return alertType; }
    public ChannelType getChannelType() { return channelType; }
}
