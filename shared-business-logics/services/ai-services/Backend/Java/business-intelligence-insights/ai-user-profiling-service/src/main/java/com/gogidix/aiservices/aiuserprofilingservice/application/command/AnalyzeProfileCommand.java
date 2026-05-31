package com.gogidix.aiservices.aiuserprofilingservice.application.command;

import java.util.Map;

/**
 * Command for analyzing a customer segment.
 */
public record AnalyzeProfileCommand(
        String segmentId,
        String tenantId,
        String userId,
        Map<String, Object> analysisOptions
) {
    public AnalyzeProfileCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
