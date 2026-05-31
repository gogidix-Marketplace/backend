package com.gogidix.ecommerce.notification.application.command;

public record UpdateNotificationCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
