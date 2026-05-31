package com.gogidix.ecommerce.customer.application.query;

public record GetCustomerByIdQuery(
    String tenantId,
    String id
) {}
