package com.gogidix.ecommerce.storecredit.application.command;

public record DeleteStoreCreditCommand(
    String tenantId,
    String id
) {}
