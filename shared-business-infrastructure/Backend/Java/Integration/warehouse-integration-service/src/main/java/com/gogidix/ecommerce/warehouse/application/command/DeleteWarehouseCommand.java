package com.gogidix.ecommerce.warehouse.application.command;

public record DeleteWarehouseCommand(
    String tenantId,
    String id
) {}
