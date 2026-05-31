package com.gogidix.ecommerce.cart.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateItemQuantityRequest(
        @NotNull @Min(1) int quantity
) {}
