package com.gogidix.ecommerce.discount.application.dto;

import java.time.Instant;

public record DiscountResponse(
    String id,
    String name,
    String description,
    String type,
    java.math.BigDecimal discountPercentage,
    java.math.BigDecimal discountAmount,
    java.math.BigDecimal minOrderValue,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
