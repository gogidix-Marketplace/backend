package com.gogidix.ecommerce.oceanshipping.application.command;

public record DeleteOceanShippingCommand(
    String tenantId,
    String id
) {}
