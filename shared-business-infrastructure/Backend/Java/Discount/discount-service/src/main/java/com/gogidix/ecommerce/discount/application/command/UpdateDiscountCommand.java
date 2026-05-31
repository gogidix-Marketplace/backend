package com.gogidix.ecommerce.discount.application.command;

public record UpdateDiscountCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
