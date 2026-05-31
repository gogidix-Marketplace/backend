package com.gogidix.ecommerce.warehouse.application.command;

public record UpdateWarehouseCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
