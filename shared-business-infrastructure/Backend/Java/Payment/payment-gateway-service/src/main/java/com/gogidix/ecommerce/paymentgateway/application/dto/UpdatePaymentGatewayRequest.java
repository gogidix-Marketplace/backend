package com.gogidix.ecommerce.paymentgateway.application.dto;

public record UpdatePaymentGatewayRequest(
    String name,
    String description,
    String type,
    String gatewayName,
    String gatewayCode,
    String supportedCurrencies,
    Boolean isActive
) {}
