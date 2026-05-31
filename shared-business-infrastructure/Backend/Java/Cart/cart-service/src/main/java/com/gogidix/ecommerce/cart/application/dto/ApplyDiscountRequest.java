package com.gogidix.ecommerce.cart.application.dto;

import jakarta.validation.constraints.NotBlank;

public record ApplyDiscountRequest(
        @NotBlank String discountCode
) {}
