package com.gogidix.shared.infrastructure.services.apimanagement.ratelimit.application.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for RateLimitConfig
 */
public record RateLimitConfigResponseDto(
    String id,
    String tenantId,
    String apiKey,
    String endpoint,
    int requestsPerMinute,
    int requestsPerHour,
    int requestsPerDay,
    boolean active,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
