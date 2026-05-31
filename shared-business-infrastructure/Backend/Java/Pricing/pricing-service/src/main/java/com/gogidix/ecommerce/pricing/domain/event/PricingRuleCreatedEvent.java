package com.gogidix.ecommerce.pricing.domain.event;

import java.time.Instant;

public record PricingRuleCreatedEvent(
    String ruleId,
    String tenantId,
    String ruleCode,
    String name,
    String type,
    Instant timestamp
) {
    public PricingRuleCreatedEvent(String ruleId, String tenantId, String ruleCode, String name, String type) {
        this(ruleId, tenantId, ruleCode, name, type, Instant.now());
    }
}
