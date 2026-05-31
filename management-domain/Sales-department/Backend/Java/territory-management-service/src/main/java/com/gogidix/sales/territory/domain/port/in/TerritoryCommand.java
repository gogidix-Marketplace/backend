package com.gogidix.sales.territory.domain.port.in;

import com.gogidix.sales.territory.domain.model.Territory;
import com.gogidix.sales.territory.domain.valueobject.GeographicBoundary;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Territory Commands (Input Port)
 * Defines the input commands for territory operations
 */
public interface TerritoryCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateTerritoryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Code is required")
        private String code;

        @NotNull(message = "Type is required")
        private Territory.TerritoryType type;

        private String description;
        private String regionId;
        private String managerId;
        private Integer priority;
        private String parentTerritoryId;

        // Geographic boundary data
        private GeographicBoundary geographicBoundary;

        // Product-based territory data
        private List<String> productCategories;
        private List<String> productIds;

        // Customer segment data
        private List<String> customerSegments;
        private List<String> customerTierIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateTerritoryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Territory ID is required")
        private String territoryId;

        private String name;
        private String description;
        private String regionId;
        private String managerId;
        private Integer priority;

        private GeographicBoundary geographicBoundary;
        private List<String> productCategories;
        private List<String> productIds;
        private List<String> customerSegments;
        private List<String> customerTierIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ActivateTerritoryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Territory ID is required")
        private String territoryId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeactivateTerritoryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Territory ID is required")
        private String territoryId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ArchiveTerritoryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Territory ID is required")
        private String territoryId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RequestRealignmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Territory ID is required")
        private String territoryId;

        @NotBlank(message = "Requested by is required")
        private String requestedBy;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CompleteRealignmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Territory ID is required")
        private String territoryId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteTerritoryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Territory ID is required")
        private String territoryId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddChildTerritoryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Parent Territory ID is required")
        private String parentTerritoryId;

        @NotBlank(message = "Child Territory ID is required")
        private String childTerritoryId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveChildTerritoryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Parent Territory ID is required")
        private String parentTerritoryId;

        @NotBlank(message = "Child Territory ID is required")
        private String childTerritoryId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdatePerformanceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Territory ID is required")
        private String territoryId;

        private java.math.BigDecimal currentSales;
        private java.math.BigDecimal quota;
        private Integer accountsCount;
        private Integer dealsCount;
    }
}
