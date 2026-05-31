package com.gogidix.aiservices.aisalesforecastingservice.application.command;

/**
 * Command for deleting a sales forecast.
 */
public record DeleteForecastCommand(
        String segmentId,
        String tenantId,
        String userId
) {
    public DeleteForecastCommand {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
