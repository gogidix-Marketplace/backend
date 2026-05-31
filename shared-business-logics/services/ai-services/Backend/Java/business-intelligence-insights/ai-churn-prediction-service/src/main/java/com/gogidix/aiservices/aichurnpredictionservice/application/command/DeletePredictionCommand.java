package com.gogidix.aiservices.aichurnpredictionservice.application.command;

/**
 * Command for deleting a churn prediction.
 */
public record DeletePredictionCommand(
        String segmentId,
        String tenantId,
        String userId
) {
    public DeletePredictionCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
