package com.gogidix.shared.warehousing.tenant.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Tenant Response DTO
 *
 * Response object for tenant data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantResponse {

    private String id;
    private String tenantId;
    private String tenantName;
    private String tenantType;
    private String storageModel;
    private Map<String, Object> businessRules;
    private Map<String, Object> pricingModel;
    private Map<String, String> integrationEndpoints;
    private Map<String, Object> complianceRequirements;
    private Map<String, Object> sla;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
