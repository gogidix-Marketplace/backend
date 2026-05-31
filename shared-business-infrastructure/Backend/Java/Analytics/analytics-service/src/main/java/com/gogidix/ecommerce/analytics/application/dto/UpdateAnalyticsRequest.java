package com.gogidix.ecommerce.analytics.application.dto;

public record UpdateAnalyticsRequest(
    String name,
    String description,
    String type,
    String eventType,
    String metricName,
    java.math.BigDecimal metricValue,
    Boolean isActive
) {}
