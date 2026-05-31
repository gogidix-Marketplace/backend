package com.gogidix.ecommerce.pricing.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CreatePricingRuleCommand(
    @NotBlank String tenantId,
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
