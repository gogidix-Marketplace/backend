package com.gogidix.ecommerce.payment.application.dto;

import java.time.Instant;

public record PaymentResponse(
    String id,
    String name,
    String description,
    String type,
    String orderId,
    java.math.BigDecimal amount,
    String paymentStatus,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
