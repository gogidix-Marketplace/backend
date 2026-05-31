package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.hr.globalhrdashboard.domain.model.ComplianceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO for Compliance Metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Compliance metric response")
public class ComplianceResponseDto {

    @Schema(description = "Compliance metric ID")
    private String id;

    @Schema(description = "ISO country code")
    private String countryCode;

    @Schema(description = "Country name")
    private String countryName;

    @Schema(description = "Region code")
    private String regionCode;

    @Schema(description = "Compliance score (0-100)")
    private Double complianceScore;

    @Schema(description = "Total requirements")
    private Integer totalRequirements;

    @Schema(description = "Passed checks")
    private Integer passedChecks;

    @Schema(description = "Failed checks")
    private Integer failedChecks;

    @Schema(description = "Critical issues count")
    private Integer criticalIssues;

    @Schema(description = "Period")
    private String period;

    @Schema(description = "Compliance status")
    private ComplianceStatus status;

    @Schema(description = "Compliance issues")
    private List<ComplianceIssueDto> issues;

    @Schema(description = "Pending actions")
    private List<String> pendingActions;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Last assessed timestamp")
    private Instant lastAssessed;

    @Schema(description = "Assessed by")
    private String assessedBy;

    @Schema(description = "Notes")
    private String notes;

    @Schema(description = "Is active")
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Created at timestamp")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Updated at timestamp")
    private Instant updatedAt;

    @Schema(description = "Pass rate percentage")
    public Double getPassRate() {
        if (totalRequirements == null || totalRequirements == 0) {
            return 0.0;
        }
        int passed = passedChecks != null ? passedChecks : 0;
        return ((double) passed / totalRequirements) * 100;
    }

    @Schema(description = "Fail rate percentage")
    public Double getFailRate() {
        if (totalRequirements == null || totalRequirements == 0) {
            return 0.0;
        }
        int failed = failedChecks != null ? failedChecks : 0;
        return ((double) failed / totalRequirements) * 100;
    }

    @Schema(description = "Is compliant")
    public boolean isCompliant() {
        return status == ComplianceStatus.COMPLIANT;
    }

    @Schema(description = "Is at risk")
    public boolean isAtRisk() {
        return status == ComplianceStatus.AT_RISK;
    }

    @Schema(description = "Is non-compliant")
    public boolean isNonCompliant() {
        return status == ComplianceStatus.NON_COMPLIANT;
    }

    @Schema(description = "Has critical issues")
    public boolean hasCriticalIssues() {
        return criticalIssues != null && criticalIssues > 0;
    }

    @Schema(description = "Requires immediate action")
    public boolean requiresImmediateAction() {
        return hasCriticalIssues() || isNonCompliant();
    }

    /**
     * DTO for compliance issues
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Compliance issue details")
    public static class ComplianceIssueDto {

        @Schema(description = "Issue ID")
        private String issueId;

        @Schema(description = "Issue title")
        private String title;

        @Schema(description = "Issue description")
        private String description;

        @Schema(description = "Severity level")
        private IssueSeverity severity;

        @Schema(description = "Category")
        private String category;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        @Schema(description = "Identified date")
        private Instant identifiedDate;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        @Schema(description = "Target resolution date")
        private Instant targetResolutionDate;

        @Schema(description = "Assigned to")
        private String assignedTo;

        @Schema(description = "Issue status")
        private IssueStatus status;

        @Schema(description = "Is critical")
        public boolean isCritical() {
            return severity == IssueSeverity.CRITICAL;
        }

        @Schema(description = "Is open")
        public boolean isOpen() {
            return status == IssueStatus.OPEN || status == IssueStatus.IN_PROGRESS;
        }

        @Schema(description = "Is overdue")
        public boolean isOverdue() {
            return targetResolutionDate != null && Instant.now().isAfter(targetResolutionDate) && isOpen();
        }
    }

    public enum IssueSeverity {
        @Schema(description = "Critical severity")
        CRITICAL,
        @Schema(description = "High severity")
        HIGH,
        @Schema(description = "Medium severity")
        MEDIUM,
        @Schema(description = "Low severity")
        LOW
    }

    public enum IssueStatus {
        @Schema(description = "Open status")
        OPEN,
        @Schema(description = "In progress status")
        IN_PROGRESS,
        @Schema(description = "Resolved status")
        RESOLVED,
        @Schema(description = "Closed status")
        CLOSED,
        @Schema(description = "Escalated status")
        ESCALATED
    }
}
