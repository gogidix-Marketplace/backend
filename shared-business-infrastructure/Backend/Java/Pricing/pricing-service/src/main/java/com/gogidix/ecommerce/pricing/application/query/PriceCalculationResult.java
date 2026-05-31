package com.gogidix.ecommerce.pricing.application.query;

import java.math.BigDecimal;

public record PriceCalculationResult(
    String productId,
    BigDecimal originalPrice,
    BigDecimal discountedPrice,
    BigDecimal totalDiscount,
    String appliedRuleCode,
    String appliedRuleName,
    String discountType
) {}
