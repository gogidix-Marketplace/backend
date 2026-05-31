package com.gogidix.shared.warehousing.tenant.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tenant Query
 *
 * Query object for searching tenants
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantQuery {

    private String tenantId;
    private String tenantType;
    private String status;
    private String storageModel;
}
