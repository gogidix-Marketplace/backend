package com.gogidix.ecommerce.pushnotification.application.command;

public record DeletePushNotificationCommand(
    String tenantId,
    String id
) {}
