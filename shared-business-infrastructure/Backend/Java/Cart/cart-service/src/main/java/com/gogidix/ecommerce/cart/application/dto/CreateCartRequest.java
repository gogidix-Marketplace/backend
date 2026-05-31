package com.gogidix.ecommerce.cart.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCartRequest(
        @NotBlank String customerId,
        @NotBlank String currency,
        String channelId
) {}
