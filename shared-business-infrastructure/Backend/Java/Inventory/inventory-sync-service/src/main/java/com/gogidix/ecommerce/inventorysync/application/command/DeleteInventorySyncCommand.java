package com.gogidix.ecommerce.inventorysync.application.command;

public record DeleteInventorySyncCommand(
    String tenantId,
    String id
) {}
