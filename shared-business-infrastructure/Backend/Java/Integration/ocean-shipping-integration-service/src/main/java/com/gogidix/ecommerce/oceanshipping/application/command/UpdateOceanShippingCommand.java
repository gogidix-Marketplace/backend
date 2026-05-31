package com.gogidix.ecommerce.oceanshipping.application.command;

public record UpdateOceanShippingCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
