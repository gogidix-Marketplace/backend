package com.gogidix.ecommerce.inventorysync.application.command;

public record UpdateInventorySyncCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
