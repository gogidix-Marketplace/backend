package com.gogidix.shared.infrastructure.services.apimanagement.ratelimit.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating RateLimitConfig
 */
public record CreateRateLimitConfigRequestDto(
    @NotBlank(message = "API Key is required")
    @Size(max = 100, message = "API Key must not exceed 100 characters")
    String apiKey,

    @Size(max = 255, message = "Endpoint must not exceed 255 characters")
    String endpoint,

    @Min(value = 1, message = "Requests per minute must be at least 1")
    Integer requestsPerMinute,

    @Min(value = 1, message = "Requests per hour must be at least 1")
    Integer requestsPerHour,

    @Min(value = 1, message = "Requests per day must be at least 1")
    Integer requestsPerDay,

    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description
) {
}
