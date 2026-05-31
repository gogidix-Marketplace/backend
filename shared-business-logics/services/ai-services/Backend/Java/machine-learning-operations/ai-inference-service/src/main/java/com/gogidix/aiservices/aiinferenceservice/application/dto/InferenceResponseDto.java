package com.gogidix.aiservices.aiinferenceservice.application.dto;

import com.gogidix.aiservices.aiinferenceservice.domain.model.InferenceResult;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO for inference.
 */
public record InferenceResponseDto(
        String inferenceId,
        String modelId,
        List<InferenceResult.Prediction> predictions,
        Long latencyMs,
        Instant executedAt
) {
}
