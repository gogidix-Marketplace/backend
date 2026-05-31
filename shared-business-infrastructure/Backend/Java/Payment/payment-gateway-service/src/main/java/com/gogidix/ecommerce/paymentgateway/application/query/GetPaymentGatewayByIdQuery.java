package com.gogidix.ecommerce.paymentgateway.application.query;

public record GetPaymentGatewayByIdQuery(
    String tenantId,
    String id
) {}
