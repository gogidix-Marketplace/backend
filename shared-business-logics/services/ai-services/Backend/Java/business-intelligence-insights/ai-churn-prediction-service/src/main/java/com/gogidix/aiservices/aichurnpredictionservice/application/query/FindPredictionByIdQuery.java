package com.gogidix.aiservices.aichurnpredictionservice.application.query;

/**
 * Query for finding a segment by ID.
 */
public record FindPredictionByIdQuery(
        String segmentId,
        String tenantId
) {
    public FindPredictionByIdQuery {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
