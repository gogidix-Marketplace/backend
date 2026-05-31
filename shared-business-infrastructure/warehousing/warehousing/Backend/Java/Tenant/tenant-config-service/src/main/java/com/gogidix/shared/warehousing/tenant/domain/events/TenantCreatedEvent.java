package com.gogidix.shared.warehousing.tenant.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Tenant Created Event
 *
 * Published when a new tenant is created
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantCreatedEvent {

    private String tenantId;
    private String tenantName;
    private String tenantType;
    private String storageModel;
    private Map<String, Object> businessRules;
    private LocalDateTime createdAt;
}
