package com.gogidix.aiservices.aiinferenceservice.application.dto;

import com.gogidix.aiservices.aiinferenceservice.domain.model.ModelStatus;

import java.time.Instant;

/**
 * Response DTO for model status.
 */
public record ModelStatusResponseDto(
        String modelId,
        ModelStatus status,
        Instant loadedAt,
        Instant cacheExpiry
) {
}
