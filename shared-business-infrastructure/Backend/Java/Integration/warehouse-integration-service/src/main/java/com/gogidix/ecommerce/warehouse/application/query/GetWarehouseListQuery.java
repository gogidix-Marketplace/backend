package com.gogidix.ecommerce.warehouse.application.query;

public record GetWarehouseListQuery(
    String tenantId,
    int page,
    int size
) {}
