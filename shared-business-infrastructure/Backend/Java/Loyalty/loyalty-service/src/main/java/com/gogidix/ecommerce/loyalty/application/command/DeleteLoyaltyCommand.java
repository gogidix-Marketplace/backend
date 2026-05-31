package com.gogidix.ecommerce.loyalty.application.command;

public record DeleteLoyaltyCommand(
    String tenantId,
    String id
) {}
