package com.gogidix.shared.infrastructure.services.security.threat.application.dto.response;
import java.time.LocalDateTime;
/**
 * Response DTO for threat indicator operations.
 */
public record ThreatIndicatorResponseDto(
    String id,
    String tenantId,
    String indicatorId,
    String indicatorType,
    String value,
    String severity,
    String description,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
