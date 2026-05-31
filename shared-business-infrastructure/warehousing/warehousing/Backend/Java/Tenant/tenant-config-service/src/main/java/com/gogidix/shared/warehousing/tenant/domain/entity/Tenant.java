package com.gogidix.shared.warehousing.tenant.domain.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Tenant Entity - Multi-tenant configuration
 *
 * Stores tenant-specific business rules, pricing, and compliance requirements
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tenants")
@CompoundIndex(def = "{'tenantId': 1, 'id': 1}", name = "idx_tenant_id")
@CompoundIndex(def = "{'tenantId': 1}", unique = true)
@CompoundIndex(def = "{'tenantId': 1, 'status': 1}", name = "idx_tenant_status")
@CompoundIndex(def = "{'tenantId': 1, 'tenantType': 1}", name = "idx_tenant_type")
public class Tenant {

    @Id
    private String id;

    @Indexed
    private String tenantId; // Unique tenant identifier

    private String tenantName;
    private String tenantType; // ECOMMERCE_VENDOR, WAREHOUSE_COMPANY, etc.

    // Business Rules (stored as flexible JSON)
    private Map<String, Object> businessRules;

    // Storage Model
    private String storageModel; // HYBRID, MANAGED, VENDOR_SELF_STORAGE

    // Pricing Model
    private Map<String, Object> pricingModel;

    // Integration Endpoints
    private Map<String, String> integrationEndpoints;

    // Compliance Requirements
    private Map<String, Object> complianceRequirements;

    // SLA Configuration
    private Map<String, Object> sla;

    private String status; // ACTIVE, SUSPENDED, TERMINATED

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
