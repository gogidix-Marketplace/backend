package com.gogidix.aiservices.aimarketbasketanalysisservice.application.query;

/**
 * Query for finding a segment by ID.
 */
public record FindBasketByIdQuery(
        String segmentId,
        String tenantId
) {
    public FindBasketByIdQuery {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
