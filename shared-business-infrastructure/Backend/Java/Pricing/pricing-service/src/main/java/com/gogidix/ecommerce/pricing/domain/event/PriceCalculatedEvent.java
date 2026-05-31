package com.gogidix.ecommerce.pricing.domain.event;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceCalculatedEvent(
    String tenantId,
    String productId,
    BigDecimal originalPrice,
    BigDecimal finalPrice,
    String appliedRuleCode,
    Instant timestamp
) {
    public PriceCalculatedEvent(String tenantId, String productId, BigDecimal originalPrice, BigDecimal finalPrice, String appliedRuleCode) {
        this(tenantId, productId, originalPrice, finalPrice, appliedRuleCode, Instant.now());
    }
}
