package com.gogidix.ecommerce.discount.application.command;

public record CreateDiscountCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
