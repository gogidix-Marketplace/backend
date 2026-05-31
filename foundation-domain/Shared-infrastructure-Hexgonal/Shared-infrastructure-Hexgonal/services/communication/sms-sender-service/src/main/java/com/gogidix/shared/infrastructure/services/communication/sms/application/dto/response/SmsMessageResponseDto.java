package com.gogidix.shared.infrastructure.services.communication.sms.application.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for SmsMessage operations
 */
public record SmsMessageResponseDto(
    String id,
    String tenantId,
    String phoneNumber,
    String countryCode,
    String status,
    String message,
    String templateName,
    String provider,
    String campaignId,
    String externalMessageId,
    String errorMessage,
    int retryCount,
    int maxRetries,
    LocalDateTime createdAt,
    LocalDateTime sentAt,
    LocalDateTime deliveredAt
) {
}
