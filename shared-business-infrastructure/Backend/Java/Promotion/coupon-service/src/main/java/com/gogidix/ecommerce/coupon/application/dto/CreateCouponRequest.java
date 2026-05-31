package com.gogidix.ecommerce.coupon.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCouponRequest(
    @NotBlank String name,
    String description,
    String type,
    String couponCode,
    java.math.BigDecimal discountPercentage,
    Integer maxUses
) {}
