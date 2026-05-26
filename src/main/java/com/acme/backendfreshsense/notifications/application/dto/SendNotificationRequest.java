package com.acme.backendfreshsense.notifications.application.dto;

import com.acme.backendfreshsense.notifications.domain.model.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SendNotificationRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String title;

    @NotBlank
    private String message;

    private String alertType;

    private ChannelType channelType; // null = todos los canales activos del usuario

    public SendNotificationRequest() {}

    public Long getUserId()         { return userId; }
    public String getTitle()        { return title; }
    public String getMessage()      { return message; }
    public String getAlertType()    { return alertType; }
    public ChannelType getChannelType() { return channelType; }
}
