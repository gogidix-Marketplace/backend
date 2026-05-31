package com.gogidix.aiservices.intelligenceanalysisservice.application.query;

import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisType;

/**
 * Query for finding segments by tenant with filtering options.
 */
public record FindAnalysissByTenantQuery(
        String tenantId,
        AnalysisType segmentType,
        Boolean active,
        int page,
        int size,
        String sortBy,
        String sortDirection
) {
    public FindAnalysissByTenantQuery {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (page < 0) {
            throw new IllegalArgumentException("page must be non-negative");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100");
        }
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "createdAt";
        }
        if (sortDirection == null || (!sortDirection.equalsIgnoreCase("ASC") && !sortDirection.equalsIgnoreCase("DESC"))) {
            sortDirection = "DESC";
        }
    }

    public static FindAnalysissByTenantQuery create(String tenantId) {
        return new FindAnalysissByTenantQuery(tenantId, null, null, 0, 20, "createdAt", "DESC");
    }
}
