package com.gogidix.ecommerce.discount.application.query;

public record GetDiscountByIdQuery(
    String tenantId,
    String id
) {}
