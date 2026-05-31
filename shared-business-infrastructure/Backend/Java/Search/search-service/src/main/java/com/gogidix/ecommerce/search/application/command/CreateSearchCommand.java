package com.gogidix.ecommerce.search.application.command;

public record CreateSearchCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
