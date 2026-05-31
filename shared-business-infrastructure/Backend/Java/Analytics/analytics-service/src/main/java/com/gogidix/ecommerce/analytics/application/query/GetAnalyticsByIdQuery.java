package com.gogidix.ecommerce.analytics.application.query;

public record GetAnalyticsByIdQuery(
    String tenantId,
    String id
) {}
