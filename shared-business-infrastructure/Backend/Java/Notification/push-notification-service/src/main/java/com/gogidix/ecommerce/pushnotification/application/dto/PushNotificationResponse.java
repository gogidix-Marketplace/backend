package com.gogidix.ecommerce.pushnotification.application.dto;

import java.time.Instant;

public record PushNotificationResponse(
    String id,
    String name,
    String description,
    String type,
    String deviceToken,
    String platform,
    String messageBody,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
