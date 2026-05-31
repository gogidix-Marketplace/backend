package com.gogidix.aiservices.aimanagementservice.application.dto;

import com.gogidix.aiservices.aimanagementservice.domain.model.ModelFramework;
import com.gogidix.aiservices.aimanagementservice.domain.model.ModelStatus;

import java.time.Instant;

/**
 * Response DTO for model.
 */
public record ModelResponseDto(
        String modelId,
        String modelName,
        ModelFramework framework,
        String version,
        String modelArtifactUrl,
        ModelStatus status,
        Long modelSizeBytes,
        Instant registeredAt,
        Instant deployedAt
) {
}
