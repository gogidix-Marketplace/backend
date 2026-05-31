package com.gogidix.aiservices.aiproductrecommendationservice.application.query;

/**
 * Query for finding a segment by ID.
 */
public record FindRecommendationByIdQuery(
        String segmentId,
        String tenantId
) {
    public FindRecommendationByIdQuery {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
