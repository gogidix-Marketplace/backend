package com.gogidix.ecommerce.loyalty.application.command;

public record CreateLoyaltyCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
