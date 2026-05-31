package com.gogidix.ecommerce.payment.application.dto;

public record UpdatePaymentRequest(
    String name,
    String description,
    String type,
    String orderId,
    java.math.BigDecimal amount,
    String paymentStatus,
    Boolean isActive
) {}
