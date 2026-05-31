package com.gogidix.aiservices.intelligenceanalysisservice.application.command;

/**
 * Command for deleting a customer segment.
 */
public record DeleteAnalysisCommand(
        String segmentId,
        String tenantId,
        String userId
) {
    public DeleteAnalysisCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
