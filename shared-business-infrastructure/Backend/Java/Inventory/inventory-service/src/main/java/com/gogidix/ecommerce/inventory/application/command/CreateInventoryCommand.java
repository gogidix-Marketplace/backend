package com.gogidix.ecommerce.inventory.application.command;

public record CreateInventoryCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
