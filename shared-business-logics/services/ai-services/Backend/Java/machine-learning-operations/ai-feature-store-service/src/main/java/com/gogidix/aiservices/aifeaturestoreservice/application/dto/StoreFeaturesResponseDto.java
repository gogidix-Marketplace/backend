package com.gogidix.aiservices.aifeaturestoreservice.application.dto;

import java.time.Instant;

/**
 * Response DTO for storing features.
 */
public record StoreFeaturesResponseDto(
        String featureVersion,
        int storedCount,
        Instant timestamp
) {
}
