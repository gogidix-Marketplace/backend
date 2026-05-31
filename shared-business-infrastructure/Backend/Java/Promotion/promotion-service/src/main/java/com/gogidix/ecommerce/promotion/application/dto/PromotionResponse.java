package com.gogidix.ecommerce.promotion.application.dto;

import java.time.Instant;

public record PromotionResponse(
    String id,
    String name,
    String description,
    String type,
    String promotionCode,
    String promotionType,
    java.math.BigDecimal discountValue,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
