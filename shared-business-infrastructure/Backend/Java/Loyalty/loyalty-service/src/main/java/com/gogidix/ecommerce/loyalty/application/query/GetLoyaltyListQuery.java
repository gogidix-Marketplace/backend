package com.gogidix.ecommerce.loyalty.application.query;

public record GetLoyaltyListQuery(
    String tenantId,
    int page,
    int size
) {}
