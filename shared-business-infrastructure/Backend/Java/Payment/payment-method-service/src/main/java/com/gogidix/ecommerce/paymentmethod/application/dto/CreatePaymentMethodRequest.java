package com.gogidix.ecommerce.paymentmethod.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePaymentMethodRequest(
    @NotBlank String name,
    String description,
    String type,
    String methodName,
    String methodCode,
    Boolean enabled
) {}
