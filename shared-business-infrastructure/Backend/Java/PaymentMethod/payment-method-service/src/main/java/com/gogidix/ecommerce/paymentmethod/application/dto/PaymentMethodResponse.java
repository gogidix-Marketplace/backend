package com.gogidix.ecommerce.paymentmethod.application.dto;

import java.time.LocalDateTime;

public record PaymentMethodResponse(
    String id,
    String name,
    String description,
    String type,
    boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String methodName, String methodCode, boolean isActive
) {
}