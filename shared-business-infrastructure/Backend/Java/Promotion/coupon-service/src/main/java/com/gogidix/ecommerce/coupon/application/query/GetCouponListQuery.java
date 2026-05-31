package com.gogidix.ecommerce.coupon.application.query;

public record GetCouponListQuery(
    String tenantId,
    int page,
    int size
) {}
