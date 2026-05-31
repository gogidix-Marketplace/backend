package com.gogidix.aiservices.aisalesforecastingservice.application.command;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria;

/**
 * Command for updating an existing sales forecast.
 */
public record UpdateSalesForecastCommand(
        String segmentId,
        String tenantId,
        String userId,
        String name,
        String description,
        ForecastCriteria criteria,
        String status
) {
    public UpdateSalesForecastCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (name != null && name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
    }
}
