package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query to get a tenant by ID or tenantId.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTenantQuery {

    private String id;
    private String tenantId;
    private String domain;

    public boolean hasId() {
        return id != null && !id.isBlank();
    }

    public boolean hasTenantId() {
        return tenantId != null && !tenantId.isBlank();
    }

    public boolean hasDomain() {
        return domain != null && !domain.isBlank();
    }

    public boolean hasSearchCriteria() {
        return hasId() || hasTenantId() || hasDomain();
    }
}
