package com.gogidix.ecommerce.sms.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSmsRequest(
    @NotBlank String name,
    String description,
    String type,
    String phoneNumber,
    String message,
    String deliveryStatus
) {}
