package com.gogidix.aiservices.aichurnpredictionservice.application.query;

import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionType;

/**
 * Query for finding segments by tenant with filtering options.
 */
public record FindPredictionsByTenantQuery(
        String tenantId,
        PredictionType segmentType,
        Boolean active,
        int page,
        int size,
        String sortBy,
        String sortDirection
) {
    public FindPredictionsByTenantQuery {
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

    public static FindPredictionsByTenantQuery create(String tenantId) {
        return new FindPredictionsByTenantQuery(tenantId, null, null, 0, 20, "createdAt", "DESC");
    }
}
