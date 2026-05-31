package com.gogidix.ecommerce.loyalty.application.command;

public record UpdateLoyaltyCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
