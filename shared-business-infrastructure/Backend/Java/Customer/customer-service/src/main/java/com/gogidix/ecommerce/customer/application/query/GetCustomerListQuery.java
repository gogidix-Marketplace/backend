package com.gogidix.ecommerce.customer.application.query;

public record GetCustomerListQuery(
    String tenantId,
    int page,
    int size
) {}
