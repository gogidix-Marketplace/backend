package com.gogidix.ecommerce.sms.application.command;

public record DeleteSmsCommand(
    String tenantId,
    String id
) {}
