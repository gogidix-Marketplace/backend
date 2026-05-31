package com.gogidix.ecommerce.warehouse.application.query;

public record GetWarehouseByIdQuery(
    String tenantId,
    String id
) {}
