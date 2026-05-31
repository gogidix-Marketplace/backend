package com.gogidix.shared.infrastructure.services.security.analytics.application.dto.response;
import java.time.LocalDateTime;
/**
 * Response DTO for security event operations.
 */
public record SecurityEventResponseDto(
    String id,
    String tenantId,
    String eventId,
    String eventType,
    String severity,
    String source,
    String description,
    LocalDateTime timestamp,
    LocalDateTime createdAt
) {
}
