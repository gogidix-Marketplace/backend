package com.gogidix.ecommerce.oceanshipping.application.query;

public record GetOceanShippingListQuery(
    String tenantId,
    int page,
    int size
) {}
