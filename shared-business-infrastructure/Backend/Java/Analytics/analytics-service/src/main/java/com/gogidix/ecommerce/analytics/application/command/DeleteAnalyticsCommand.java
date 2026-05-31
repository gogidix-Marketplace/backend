package com.gogidix.ecommerce.analytics.application.command;

public record DeleteAnalyticsCommand(
    String tenantId,
    String id
) {}
