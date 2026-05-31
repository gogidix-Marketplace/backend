package com.gogidix.ecommerce.sms.application.dto;

import java.time.LocalDateTime;

public record SmsResponse(
    String id,
    String name,
    String description,
    String type,
    boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String phoneNumber, String message, String deliveryStatus
) {
}