package com.gogidix.ecommerce.payment.application.query;

public record GetPaymentListQuery(
    String tenantId,
    int page,
    int size
) {}
