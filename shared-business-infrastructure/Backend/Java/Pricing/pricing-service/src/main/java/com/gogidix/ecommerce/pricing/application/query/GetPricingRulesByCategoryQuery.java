package com.gogidix.ecommerce.pricing.application.query;

public record GetPricingRulesByCategoryQuery(
    String tenantId,
    String categoryId
) {}
