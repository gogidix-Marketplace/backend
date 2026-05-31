package com.gogidix.ecommerce.discount.application.dto;
import java.math.BigDecimal;
public record UpdateDiscountRequest(
    String name,
    String description,
    String type,
    java.math.BigDecimal discountPercentage,
    java.math.BigDecimal discountAmount,
    java.math.BigDecimal minOrderValue,
    Boolean isActive
) {}
