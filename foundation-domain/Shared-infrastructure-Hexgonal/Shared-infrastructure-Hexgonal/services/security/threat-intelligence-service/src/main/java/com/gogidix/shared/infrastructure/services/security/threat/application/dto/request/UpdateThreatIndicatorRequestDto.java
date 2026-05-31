package com.gogidix.shared.infrastructure.services.security.threat.application.dto.request;
/**
 * Request DTO for updating a threat indicator.
 */
public record UpdateThreatIndicatorRequestDto(
    String indicatorType,
    String value,
    String severity,
    String description
) {
}
