package com.gogidix.ecommerce.storecredit.application.command;

public record CreateStoreCreditCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
