package com.gogidix.ecommerce.payment.application.command;

public record CreatePaymentCommand(
    String tenantId,
    String name,
    String description,
    String type
) {}
