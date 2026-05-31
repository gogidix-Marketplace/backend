package com.gogidix.aiservices.aimarketbasketanalysisservice.application.query;

import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType;

/**
 * Query for finding segments by tenant with filtering options.
 */
public record FindBasketsByTenantQuery(
        String tenantId,
        BasketType segmentType,
        Boolean active,
        int page,
        int size,
        String sortBy,
        String sortDirection
) {
    public FindBasketsByTenantQuery {
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

    public static FindBasketsByTenantQuery create(String tenantId) {
        return new FindBasketsByTenantQuery(tenantId, null, null, 0, 20, "createdAt", "DESC");
    }
}
