package com.gogidix.ecommerce.customer.application.command;

public record DeleteCustomerCommand(
    String tenantId,
    String id
) {}
