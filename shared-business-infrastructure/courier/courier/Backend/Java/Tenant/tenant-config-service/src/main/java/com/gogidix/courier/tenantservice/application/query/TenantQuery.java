package com.gogidix.courier.tenantservice.application.query;

import com.gogidix.courier.tenantservice.domain.entity.Tenant;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Query for filtering tenants.
 */
@Schema(description="Query parameters for tenant filtering")
public record TenantQuery(
        @Schema(description="Filter by tenant ID")
        String tenantId,

        @Schema(description="Filter by name (partial match)")
        String name,

        @Schema(description="Filter by status")
        Tenant.TenantStatus status,

        @Schema(description="Page number (0-based)")
        int page,

        @Schema(description="Page size")
        int size,

        @Schema(description="Sort field")
        String sortBy,

        @Schema(description="Sort direction (ASC/DESC)")
        String sortDirection
) {
    public TenantQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100");
        }
    }

    public static TenantQuery create() {
        return new TenantQuery(null, null, null, 0, 20, "createdAt", "DESC");
    }

    public TenantQuery withTenantId(String tenantId) {
        return new TenantQuery(tenantId, this.name, this.status, this.page, this.size, this.sortBy, this.sortDirection);
    }

    public TenantQuery withName(String name) {
        return new TenantQuery(this.tenantId, name, this.status, this.page, this.size, this.sortBy, this.sortDirection);
    }

    public TenantQuery withStatus(Tenant.TenantStatus status) {
        return new TenantQuery(this.tenantId, this.name, status, this.page, this.size, this.sortBy, this.sortDirection);
    }

    public TenantQuery withPagination(int page, int size) {
        return new TenantQuery(this.tenantId, this.name, this.status, page, size, this.sortBy, this.sortDirection);
    }

    public TenantQuery withSort(String sortBy, String sortDirection) {
        return new TenantQuery(this.tenantId, this.name, this.status, this.page, this.size, sortBy, sortDirection);
    }
}
