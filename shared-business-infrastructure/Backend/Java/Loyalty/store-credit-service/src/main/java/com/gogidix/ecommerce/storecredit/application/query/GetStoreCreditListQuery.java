package com.gogidix.ecommerce.storecredit.application.query;

public record GetStoreCreditListQuery(
    String tenantId,
    int page,
    int size
) {}
