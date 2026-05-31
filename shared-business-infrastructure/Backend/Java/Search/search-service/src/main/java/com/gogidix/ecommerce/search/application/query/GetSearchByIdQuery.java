package com.gogidix.ecommerce.search.application.query;

public record GetSearchByIdQuery(
    String tenantId,
    String id
) {}
