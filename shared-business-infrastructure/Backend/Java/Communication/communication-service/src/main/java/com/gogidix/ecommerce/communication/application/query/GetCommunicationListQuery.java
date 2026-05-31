package com.gogidix.ecommerce.communication.application.query;

public record GetCommunicationListQuery(
    String tenantId,
    int page,
    int size
) {}
