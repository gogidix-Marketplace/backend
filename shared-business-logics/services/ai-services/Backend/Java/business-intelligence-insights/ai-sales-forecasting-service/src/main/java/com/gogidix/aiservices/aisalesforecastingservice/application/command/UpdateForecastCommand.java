package com.gogidix.aiservices.aisalesforecastingservice.application.command;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
import java.util.Map;

/**
 * Command for updating an existing sales forecast.
 */
public record UpdateForecastCommand(
        String segmentId,
        String name,
        String description,
        ForecastType segmentType,
        Map<String, Object> criteria,
        Boolean active,
        String tenantId,
        String userId
) {
    public UpdateForecastCommand {
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
