package com.gogidix.hr.globalcompliance.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Requirement Commands (Input Port)
 * Defines the input commands for compliance requirement operations
 */
public interface RequirementCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateRequirementCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Requirement code is required")
        private String requirementCode;

        @NotBlank(message = "Requirement name is required")
        private String requirementName;

        @NotBlank(message = "Category is required")
        private String category;

        @NotBlank(message = "Country code is required")
        private String countryCode;

        private String description;

        private String authority;

        @NotBlank(message = "Type is required")
        private String type;

        private LocalDate effectiveFrom;

        private LocalDate effectiveTo;

        private String frequency;

        @NotBlank(message = "Severity is required")
        private String severity;

        private String ownerDepartment;

        private String ownerId;

        private List<String> relatedRequirements;

        @NotBlank(message = "Created by is required")
        private String createdBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateRequirementCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Requirement ID is required")
        private String requirementId;

        private String requirementName;

        private String description;

        private String authority;

        private String ownerDepartment;

        private String ownerId;

        private LocalDate effectiveTo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ActivateRequirementCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Requirement ID is required")
        private String requirementId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeactivateRequirementCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Requirement ID is required")
        private String requirementId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ScheduleCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Requirement ID is required")
        private String requirementId;

        @NotNull(message = "Scheduled date is required")
        private LocalDate scheduledDate;

        private String frequency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddRelatedRequirementCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Requirement ID is required")
        private String requirementId;

        @NotBlank(message = "Related requirement ID is required")
        private String relatedRequirementId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateReviewDateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Requirement ID is required")
        private String requirementId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteRequirementCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Requirement ID is required")
        private String requirementId;
    }
}
