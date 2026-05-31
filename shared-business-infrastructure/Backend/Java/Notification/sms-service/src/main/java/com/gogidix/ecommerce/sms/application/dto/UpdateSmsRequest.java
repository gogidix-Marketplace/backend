package com.gogidix.ecommerce.sms.application.dto;

public record UpdateSmsRequest(
    String name,
    String description,
    String type,
    String phoneNumber,
    String message,
    String deliveryStatus,
    Boolean isActive
) {}
