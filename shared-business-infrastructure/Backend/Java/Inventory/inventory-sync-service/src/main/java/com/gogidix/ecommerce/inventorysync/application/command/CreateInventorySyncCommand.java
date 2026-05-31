package com.gogidix.ecommerce.inventorysync.application.command;

public record CreateInventorySyncCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
