package com.gogidix.ecommerce.analytics.application.command;

public record UpdateAnalyticsCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
