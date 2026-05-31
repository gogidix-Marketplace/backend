package com.gogidix.ecommerce.promotion.application.query;

public record GetPromotionByIdQuery(
    String tenantId,
    String id
) {}
