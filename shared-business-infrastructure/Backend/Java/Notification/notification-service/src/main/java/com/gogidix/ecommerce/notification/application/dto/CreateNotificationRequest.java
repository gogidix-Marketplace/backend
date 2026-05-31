package com.gogidix.ecommerce.notification.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateNotificationRequest(
    @NotBlank String name,
    String description,
    String type,
    String recipientId,
    String notificationType,
    String message
) {}
