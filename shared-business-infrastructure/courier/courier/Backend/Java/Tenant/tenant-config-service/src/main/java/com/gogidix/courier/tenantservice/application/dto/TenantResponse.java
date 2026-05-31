package com.gogidix.courier.tenantservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogidix.courier.tenantservice.domain.entity.Tenant;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

/**
 * Response DTO for tenant data.
 */
@Schema(description="Response DTO for tenant")
public record TenantResponse(

        @Schema(description="Unique tenant ID", example = "550e8400-e29b-41d4-a716-446655440000")
        String id,

        @JsonProperty("tenant_id")
        @Schema(description="Tenant identifier", example = "tenant-001")
        String tenantId,

        @Schema(description="Tenant name", example = "Acme Courier")
        String name,

        @Schema(description="Tenant description", example = "Acme Corporation Courier Service")
        String description,

        @Schema(description="Tenant status", example = "ACTIVE")
        Tenant.TenantStatus status,

        @JsonProperty("config")
        @Schema(description="Tenant configuration")
        TenantConfigResponse config,

        @JsonProperty("created_at")
        @Schema(description="Creation timestamp", example = "2023-01-01T00:00:00Z")
        Instant createdAt,

        @JsonProperty("updated_at")
        @Schema(description="Last update timestamp", example = "2023-01-01T00:00:00Z")
        Instant updatedAt,

        @JsonProperty("activated_at")
        @Schema(description="Activation timestamp", example = "2023-01-01T00:00:00Z")
        Instant activatedAt,

        @JsonProperty("deactivated_at")
        @Schema(description="Deactivation timestamp", example = "2023-01-01T00:00:00Z")
        Instant deactivatedAt
) {
}
