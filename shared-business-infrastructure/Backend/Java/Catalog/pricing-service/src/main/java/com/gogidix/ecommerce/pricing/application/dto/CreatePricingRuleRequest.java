package com.gogidix.ecommerce.pricing.application.dto;

import java.time.Instant;
import java.math.BigDecimal;

public record CreatePricingRuleRequest(
    String name, String ruleType, BigDecimal value, boolean percentage,
    Instant startDate, Instant endDate
) {}
