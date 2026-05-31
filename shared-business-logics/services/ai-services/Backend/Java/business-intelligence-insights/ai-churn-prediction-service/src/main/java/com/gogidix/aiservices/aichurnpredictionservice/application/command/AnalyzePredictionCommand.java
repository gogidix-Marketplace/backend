package com.gogidix.aiservices.aichurnpredictionservice.application.command;

import java.util.Map;

/**
 * Command for analyzing a churn prediction.
 */
public record AnalyzePredictionCommand(
        String segmentId,
        String tenantId,
        String userId,
        Map<String, Object> analysisOptions
) {
    public AnalyzePredictionCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
