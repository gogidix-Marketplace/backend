package com.gogidix.ecommerce.payment.application.command;

public record UpdatePaymentCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
