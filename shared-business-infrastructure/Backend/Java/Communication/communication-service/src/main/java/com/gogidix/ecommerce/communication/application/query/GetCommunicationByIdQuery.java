package com.gogidix.ecommerce.communication.application.query;

public record GetCommunicationByIdQuery(
    String tenantId,
    String id
) {}
