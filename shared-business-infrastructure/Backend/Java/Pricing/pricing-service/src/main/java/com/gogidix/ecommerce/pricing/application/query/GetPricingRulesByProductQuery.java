package com.gogidix.ecommerce.pricing.application.query;

public record GetPricingRulesByProductQuery(
    String tenantId,
    String productId
) {}
