package com.gogidix.ecommerce.analytics.application.command;

public record CreateAnalyticsCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
