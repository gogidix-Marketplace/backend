package com.gogidix.ecommerce.coupon.application.dto;

public record UpdateCouponRequest(
    String name,
    String description,
    String type,
    String couponCode,
    java.math.BigDecimal discountPercentage,
    Integer maxUses,
    Boolean isActive
) {}
