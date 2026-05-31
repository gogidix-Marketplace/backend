package com.gogidix.ecommerce.pricing.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PricingRuleResponse(
    String id,
    String ruleCode,
    String name,
    String description,
    String type,
    String strategy,
    BigDecimal basePrice,
    BigDecimal salePrice,
    BigDecimal costPrice,
    BigDecimal minimumPrice,
    BigDecimal maximumPrice,
    BigDecimal discountPercentage,
    BigDecimal discountAmount,
    String currency,
    String productId,
    String categoryId,
    List<String> applicableProductIds,
    List<String> applicableCategoryIds,
    Instant effectiveFrom,
    Instant effectiveTo,
    Boolean isActive,
    Integer priority,
    Instant createdAt,
    Instant updatedAt
) {}
