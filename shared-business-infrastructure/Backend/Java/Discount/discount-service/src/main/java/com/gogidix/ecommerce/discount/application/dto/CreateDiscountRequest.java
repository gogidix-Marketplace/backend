package com.gogidix.ecommerce.discount.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateDiscountRequest(
    @NotBlank String name,
    String description,
    String type,
    java.math.BigDecimal discountPercentage,
    java.math.BigDecimal discountAmount,
    java.math.BigDecimal minOrderValue
) {}
