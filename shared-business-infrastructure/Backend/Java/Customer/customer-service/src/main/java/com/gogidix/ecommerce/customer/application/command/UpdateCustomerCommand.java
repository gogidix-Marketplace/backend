package com.gogidix.ecommerce.customer.application.command;

public record UpdateCustomerCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
