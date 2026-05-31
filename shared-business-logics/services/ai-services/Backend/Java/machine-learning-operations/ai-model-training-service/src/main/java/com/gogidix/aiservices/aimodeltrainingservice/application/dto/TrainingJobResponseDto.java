package com.gogidix.aiservices.aimodeltrainingservice.application.dto;

import com.gogidix.aiservices.aimodeltrainingservice.domain.model.TrainingStatus;

import java.time.Instant;

/**
 * Response DTO for training job.
 */
public record TrainingJobResponseDto(
        String trainingJobId,
        String modelType,
        TrainingStatus status,
        String modelArtifactUrl,
        java.util.Map<String, Double> metrics,
        Instant startedAt,
        Instant estimatedCompletion
) {
}
