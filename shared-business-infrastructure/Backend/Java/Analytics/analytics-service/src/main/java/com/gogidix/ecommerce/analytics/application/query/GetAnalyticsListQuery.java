package com.gogidix.ecommerce.analytics.application.query;

public record GetAnalyticsListQuery(
    String tenantId,
    int page,
    int size
) {}
