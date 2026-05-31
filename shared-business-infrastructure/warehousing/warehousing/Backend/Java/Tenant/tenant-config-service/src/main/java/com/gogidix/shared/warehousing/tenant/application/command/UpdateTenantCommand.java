package com.gogidix.shared.warehousing.tenant.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Update Tenant Command
 *
 * Command object for updating an existing tenant
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTenantCommand {

    @NotBlank(message = "Tenant name is required")
    private String tenantName;

    private String tenantType;

    private String storageModel;

    private Map<String, Object> businessRules;

    private Map<String, Object> pricingModel;

    private Map<String, String> integrationEndpoints;

    private Map<String, Object> complianceRequirements;

    private Map<String, Object> sla;

    private String status;
}
