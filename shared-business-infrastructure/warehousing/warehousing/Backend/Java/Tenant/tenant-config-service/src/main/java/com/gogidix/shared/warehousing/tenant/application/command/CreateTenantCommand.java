package com.gogidix.shared.warehousing.tenant.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Create Tenant Command
 *
 * Command object for creating a new tenant
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTenantCommand {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotBlank(message = "Tenant name is required")
    private String tenantName;

    @NotBlank(message = "Tenant type is required")
    private String tenantType;

    @NotBlank(message = "Storage model is required")
    private String storageModel;

    private Map<String, Object> businessRules;

    private Map<String, Object> pricingModel;

    private Map<String, String> integrationEndpoints;

    private Map<String, Object> complianceRequirements;

    private Map<String, Object> sla;

    @Builder.Default
    private String status = "ACTIVE";
}
