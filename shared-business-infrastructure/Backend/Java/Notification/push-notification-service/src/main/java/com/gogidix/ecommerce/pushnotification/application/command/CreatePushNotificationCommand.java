package com.gogidix.ecommerce.pushnotification.application.command;

public record CreatePushNotificationCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
