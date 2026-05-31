package com.gogidix.ecommerce.airfreight.application.query;

public record GetAirFreightListQuery(
    String tenantId,
    int page,
    int size
) {}
