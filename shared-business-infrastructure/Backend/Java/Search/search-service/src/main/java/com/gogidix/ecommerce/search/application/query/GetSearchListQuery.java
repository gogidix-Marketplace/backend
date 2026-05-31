package com.gogidix.ecommerce.search.application.query;

public record GetSearchListQuery(
    String tenantId,
    int page,
    int size
) {}
