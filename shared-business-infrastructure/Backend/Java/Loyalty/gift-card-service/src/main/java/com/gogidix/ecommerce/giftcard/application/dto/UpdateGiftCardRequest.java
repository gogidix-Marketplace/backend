package com.gogidix.ecommerce.giftcard.application.dto;

public record UpdateGiftCardRequest(
    String name,
    String description,
    String type,
    String cardNumber,
    java.math.BigDecimal balance,
    java.math.BigDecimal initialAmount,
    Boolean isActive
) {}
