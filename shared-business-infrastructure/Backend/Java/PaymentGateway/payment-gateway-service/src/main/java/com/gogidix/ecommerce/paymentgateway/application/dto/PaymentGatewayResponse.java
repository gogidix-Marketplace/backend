package com.gogidix.ecommerce.paymentgateway.application.dto;

import java.time.LocalDateTime;

public record PaymentGatewayResponse(
    String id,
    String name,
    String description,
    String type,
    boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String gatewayName, String gatewayCode, String supportedCurrencies
) {
}