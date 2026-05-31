package com.gogidix.ecommerce.cart.application.dto;

import com.gogidix.ecommerce.cart.domain.model.Cart;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public record CartDto(
        String id,
        String customerId,
        Cart.CartStatus status,
        String currency,
        String channelId,
        List<CartItemDto> items,
        CartSummaryDto summary,
        Instant expiresAt,
        Instant createdAt,
        Instant updatedAt,
        Integer version
) {}
