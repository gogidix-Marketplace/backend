package com.gogidix.ecommerce.cart.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
    String id,
    String tenantId,
    String customerId,
    List<CartItemDto> items,
    BigDecimal subtotal,
    BigDecimal taxAmount,
    BigDecimal discountAmount,
    BigDecimal totalAmount,
    String currency
) {
    public static CartResponse from(CartDto dto) {
        return new CartResponse(
            dto.id(),
            dto.tenantId(),
            dto.customerId(),
            dto.items(),
            dto.subtotal(),
            dto.taxAmount(),
            dto.discountAmount(),
            dto.totalAmount(),
            null
        );
    }
}
