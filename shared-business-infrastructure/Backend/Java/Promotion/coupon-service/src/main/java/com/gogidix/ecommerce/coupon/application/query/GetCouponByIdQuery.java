package com.gogidix.ecommerce.coupon.application.query;

public record GetCouponByIdQuery(
    String tenantId,
    String id
) {}
