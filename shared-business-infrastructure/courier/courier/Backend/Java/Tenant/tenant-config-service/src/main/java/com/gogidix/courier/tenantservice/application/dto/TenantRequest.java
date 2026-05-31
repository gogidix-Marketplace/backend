package com.gogidix.courier.tenantservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a tenant.
 */
@Schema(description="Request DTO for tenant creation/update")
public record TenantRequest(

        @JsonProperty("tenant_id")
        @Schema(description="Unique tenant identifier", example = "tenant-001", required = true)
        @NotBlank(message = "tenantId is required")
        String tenantId,

        @Schema(description="Tenant name", example = "Acme Courier", required = true)
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must not exceed 100 characters")
        String name,

        @Schema(description="Tenant description", example = "Acme Corporation Courier Service")
        String description
) {
}
