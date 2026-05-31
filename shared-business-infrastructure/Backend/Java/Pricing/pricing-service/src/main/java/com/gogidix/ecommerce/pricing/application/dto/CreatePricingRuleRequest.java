package com.gogidix.ecommerce.pricing.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePricingRuleRequest(
    @NotBlank String ruleCode,
    @NotBlank String name,
    String description,
    @NotNull String type,
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
    Integer priority
) {}
