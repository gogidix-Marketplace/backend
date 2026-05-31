package com.gogidix.ecommerce.inventory.application.command;

public record UpdateInventoryCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
