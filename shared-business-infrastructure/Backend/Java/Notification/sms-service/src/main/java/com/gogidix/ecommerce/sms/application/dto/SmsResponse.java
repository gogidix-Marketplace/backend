package com.gogidix.ecommerce.sms.application.dto;

import java.time.Instant;

public record SmsResponse(
    String id,
    String name,
    String description,
    String type,
    String phoneNumber,
    String message,
    String deliveryStatus,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
