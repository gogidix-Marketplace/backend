package com.gogidix.ecommerce.notification.application.dto;

import java.time.Instant;

public record NotificationResponse(
    String id,
    String name,
    String description,
    String type,
    String recipientId,
    String notificationType,
    String message,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
