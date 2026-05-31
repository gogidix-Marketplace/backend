package com.gogidix.aiservices.aiuserprofilingservice.application.query;

/**
 * Query for finding a segment by ID.
 */
public record FindProfileByIdQuery(
        String segmentId,
        String tenantId
) {
    public FindProfileByIdQuery {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
