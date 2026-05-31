package com.gogidix.ecommerce.paymentgateway.application.command;

public record UpdatePaymentGatewayCommand(
    String tenantId,
    String id,
    String name,
    String description
) {}
