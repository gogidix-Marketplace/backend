package com.gogidix.shared.infrastructure.services.security.analytics.application.dto.request;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
/**
 * Request DTO for creating a security event.
 */
public record CreateSecurityEventRequestDto(
    @NotBlank(message = "Event type is required")
    String eventType,
    @NotBlank(message = "Severity is required")
    String severity,
    String source,
    String description,
    LocalDateTime timestamp
) {
}
