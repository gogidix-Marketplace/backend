package com.gogidix.ecommerce.payment.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePaymentRequest(
    @NotBlank String name,
    String description,
    String type,
    String orderId,
    java.math.BigDecimal amount,
    String paymentStatus
) {}
