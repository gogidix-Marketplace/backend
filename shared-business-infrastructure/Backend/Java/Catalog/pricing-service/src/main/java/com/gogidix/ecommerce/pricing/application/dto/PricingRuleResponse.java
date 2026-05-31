package com.gogidix.ecommerce.pricing.application.dto;

import java.time.Instant;
import java.math.BigDecimal;

public record PricingRuleResponse(
    String id, String tenantId, String name, String ruleType,
    BigDecimal value, boolean percentage, boolean active,
    Instant startDate, Instant endDate, Instant createdAt, Instant updatedAt
) {
    public static PricingRuleResponse from(PricingRuleDto dto) {
        return new PricingRuleResponse(dto.id(), dto.tenantId(), dto.name(), dto.ruleType(),
            dto.value(), dto.percentage(), dto.active(), dto.startDate(), dto.endDate(), dto.createdAt(), dto.updatedAt());
    }
}
