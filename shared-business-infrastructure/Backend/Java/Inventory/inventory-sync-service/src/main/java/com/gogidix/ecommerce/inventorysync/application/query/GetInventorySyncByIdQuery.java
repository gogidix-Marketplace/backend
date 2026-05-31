package com.gogidix.ecommerce.inventorysync.application.query;

public record GetInventorySyncByIdQuery(
    String tenantId,
    String id
) {}
