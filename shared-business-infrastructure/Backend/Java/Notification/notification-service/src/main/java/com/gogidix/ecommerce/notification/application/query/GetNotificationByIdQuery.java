package com.gogidix.ecommerce.notification.application.query;

public record GetNotificationByIdQuery(
    String tenantId,
    String id
) {}
