package com.gogidix.ecommerce.pricing.domain.event;

import java.time.Instant;

public record PricingRuleDeletedEvent(
    String ruleId,
    String tenantId,
    String ruleCode,
    Instant timestamp
) {
    public PricingRuleDeletedEvent(String ruleId, String tenantId, String ruleCode) {
        this(ruleId, tenantId, ruleCode, Instant.now());
    }
}
