package com.gogidix.aiservices.aisalesforecastingservice.application.command;

import java.util.Map;

/**
 * Command for analyzing a sales forecast.
 */
public record AnalyzeForecastCommand(
        String segmentId,
        String tenantId,
        String userId,
        Map<String, Object> analysisOptions
) {
    public AnalyzeForecastCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
