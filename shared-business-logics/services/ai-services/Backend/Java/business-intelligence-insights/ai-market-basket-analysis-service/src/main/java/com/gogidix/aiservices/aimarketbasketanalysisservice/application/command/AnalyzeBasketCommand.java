package com.gogidix.aiservices.aimarketbasketanalysisservice.application.command;

import java.util.Map;

/**
 * Command for analyzing a market basket.
 */
public record AnalyzeBasketCommand(
        String segmentId,
        String tenantId,
        String userId,
        Map<String, Object> analysisOptions
) {
    public AnalyzeBasketCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
