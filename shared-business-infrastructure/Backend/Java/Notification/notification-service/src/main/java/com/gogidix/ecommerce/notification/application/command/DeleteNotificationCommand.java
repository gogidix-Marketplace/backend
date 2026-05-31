package com.gogidix.ecommerce.notification.application.command;

public record DeleteNotificationCommand(
    String tenantId,
    String id
) {}
