package com.gogidix.ecommerce.promotion.application.dto;

public record UpdatePromotionRequest(
    String name,
    String description,
    String type,
    String promotionCode,
    String promotionType,
    java.math.BigDecimal discountValue,
    Boolean isActive
) {}
