package com.gogidix.ecommerce.search.application.command;

public record UpdateSearchCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
