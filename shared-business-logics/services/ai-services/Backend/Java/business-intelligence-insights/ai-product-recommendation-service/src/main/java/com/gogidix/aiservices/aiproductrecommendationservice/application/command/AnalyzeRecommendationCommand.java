package com.gogidix.aiservices.aiproductrecommendationservice.application.command;

import java.util.Map;

/**
 * Command for analyzing a product recommendation.
 */
public record AnalyzeRecommendationCommand(
        String segmentId,
        String tenantId,
        String userId,
        Map<String, Object> analysisOptions
) {
    public AnalyzeRecommendationCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
