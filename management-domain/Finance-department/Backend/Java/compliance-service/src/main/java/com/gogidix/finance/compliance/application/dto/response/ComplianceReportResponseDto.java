package com.gogidix.finance.compliance.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Compliance Report Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComplianceReportResponseDto {

    private String id;

    private String reportId;

    private String tenantId;

    private String reportName;

    private String description;

    private ReportTypeDto reportType;

    private ReportStatusDto status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportPeriodStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportPeriodEnd;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant generatedAt;

    private String generatedBy;

    private String generatedByUserId;

    private ReportSummaryDto summary;

    private List<ReportSectionDto> sections;

    private List<String> departmentIds;

    private List<String> costCenterIds;

    private List<String> ruleIds;

    private List<String> checkIds;

    private Map<String, Object> filters;

    private String format;

    private String fileUrl;

    private Long fileSize;

    private Integer totalRecords;

    private Integer compliantCount;

    private Integer nonCompliantCount;

    private Integer warningCount;

    private Integer notApplicableCount;

    private Double compliancePercentage;

    private String complianceGrade;

    private List<ComplianceMetricDto> metrics;

    private List<String> tags;

    private String notes;

    private String correlationId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant expiresAt;

    private Boolean isArchived;

    private List<ViolationSummaryDto> violationSummaries;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum ReportTypeDto {
        DAILY_SUMMARY,
        WEEKLY_SUMMARY,
        MONTHLY_SUMMARY,
        QUARTERLY_SUMMARY,
        ANNUAL_SUMMARY,
        DEPARTMENT,
        COST_CENTER,
        RULE_SPECIFIC,
        VIOLATION_DETAILS,
        AUDIT_TRAIL,
        CUSTOM
    }

    public enum ReportStatusDto {
        GENERATING,
        COMPLETED,
        FAILED,
        SCHEDULED,
        EXPIRED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReportSummaryDto {
        private String title;
        private String description;
        private Map<String, Object> data;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReportSectionDto {
        private String sectionId;
        private String title;
        private String description;
        private Integer order;
        private Map<String, Object> content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ComplianceMetricDto {
        private String metricName;
        private String metricType;
        private Double value;
        private String targetValue;
        private String status;
        private String trend;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ViolationSummaryDto {
        private String ruleId;
        private String ruleName;
        private Long violationCount;
        private String severity;
        private String topDepartment;
        private Double avgVariance;
    }
}
