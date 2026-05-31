package com.gogidix.ecommerce.pushnotification.application.dto;

import jakarta.validation.constraints.NotBlank;

public class CreatePushNotificationRequest {

    @NotBlank
    private String name;

    private String description;

    private String type;

    private String deviceToken;
    private String platform;
    private String title;
    private String payload;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDeviceToken() { return deviceToken; }
    public void setDeviceToken(String deviceToken) { this.deviceToken = deviceToken; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

}