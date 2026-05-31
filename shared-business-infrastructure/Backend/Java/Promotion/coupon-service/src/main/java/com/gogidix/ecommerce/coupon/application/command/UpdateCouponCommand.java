package com.gogidix.ecommerce.coupon.application.command;

public record UpdateCouponCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
