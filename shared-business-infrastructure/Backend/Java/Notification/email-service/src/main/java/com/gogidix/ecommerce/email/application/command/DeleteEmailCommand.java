package com.gogidix.ecommerce.email.application.command;

public record DeleteEmailCommand(
    String tenantId,
    String id
) {}
