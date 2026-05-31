package com.gogidix.aiservices.aisalesforecastingservice.application.command;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
import java.util.Map;

/**
 * Command for creating a new sales forecast.
 */
public record CreateForecastCommand(
        String name,
        String description,
        ForecastType segmentType,
        Map<String, Object> criteria,
        String tenantId,
        String userId
) {
    public CreateForecastCommand {
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
