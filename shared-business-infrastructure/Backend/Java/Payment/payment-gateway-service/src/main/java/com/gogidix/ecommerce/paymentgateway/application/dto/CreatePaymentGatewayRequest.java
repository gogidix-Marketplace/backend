package com.gogidix.ecommerce.paymentgateway.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePaymentGatewayRequest(
    @NotBlank String name,
    String description,
    String type,
    String gatewayName,
    String gatewayCode,
    String supportedCurrencies
) {}
