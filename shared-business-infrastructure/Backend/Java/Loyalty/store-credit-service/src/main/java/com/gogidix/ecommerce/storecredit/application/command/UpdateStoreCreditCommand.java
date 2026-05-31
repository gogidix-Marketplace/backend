package com.gogidix.ecommerce.storecredit.application.command;

public record UpdateStoreCreditCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
