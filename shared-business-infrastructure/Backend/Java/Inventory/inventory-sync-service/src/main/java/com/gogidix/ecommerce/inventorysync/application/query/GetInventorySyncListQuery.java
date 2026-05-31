package com.gogidix.ecommerce.inventorysync.application.query;

public record GetInventorySyncListQuery(
    String tenantId,
    int page,
    int size
) {}
