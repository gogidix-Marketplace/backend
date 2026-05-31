package com.gogidix.ecommerce.sms.application.command;

public record UpdateSmsCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
