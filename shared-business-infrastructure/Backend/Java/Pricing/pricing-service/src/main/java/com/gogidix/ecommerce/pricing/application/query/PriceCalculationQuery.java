package com.gogidix.ecommerce.pricing.application.query;

import java.math.BigDecimal;

public record PriceCalculationQuery(
    String tenantId,
    String productId,
    String categoryId,
    String customerTier,
    String channel,
    String region,
    BigDecimal orderValue,
    Integer quantity
) {}
