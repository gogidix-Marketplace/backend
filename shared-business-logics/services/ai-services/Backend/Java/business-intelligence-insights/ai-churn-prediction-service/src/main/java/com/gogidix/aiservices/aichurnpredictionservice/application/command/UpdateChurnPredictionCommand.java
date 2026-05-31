package com.gogidix.aiservices.aichurnpredictionservice.application.command;

import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionCriteria;

/**
 * Command for updating an existing churn prediction.
 */
public record UpdateChurnPredictionCommand(
        String segmentId,
        String tenantId,
        String userId,
        String name,
        String description,
        PredictionCriteria criteria,
        String status
) {
    public UpdateChurnPredictionCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (name != null && name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
    }
}
