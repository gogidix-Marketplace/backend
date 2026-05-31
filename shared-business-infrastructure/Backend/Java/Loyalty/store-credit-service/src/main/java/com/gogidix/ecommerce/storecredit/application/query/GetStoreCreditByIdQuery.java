package com.gogidix.ecommerce.storecredit.application.query;

public record GetStoreCreditByIdQuery(
    String tenantId,
    String id
) {}
