package com.gogidix.ecommerce.coupon.application.command;

public record CreateCouponCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
