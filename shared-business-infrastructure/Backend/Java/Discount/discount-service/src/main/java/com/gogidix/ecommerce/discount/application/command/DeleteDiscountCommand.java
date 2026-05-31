package com.gogidix.ecommerce.discount.application.command;

public record DeleteDiscountCommand(
    String tenantId,
    String id
) {}
