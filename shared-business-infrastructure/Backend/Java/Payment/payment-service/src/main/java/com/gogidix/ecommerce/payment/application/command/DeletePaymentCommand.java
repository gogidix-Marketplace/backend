package com.gogidix.ecommerce.payment.application.command;

public record DeletePaymentCommand(
    String tenantId,
    String id
) {}
