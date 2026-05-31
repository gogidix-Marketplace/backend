package com.gogidix.finance.compliance.infrastructure.persistence.mongodb;

import com.gogidix.finance.compliance.domain.model.ComplianceReport;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * MongoDB document entity for storing ComplianceReport domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "compliance_reports")
public class ComplianceReportEntity {

    @Id
    private String id;

    @Indexed
    @Field("report_id")
    private String reportId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("report_name")
    private String reportName;

    @Field("description")
    private String description;

    @Indexed
    @Field("report_type")
    private String reportType;

    @Indexed
    @Field("status")
    private String status;

    @Field("report_period_start")
    private LocalDate reportPeriodStart;

    @Field("report_period_end")
    private LocalDate reportPeriodEnd;

    @Field("generated_at")
    private Instant generatedAt;

    @Field("generated_by")
    private String generatedBy;

    @Field("generated_by_user_id")
    private String generatedByUserId;

    @Field("summary")
    private ComplianceReport.ReportSummary summary;

    @Field("sections")
    private List<ComplianceReport.ReportSection> sections;

    @Field("department_ids")
    private List<String> departmentIds;

    @Field("cost_center_ids")
    private List<String> costCenterIds;

    @Field("rule_ids")
    private List<String> ruleIds;

    @Field("check_ids")
    private List<String> checkIds;

    @Field("filters")
    private Map<String, Object> filters;

    @Field("format")
    private String format;

    @Field("file_url")
    private String fileUrl;

    @Field("file_size")
    private Long fileSize;

    @Field("total_records")
    private Integer totalRecords;

    @Field("compliant_count")
    private Integer compliantCount;

    @Field("non_compliant_count")
    private Integer nonCompliantCount;

    @Field("warning_count")
    private Integer warningCount;

    @Field("not_applicable_count")
    private Integer notApplicableCount;

    @Field("compliance_percentage")
    private Double compliancePercentage;

    @Field("metrics")
    private List<ComplianceReport.ComplianceMetric> metrics;

    @Field("tags")
    private List<String> tags;

    @Field("notes")
    private String notes;

    @Field("correlation_id")
    private String correlationId;

    @Field("expires_at")
    private Instant expiresAt;

    @Field("is_archived")
    private Boolean isArchived;

    @Field("violation_summaries")
    private List<ComplianceReport.ReportViolationSummary> violationSummaries;

    // Default constructor for MongoDB
    public ComplianceReportEntity() {
    }

    // Constructor from domain model
    public ComplianceReportEntity(ComplianceReport report) {
        this.id = report.getReportId(); // Use reportId as the MongoDB _id
        this.reportId = report.getReportId();
        this.tenantId = report.getTenantId();
        this.reportName = report.getReportName();
        this.description = report.getDescription();
        this.reportType = report.getReportType() != null ? report.getReportType().name() : null;
        this.status = report.getStatus() != null ? report.getStatus().name() : null;
        this.reportPeriodStart = report.getReportPeriodStart();
        this.reportPeriodEnd = report.getReportPeriodEnd();
        this.generatedAt = report.getGeneratedAt();
        this.generatedBy = report.getGeneratedBy();
        this.generatedByUserId = report.getGeneratedByUserId();
        this.summary = report.getSummary();
        this.sections = report.getSections();
        this.departmentIds = report.getDepartmentIds();
        this.costCenterIds = report.getCostCenterIds();
        this.ruleIds = report.getRuleIds();
        this.checkIds = report.getCheckIds();
        this.filters = report.getFilters();
        this.format = report.getFormat();
        this.fileUrl = report.getFileUrl();
        this.fileSize = report.getFileSize();
        this.totalRecords = report.getTotalRecords();
        this.compliantCount = report.getCompliantCount();
        this.nonCompliantCount = report.getNonCompliantCount();
        this.warningCount = report.getWarningCount();
        this.notApplicableCount = report.getNotApplicableCount();
        this.compliancePercentage = report.getCompliancePercentage();
        this.metrics = report.getMetrics();
        this.tags = report.getTags();
        this.notes = report.getNotes();
        this.correlationId = report.getCorrelationId();
        this.expiresAt = report.getExpiresAt();
        this.isArchived = report.getIsArchived();
        this.violationSummaries = report.getViolationSummaries();
    }

    public ComplianceReport toDomainModel() {
        return ComplianceReport.builder()
            .reportId(reportId)
            .tenantId(tenantId)
            .reportName(reportName)
            .description(description)
            .reportType(reportType != null ? ComplianceReport.ReportType.valueOf(reportType) : null)
            .status(status != null ? ComplianceReport.ReportStatus.valueOf(status) : null)
            .reportPeriodStart(reportPeriodStart)
            .reportPeriodEnd(reportPeriodEnd)
            .generatedAt(generatedAt)
            .generatedBy(generatedBy)
            .generatedByUserId(generatedByUserId)
            .summary(summary)
            .sections(sections)
            .departmentIds(departmentIds)
            .costCenterIds(costCenterIds)
            .ruleIds(ruleIds)
            .checkIds(checkIds)
            .filters(filters)
            .format(format)
            .fileUrl(fileUrl)
            .fileSize(fileSize)
            .totalRecords(totalRecords)
            .compliantCount(compliantCount)
            .nonCompliantCount(nonCompliantCount)
            .warningCount(warningCount)
            .notApplicableCount(notApplicableCount)
            .compliancePercentage(compliancePercentage)
            .metrics(metrics)
            .tags(tags)
            .notes(notes)
            .correlationId(correlationId)
            .expiresAt(expiresAt)
            .isArchived(isArchived)
            .violationSummaries(violationSummaries)
            .build();
    }

    // Getters and setters for MongoDB
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getReportPeriodStart() {
        return reportPeriodStart;
    }

    public void setReportPeriodStart(LocalDate reportPeriodStart) {
        this.reportPeriodStart = reportPeriodStart;
    }

    public LocalDate getReportPeriodEnd() {
        return reportPeriodEnd;
    }

    public void setReportPeriodEnd(LocalDate reportPeriodEnd) {
        this.reportPeriodEnd = reportPeriodEnd;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public String getGeneratedByUserId() {
        return generatedByUserId;
    }

    public void setGeneratedByUserId(String generatedByUserId) {
        this.generatedByUserId = generatedByUserId;
    }

    public ComplianceReport.ReportSummary getSummary() {
        return summary;
    }

    public void setSummary(ComplianceReport.ReportSummary summary) {
        this.summary = summary;
    }

    public List<ComplianceReport.ReportSection> getSections() {
        return sections;
    }

    public void setSections(List<ComplianceReport.ReportSection> sections) {
        this.sections = sections;
    }

    public List<String> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<String> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public List<String> getCostCenterIds() {
        return costCenterIds;
    }

    public void setCostCenterIds(List<String> costCenterIds) {
        this.costCenterIds = costCenterIds;
    }

    public List<String> getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(List<String> ruleIds) {
        this.ruleIds = ruleIds;
    }

    public List<String> getCheckIds() {
        return checkIds;
    }

    public void setCheckIds(List<String> checkIds) {
        this.checkIds = checkIds;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getCompliantCount() {
        return compliantCount;
    }

    public void setCompliantCount(Integer compliantCount) {
        this.compliantCount = compliantCount;
    }

    public Integer getNonCompliantCount() {
        return nonCompliantCount;
    }

    public void setNonCompliantCount(Integer nonCompliantCount) {
        this.nonCompliantCount = nonCompliantCount;
    }

    public Integer getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(Integer warningCount) {
        this.warningCount = warningCount;
    }

    public Integer getNotApplicableCount() {
        return notApplicableCount;
    }

    public void setNotApplicableCount(Integer notApplicableCount) {
        this.notApplicableCount = notApplicableCount;
    }

    public Double getCompliancePercentage() {
        return compliancePercentage;
    }

    public void setCompliancePercentage(Double compliancePercentage) {
        this.compliancePercentage = compliancePercentage;
    }

    public List<ComplianceReport.ComplianceMetric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<ComplianceReport.ComplianceMetric> metrics) {
        this.metrics = metrics;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public List<ComplianceReport.ReportViolationSummary> getViolationSummaries() {
        return violationSummaries;
    }

    public void setViolationSummaries(List<ComplianceReport.ReportViolationSummary> violationSummaries) {
        this.violationSummaries = violationSummaries;
    }
}
