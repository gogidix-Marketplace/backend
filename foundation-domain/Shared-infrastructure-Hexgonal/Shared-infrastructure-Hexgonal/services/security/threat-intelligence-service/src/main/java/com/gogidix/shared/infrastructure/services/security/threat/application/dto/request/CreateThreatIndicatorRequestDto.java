package com.gogidix.shared.infrastructure.services.security.threat.application.dto.request;
import jakarta.validation.constraints.NotBlank;
/**
 * Request DTO for creating a threat indicator.
 */
public record CreateThreatIndicatorRequestDto(
    @NotBlank(message = "Indicator type is required")
    String indicatorType,
    @NotBlank(message = "Value is required")
    String value,
    String severity,
    String description
) {
}
