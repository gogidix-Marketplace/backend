package com.gogidix.aiservices.aimanagementservice.application.dto;

import java.time.Instant;

/**
 * Response DTO for model registration.
 */
public record RegisterModelResponseDto(
        String modelId,
        String status,
        Instant registeredAt
) {
}
