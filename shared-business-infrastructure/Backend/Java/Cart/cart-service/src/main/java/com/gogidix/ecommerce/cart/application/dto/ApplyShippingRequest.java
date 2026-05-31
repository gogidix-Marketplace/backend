package com.gogidix.ecommerce.cart.application.dto;

import jakarta.validation.constraints.NotBlank;

public record ApplyShippingRequest(
        @NotBlank String shippingMethodId
) {}
