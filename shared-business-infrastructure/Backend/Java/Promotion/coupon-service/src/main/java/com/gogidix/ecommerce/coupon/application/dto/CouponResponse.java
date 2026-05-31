package com.gogidix.ecommerce.coupon.application.dto;

import java.time.Instant;

public record CouponResponse(
    String id,
    String name,
    String description,
    String type,
    String couponCode,
    java.math.BigDecimal discountPercentage,
    Integer maxUses,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
