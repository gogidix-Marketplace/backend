package com.gogidix.ecommerce.giftcard.application.dto;

import java.time.Instant;

public record GiftCardResponse(
    String id,
    String name,
    String description,
    String type,
    String cardNumber,
    java.math.BigDecimal balance,
    java.math.BigDecimal initialAmount,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
