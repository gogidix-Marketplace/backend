package com.gogidix.ecommerce.promotion.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePromotionRequest(
    @NotBlank String name,
    String description,
    String type,
    String promotionCode,
    String promotionType,
    java.math.BigDecimal discountValue
) {}
