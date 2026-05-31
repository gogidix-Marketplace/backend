package com.gogidix.ecommerce.payment.application.query;

public record GetPaymentByIdQuery(
    String tenantId,
    String id
) {}
