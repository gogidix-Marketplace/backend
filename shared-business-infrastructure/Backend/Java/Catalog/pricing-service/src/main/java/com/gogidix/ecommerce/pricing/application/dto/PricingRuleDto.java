package com.gogidix.ecommerce.pricing.application.dto;

import java.time.Instant;
import java.math.BigDecimal;

public record PricingRuleDto(
    String id, String tenantId, String name, String ruleType,
    BigDecimal value, boolean percentage, boolean active,
    Instant startDate, Instant endDate, Instant createdAt, Instant updatedAt
) {}
