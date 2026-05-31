package com.gogidix.ecommerce.notification.application.dto;

public record UpdateNotificationRequest(
    String name,
    String description,
    String type,
    String recipientId,
    String notificationType,
    String message,
    Boolean isActive
) {}
