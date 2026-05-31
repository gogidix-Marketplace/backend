package com.gogidix.ecommerce.customer.application.command;

public record CreateCustomerCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
