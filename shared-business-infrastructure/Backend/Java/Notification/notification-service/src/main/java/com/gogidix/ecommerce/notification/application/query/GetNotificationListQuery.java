package com.gogidix.ecommerce.notification.application.query;

public record GetNotificationListQuery(
    String tenantId,
    int page,
    int size
) {}
