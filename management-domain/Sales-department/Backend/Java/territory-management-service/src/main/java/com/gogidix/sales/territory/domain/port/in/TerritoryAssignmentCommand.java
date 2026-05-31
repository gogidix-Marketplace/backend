package com.gogidix.sales.territory.domain.port.in;

import com.gogidix.sales.territory.domain.model.TerritoryAssignment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Territory Assignment Commands (Input Port)
 * Defines the input commands for territory assignment operations
 */
public interface TerritoryAssignmentCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateAssignmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Territory ID is required")
        private String territoryId;

        @NotBlank(message = "Sales Representative ID is required")
        private String salesRepresentativeId;

        private String salesRepresentativeName;

        @NotNull(message = "Assignment type is required")
        private TerritoryAssignment.AssignmentType type;

        private LocalDate effectiveDate;
        private LocalDate endDate;
        private Boolean primaryAssignment;
        private Integer priority;
        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ActivateAssignmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Assignment ID is required")
        private String assignmentId;

        @NotBlank(message = "Activated by is required")
        private String activatedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeactivateAssignmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Assignment ID is required")
        private String assignmentId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RevokeAssignmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Assignment ID is required")
        private String assignmentId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateAssignmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Assignment ID is required")
        private String assignmentId;

        private LocalDate endDate;
        private Boolean primaryAssignment;
        private Integer priority;
        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteAssignmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Assignment ID is required")
        private String assignmentId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateAssignmentPerformanceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Assignment ID is required")
        private String assignmentId;

        private java.math.BigDecimal salesGenerated;
        private Integer accountsManaged;
        private Integer dealsClosed;
    }
}
