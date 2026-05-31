package com.gogidix.aiservices.intelligenceanalysisservice.application.query;

/**
 * Query for finding a segment by ID.
 */
public record FindAnalysisByIdQuery(
        String segmentId,
        String tenantId
) {
    public FindAnalysisByIdQuery {
        if (segmentId == null || segmentId.isBlank()) {
            throw new IllegalArgumentException("segmentId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
