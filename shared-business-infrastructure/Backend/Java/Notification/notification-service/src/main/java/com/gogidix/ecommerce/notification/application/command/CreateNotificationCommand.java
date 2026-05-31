package com.gogidix.ecommerce.notification.application.command;

public record CreateNotificationCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
