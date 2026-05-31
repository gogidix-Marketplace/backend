package com.gogidix.ecommerce.pushnotification.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePushNotificationRequest(
    @NotBlank String name,
    String description,
    String type,
    String deviceToken,
    String platform,
    String messageBody
) {}
