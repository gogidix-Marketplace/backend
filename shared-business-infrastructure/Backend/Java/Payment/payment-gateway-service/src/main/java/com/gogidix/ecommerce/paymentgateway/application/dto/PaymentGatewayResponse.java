package com.gogidix.ecommerce.paymentgateway.application.dto;

import java.time.Instant;

public record PaymentGatewayResponse(
    String id,
    String name,
    String description,
    String type,
    String gatewayName,
    String gatewayCode,
    String supportedCurrencies,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
