package com.gogidix.ecommerce.warehouse.application.command;

public record CreateWarehouseCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
