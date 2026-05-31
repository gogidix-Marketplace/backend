package com.gogidix.ecommerce.inventory.application.command;

public record DeleteInventoryCommand(
    String tenantId,
    String id
) {}
