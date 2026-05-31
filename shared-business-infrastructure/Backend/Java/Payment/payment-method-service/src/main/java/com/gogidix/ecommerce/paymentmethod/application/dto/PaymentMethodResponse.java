package com.gogidix.ecommerce.paymentmethod.application.dto;

import java.time.Instant;

public record PaymentMethodResponse(
    String id,
    String name,
    String description,
    String type,
    String methodName,
    String methodCode,
    Boolean enabled,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
