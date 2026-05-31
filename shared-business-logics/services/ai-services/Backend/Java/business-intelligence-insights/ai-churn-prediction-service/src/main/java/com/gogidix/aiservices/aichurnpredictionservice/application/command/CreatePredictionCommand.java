package com.gogidix.aiservices.aichurnpredictionservice.application.command;

import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionType;
import java.util.Map;

/**
 * Command for creating a new churn prediction.
 */
public record CreatePredictionCommand(
        String name,
        String description,
        PredictionType segmentType,
        Map<String, Object> criteria,
        String tenantId,
        String userId
) {
    public CreatePredictionCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("description must not exceed 500 characters");
        }
        if (segmentType == null) {
            throw new IllegalArgumentException("segmentType is required");
        }
        if (criteria == null || criteria.isEmpty()) {
            throw new IllegalArgumentException("criteria cannot be null or empty");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
