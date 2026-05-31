package com.gogidix.ecommerce.pushnotification.application.dto;

public record UpdatePushNotificationRequest(
    String name,
    String description,
    String type,
    String deviceToken,
    String platform,
    String messageBody,
    Boolean isActive
) {}
