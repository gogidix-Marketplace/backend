package com.gogidix.aiservices.aichurnpredictionservice.application.command;

import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionType;
import java.util.Map;

/**
 * Command for updating an existing churn prediction.
 */
public record UpdatePredictionCommand(
        String segmentId,
        String name,
        String description,
        PredictionType segmentType,
        Map<String, Object> criteria,
        Boolean active,
        String tenantId,
        String userId
) {
    public UpdatePredictionCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (name != null && name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        if (name != null && name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("description must not exceed 500 characters");
        }
    }
}
