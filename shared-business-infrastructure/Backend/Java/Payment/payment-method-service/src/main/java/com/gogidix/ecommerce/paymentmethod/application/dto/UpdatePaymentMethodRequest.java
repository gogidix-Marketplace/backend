package com.gogidix.ecommerce.paymentmethod.application.dto;

public record UpdatePaymentMethodRequest(
    String name,
    String description,
    String type,
    String methodName,
    String methodCode,
    Boolean enabled,
    Boolean isActive
) {}
