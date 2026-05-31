package com.gogidix.aiservices.aisalesforecastingservice.application.query;

/**
 * Query for finding a segment by ID.
 */
public record FindForecastByIdQuery(
        String segmentId,
        String tenantId
) {
    public FindForecastByIdQuery {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
