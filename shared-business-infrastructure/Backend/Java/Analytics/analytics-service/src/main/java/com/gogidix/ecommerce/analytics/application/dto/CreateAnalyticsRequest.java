package com.gogidix.ecommerce.analytics.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAnalyticsRequest(
    @NotBlank String name,
    String description,
    String type,
    String eventType,
    String metricName,
    java.math.BigDecimal metricValue
) {}
