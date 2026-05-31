package com.gogidix.ecommerce.sms.application.query;

public record GetSmsListQuery(
    String tenantId,
    int page,
    int size
) {}
