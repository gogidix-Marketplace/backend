package com.gogidix.ecommerce.inventory.application.query;

public record GetInventoryByIdQuery(
    String tenantId,
    String id
) {}
