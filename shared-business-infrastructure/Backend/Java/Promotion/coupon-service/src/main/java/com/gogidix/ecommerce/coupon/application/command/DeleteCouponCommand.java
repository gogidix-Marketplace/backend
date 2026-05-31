package com.gogidix.ecommerce.coupon.application.command;

public record DeleteCouponCommand(
    String tenantId,
    String id
) {}
