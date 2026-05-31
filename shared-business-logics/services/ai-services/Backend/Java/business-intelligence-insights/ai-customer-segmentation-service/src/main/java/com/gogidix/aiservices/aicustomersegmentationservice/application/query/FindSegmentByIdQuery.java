package com.gogidix.aiservices.aicustomersegmentationservice.application.query;

/**
 * Query for finding a segment by ID.
 */
public record FindSegmentByIdQuery(
        String segmentId,
        String tenantId
) {
    public FindSegmentByIdQuery {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
