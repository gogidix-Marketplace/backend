package com.gogidix.aiservices.aisalesforecastingservice.application.query;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;

/**
 * Query for finding segments by tenant with filtering options.
 */
public record FindForecastsByTenantQuery(
        String tenantId,
        ForecastType segmentType,
        Boolean active,
        int page,
        int size,
        String sortBy,
        String sortDirection
) {
    public FindForecastsByTenantQuery {
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

    public static FindForecastsByTenantQuery create(String tenantId) {
        return new FindForecastsByTenantQuery(tenantId, null, null, 0, 20, "createdAt", "DESC");
    }
}
