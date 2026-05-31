package com.gogidix.ecommerce.cart.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public record CartItemDto(
        String sku,
        String productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        String currency,
        Map<String, String> attributes,
        Instant addedAt,
        Instant updatedAt
) {}
