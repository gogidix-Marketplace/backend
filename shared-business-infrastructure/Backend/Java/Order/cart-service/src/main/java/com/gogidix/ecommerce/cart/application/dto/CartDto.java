package com.gogidix.ecommerce.cart.application.dto;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;

public record CartDto(
    String id,
    String tenantId,
    String customerId,
    List<CartItemDto> items,
    BigDecimal subtotal,
    BigDecimal taxAmount,
    BigDecimal discountAmount,
    BigDecimal totalAmount,
    Instant createdAt,
    Instant updatedAt
) {}
