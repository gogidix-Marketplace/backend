package com.gogidix.ecommerce.loyalty.application.query;

public record GetLoyaltyByIdQuery(
    String tenantId,
    String id
) {}
