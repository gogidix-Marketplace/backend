package com.gogidix.ecommerce.promotion.application.query;

public record GetPromotionListQuery(
    String tenantId,
    int page,
    int size
) {}
