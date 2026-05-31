package com.gogidix.hr.globalcompliance.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Issue Commands (Input Port)
 * Defines the input commands for non-compliance issue operations
 */
public interface IssueCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateIssueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        private String requirementId;

        private String checkId;

        @NotBlank(message = "Country code is required")
        private String countryCode;

        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Description is required")
        private String description;

        @NotBlank(message = "Severity is required")
        private String severity;

        @NotBlank(message = "Identified by is required")
        private String identifiedBy;

        private String identifiedByName;

        private List<String> affectedEmployees;

        private Double financialImpact;

        private String currency;

        private String department;

        private String location;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AssignIssueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Issue ID is required")
        private String issueId;

        @NotBlank(message = "Assigned to is required")
        private String assignedTo;

        private String assignedToName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class StartProgressCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Issue ID is required")
        private String issueId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ResolveIssueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Issue ID is required")
        private String issueId;

        @NotBlank(message = "Resolution is required")
        private String resolution;

        private String rootCause;

        @NotBlank(message = "Resolved by is required")
        private String resolvedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CloseIssueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Issue ID is required")
        private String issueId;

        @NotBlank(message = "Closed by is required")
        private String closedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class EscalateIssueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Issue ID is required")
        private String issueId;

        @NotBlank(message = "Reason is required")
        private String reason;

        @NotBlank(message = "Escalated by is required")
        private String escalatedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ReopenIssueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Issue ID is required")
        private String issueId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateSeverityCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Issue ID is required")
        private String issueId;

        @NotBlank(message = "New severity is required")
        private String newSeverity;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateDueDateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Issue ID is required")
        private String issueId;

        @NotNull(message = "Due date is required")
        private LocalDate newDueDate;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddAffectedEmployeeCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Issue ID is required")
        private String issueId;

        @NotBlank(message = "Employee ID is required")
        private String employeeId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetFinancialImpactCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Issue ID is required")
        private String issueId;

        private Double financialImpact;

        private String currency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddActionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Issue ID is required")
        private String issueId;

        @NotBlank(message = "Action is required")
        private String action;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteIssueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Issue ID is required")
        private String issueId;
    }
}
