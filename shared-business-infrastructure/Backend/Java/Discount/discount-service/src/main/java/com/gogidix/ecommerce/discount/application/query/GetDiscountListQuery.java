package com.gogidix.ecommerce.discount.application.query;

public record GetDiscountListQuery(
    String tenantId,
    int page,
    int size
) {}
