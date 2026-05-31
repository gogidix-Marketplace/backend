package com.gogidix.ecommerce.inventory.application.query;

public record GetInventoryListQuery(
    String tenantId,
    int page,
    int size
) {}
