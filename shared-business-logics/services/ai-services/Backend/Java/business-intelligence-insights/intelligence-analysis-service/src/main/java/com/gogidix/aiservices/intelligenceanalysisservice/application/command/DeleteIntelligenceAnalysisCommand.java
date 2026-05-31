package com.gogidix.aiservices.intelligenceanalysisservice.application.command;

/**
 * Command for deleting a customer segment.
 */
public record DeleteIntelligenceAnalysisCommand(
        String segmentId,
        String tenantId,
        String userId
) {
    public DeleteIntelligenceAnalysisCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
