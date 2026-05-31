package com.gogidix.ecommerce.search.application.command;

public record DeleteSearchCommand(
    String tenantId,
    String id
) {}
