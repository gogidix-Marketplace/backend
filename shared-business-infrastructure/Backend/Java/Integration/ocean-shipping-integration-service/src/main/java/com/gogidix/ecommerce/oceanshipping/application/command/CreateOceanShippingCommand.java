package com.gogidix.ecommerce.oceanshipping.application.command;

public record CreateOceanShippingCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
