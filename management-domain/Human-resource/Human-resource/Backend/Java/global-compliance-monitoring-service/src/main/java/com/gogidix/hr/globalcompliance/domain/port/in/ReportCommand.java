package com.gogidix.hr.globalcompliance.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Report Commands (Input Port)
 * Defines the input commands for compliance report operations
 */
public interface ReportCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateReportCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report type is required")
        private String reportType;

        @NotBlank(message = "Country code is required")
        private String countryCode;

        @NotNull(message = "Period start is required")
        private LocalDate periodStart;

        @NotNull(message = "Period end is required")
        private LocalDate periodEnd;

        @NotBlank(message = "Prepared by is required")
        private String preparedBy;

        private String preparedByName;

        private String department;

        private String region;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetMetricsCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;

        private Integer totalRequirements;

        private Integer passedChecks;

        private Integer failedChecks;

        private Integer pendingChecks;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddCriticalIssueCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;

        @NotBlank(message = "Critical issue is required")
        private String criticalIssue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddRecommendationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;

        @NotBlank(message = "Recommendation is required")
        private String recommendation;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetSummaryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;

        private String summary;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class IncludeRequirementCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;

        @NotBlank(message = "Requirement ID is required")
        private String requirementId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class IncludeCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;

        @NotBlank(message = "Check ID is required")
        private String checkId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SubmitReportCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ApproveReportCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;

        @NotBlank(message = "Approved by is required")
        private String approvedBy;

        private String approvedByName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RejectReportCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;

        @NotBlank(message = "Rejection reason is required")
        private String rejectionReason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class PublishReportCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ReturnToDraftCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateNotesCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteReportCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;
    }
}
