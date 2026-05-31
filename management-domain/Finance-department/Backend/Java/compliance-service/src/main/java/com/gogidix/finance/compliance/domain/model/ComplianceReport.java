package com.gogidix.finance.compliance.domain.model;

import com.gogidix.finance.compliance.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Compliance Report Domain Entity
 * Aggregates compliance check results for reporting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "compliance_reports")
public class ComplianceReport extends BaseEntity {

    private String reportId;

    private String tenantId;

    private String reportName;

    private String description;

    private ReportType reportType;

    private ReportStatus status;

    private LocalDate reportPeriodStart;

    private LocalDate reportPeriodEnd;

    private Instant generatedAt;

    private String generatedBy;

    private String generatedByUserId;

    private ReportSummary summary;

    private List<ReportSection> sections;

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

    private List<ComplianceMetric> metrics;

    private List<String> tags;

    private String notes;

    private String correlationId;

    private Instant expiresAt;

    private Boolean isArchived;

    @Builder.Default
    private List<ReportViolationSummary> violationSummaries = new ArrayList<>();

    public enum ReportType {
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

    public enum ReportStatus {
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
    public static class ReportSummary {
        private String title;
        private String description;
        private Map<String, Object> data;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportSection {
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
    public static class ComplianceMetric {
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
    public static class ReportViolationSummary {
        private String ruleId;
        private String ruleName;
        private Long violationCount;
        private String severity;
        private String topDepartment;
        private Double avgVariance;
    }

    /**
     * Creates a new compliance report
     */
    public static ComplianceReport create(String tenantId, String reportName, ReportType reportType,
                                           LocalDate periodStart, LocalDate periodEnd,
                                           String generatedByUserId) {
        ComplianceReport report = ComplianceReport.builder()
            .tenantId(tenantId)
            .reportName(reportName)
            .reportType(reportType)
            .reportPeriodStart(periodStart)
            .reportPeriodEnd(periodEnd)
            .status(ReportStatus.GENERATING)
            .generatedByUserId(generatedByUserId)
            .sections(new ArrayList<>())
            .departmentIds(new ArrayList<>())
            .costCenterIds(new ArrayList<>())
            .ruleIds(new ArrayList<>())
            .checkIds(new ArrayList<>())
            .metrics(new ArrayList<>())
            .violationSummaries(new ArrayList<>())
            .tags(new ArrayList<>())
            .build();

        return report;
    }

    /**
     * Marks the report as completed
     */
    public void markAsCompleted(String fileUrl, Long fileSize) {
        if (this.status != ReportStatus.GENERATING && this.status != ReportStatus.SCHEDULED) {
            throw new IllegalStateException("Can only complete generating or scheduled reports");
        }

        this.status = ReportStatus.COMPLETED;
        this.generatedAt = Instant.now();
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;

        calculateCompliancePercentage();
    }

    /**
     * Marks the report as failed
     */
    public void markAsFailed(String reason) {
        if (this.status != ReportStatus.GENERATING) {
            throw new IllegalStateException("Can only fail generating reports");
        }

        this.status = ReportStatus.FAILED;
        this.notes = reason;
    }

    /**
     * Updates summary statistics
     */
    public void updateSummary(Integer total, Integer compliant, Integer nonCompliant,
                               Integer warning, Integer notApplicable) {
        this.totalRecords = total;
        this.compliantCount = compliant;
        this.nonCompliantCount = nonCompliant;
        this.warningCount = warning;
        this.notApplicableCount = notApplicable;

        calculateCompliancePercentage();
    }

    /**
     * Adds a section to the report
     */
    public void addSection(String title, String description, Integer order, Map<String, Object> content) {
        if (this.sections == null) {
            this.sections = new ArrayList<>();
        }

        ReportSection section = ReportSection.builder()
            .sectionId(java.util.UUID.randomUUID().toString())
            .title(title)
            .description(description)
            .order(order)
            .content(content)
            .build();

        this.sections.add(section);
    }

    /**
     * Adds a metric to the report
     */
    public void addMetric(String metricName, String metricType, Double value,
                           String targetValue, String status, String trend) {
        if (this.metrics == null) {
            this.metrics = new ArrayList<>();
        }

        ComplianceMetric metric = ComplianceMetric.builder()
            .metricName(metricName)
            .metricType(metricType)
            .value(value)
            .targetValue(targetValue)
            .status(status)
            .trend(trend)
            .build();

        this.metrics.add(metric);
    }

    /**
     * Adds a violation summary
     */
    public void addViolationSummary(String ruleId, String ruleName, Long violationCount,
                                      String severity, String topDepartment, Double avgVariance) {
        if (this.violationSummaries == null) {
            this.violationSummaries = new ArrayList<>();
        }

        ReportViolationSummary summary = ReportViolationSummary.builder()
            .ruleId(ruleId)
            .ruleName(ruleName)
            .violationCount(violationCount)
            .severity(severity)
            .topDepartment(topDepartment)
            .avgVariance(avgVariance)
            .build();

        this.violationSummaries.add(summary);
    }

    /**
     * Sets filters for the report
     */
    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    /**
     * Adds a department to the report scope
     */
    public void addDepartment(String departmentId) {
        if (this.departmentIds == null) {
            this.departmentIds = new ArrayList<>();
        }
        if (!this.departmentIds.contains(departmentId)) {
            this.departmentIds.add(departmentId);
        }
    }

    /**
     * Adds a cost center to the report scope
     */
    public void addCostCenter(String costCenterId) {
        if (this.costCenterIds == null) {
            this.costCenterIds = new ArrayList<>();
        }
        if (!this.costCenterIds.contains(costCenterId)) {
            this.costCenterIds.add(costCenterId);
        }
    }

    /**
     * Adds a rule to the report scope
     */
    public void addRule(String ruleId) {
        if (this.ruleIds == null) {
            this.ruleIds = new ArrayList<>();
        }
        if (!this.ruleIds.contains(ruleId)) {
            this.ruleIds.add(ruleId);
        }
    }

    /**
     * Adds a check to the report
     */
    public void addCheck(String checkId) {
        if (this.checkIds == null) {
            this.checkIds = new ArrayList<>();
        }
        if (!this.checkIds.contains(checkId)) {
            this.checkIds.add(checkId);
        }
    }

    /**
     * Sets expiration date for the report
     */
    public void setExpiration(Instant expiresAt) {
        this.expiresAt = expiresAt;
        if (Instant.now().isAfter(expiresAt)) {
            this.status = ReportStatus.EXPIRED;
        }
    }

    /**
     * Archives the report
     */
    public void archive() {
        this.isArchived = true;
    }

    /**
     * Unarchives the report
     */
    public void unarchive() {
        this.isArchived = false;
    }

    /**
     * Checks if report is expired
     */
    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }

    /**
     * Adds a tag to the report
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Removes a tag from the report
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Calculates compliance percentage
     */
    private void calculateCompliancePercentage() {
        if (totalRecords != null && totalRecords > 0) {
            Integer applicableRecords = totalRecords - notApplicableCount;
            if (applicableRecords > 0) {
                this.compliancePercentage = (compliantCount.doubleValue() / applicableRecords.doubleValue()) * 100;
            } else {
                this.compliancePercentage = 0.0;
            }
        }
    }

    /**
     * Gets compliance grade
     */
    public String getComplianceGrade() {
        if (compliancePercentage == null) {
            return "N/A";
        }

        if (compliancePercentage >= 95) {
            return "A";
        } else if (compliancePercentage >= 85) {
            return "B";
        } else if (compliancePercentage >= 70) {
            return "C";
        } else if (compliancePercentage >= 50) {
            return "D";
        } else {
            return "F";
        }
    }
}
