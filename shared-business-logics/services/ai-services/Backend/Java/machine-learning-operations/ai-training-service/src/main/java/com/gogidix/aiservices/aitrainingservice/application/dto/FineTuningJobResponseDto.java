package com.gogidix.aiservices.aitrainingservice.application.dto;

import com.gogidix.aiservices.aitrainingservice.domain.model.FineTuningStatus;

import java.time.Instant;

/**
 * Response DTO for fine-tuning job.
 */
public record FineTuningJobResponseDto(
        String fineTuningJobId,
        String baseModel,
        FineTuningStatus status,
        String fineTunedModelUrl,
        FineTuningJobMetricsDto metrics,
        Instant createdAt,
        Instant estimatedCompletion
) {
    public record FineTuningJobMetricsDto(Double loss, Double accuracy, Integer epochsCompleted) {}
}
