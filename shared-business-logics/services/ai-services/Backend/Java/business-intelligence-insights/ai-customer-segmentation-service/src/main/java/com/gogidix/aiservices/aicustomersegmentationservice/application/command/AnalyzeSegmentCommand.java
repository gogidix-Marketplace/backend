package com.gogidix.aiservices.aicustomersegmentationservice.application.command;

import java.util.Map;

/**
 * Command for analyzing a customer segment.
 */
public record AnalyzeSegmentCommand(
        String segmentId,
        String tenantId,
        String userId,
        Map<String, Object> analysisOptions
) {
    public AnalyzeSegmentCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
