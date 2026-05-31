package com.gogidix.ecommerce.analytics.application.dto;

import java.time.Instant;

public record AnalyticsResponse(
    String id,
    String name,
    String description,
    String type,
    String eventType,
    String metricName,
    java.math.BigDecimal metricValue,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
