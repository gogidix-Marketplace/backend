package com.gogidix.ecommerce.pushnotification.application.dto;

import java.time.LocalDateTime;

public record PushNotificationResponse(
    String id,
    String name,
    String description,
    String type,
    boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String deviceToken, String platform, String title, String payload
) {
}