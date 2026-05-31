package com.gogidix.shared.infrastructure.services.communication.email.application.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for EmailMessage operations
 */
public record EmailMessageResponseDto(
    String id,
    String tenantId,
    String to,
    String cc,
    String bcc,
    String subject,
    String body,
    String templateName,
    String status,
    int retryCount,
    int maxRetries,
    String errorMessage,
    String provider,
    String campaignId,
    LocalDateTime createdAt,
    LocalDateTime sentAt
) {
}
