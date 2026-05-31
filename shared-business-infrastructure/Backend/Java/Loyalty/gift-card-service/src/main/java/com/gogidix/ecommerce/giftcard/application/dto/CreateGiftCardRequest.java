package com.gogidix.ecommerce.giftcard.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateGiftCardRequest(
    @NotBlank String name,
    String description,
    String type,
    String cardNumber,
    java.math.BigDecimal balance,
    java.math.BigDecimal initialAmount
) {}
