package com.gogidix.shared.infrastructure.services.apimanagement.ratelimit.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating RateLimitConfig
 */
public record UpdateRateLimitConfigRequestDto(
    @Size(max = 255, message = "Endpoint must not exceed 255 characters")
    String endpoint,

    @Min(value = 1, message = "Requests per minute must be at least 1")
    Integer requestsPerMinute,

    @Min(value = 1, message = "Requests per hour must be at least 1")
    Integer requestsPerHour,

    @Min(value = 1, message = "Requests per day must be at least 1")
    Integer requestsPerDay,

    Boolean active,

    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description
) {
}
