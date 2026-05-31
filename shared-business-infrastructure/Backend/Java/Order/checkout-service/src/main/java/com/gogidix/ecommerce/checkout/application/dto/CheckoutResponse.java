package com.gogidix.ecommerce.checkout.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CheckoutResponse(
    String id, String tenantId, String customerId, String status,
    BigDecimal total, Instant createdAt
) {
    public static CheckoutResponse from(CheckoutDto dto) {
        return new CheckoutResponse(dto.id(), dto.tenantId(), dto.customerId(),
            dto.status(), dto.total(), dto.createdAt());
    }
}
