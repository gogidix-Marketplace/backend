package com.gogidix.hr.globalcompliance.infrastructure.persistence.mongodb.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * MongoDB Entity for ComplianceReport
 * Maps domain model to database representation
 */
@Document(collection = "compliance_reports")
public class ComplianceReportEntity {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("report_id")
    private String reportId;

    @Field("report_number")
    private String reportNumber;

    @Field("report_type")
    private String reportType;

    @Field("country_code")
    private String countryCode;

    @Field("period_start")
    private LocalDate periodStart;

    @Field("period_end")
    private LocalDate periodEnd;

    @Field("status")
    private String status;

    @Field("total_requirements")
    private Integer totalRequirements;

    @Field("passed_checks")
    private Integer passedChecks;

    @Field("failed_checks")
    private Integer failedChecks;

    @Field("pending_checks")
    private Integer pendingChecks;

    @Field("compliance_score")
    private Double complianceScore;

    @Field("critical_issues")
    private List<String> criticalIssues;

    @Field("recommendations")
    private List<String> recommendations;

    @Field("prepared_by")
    private String preparedBy;

    @Field("prepared_by_name")
    private String preparedByName;

    @Field("approved_by")
    private String approvedBy;

    @Field("approved_by_name")
    private String approvedByName;

    @Field("submitted_date")
    private LocalDate submittedDate;

    @Field("approved_date")
    private LocalDate approvedDate;

    @Field("summary")
    private String summary;

    @Field("department")
    private String department;

    @Field("region")
    private String region;

    @Field("included_requirements")
    private List<String> includedRequirements;

    @Field("included_checks")
    private List<String> includedChecks;

    @Field("notes")
    private String notes;

    @Field("rejection_reason")
    private String rejectionReason;

    @Field("domain_events")
    private List<Object> domainEvents;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("updated_by")
    private String updatedBy;

    // Default constructor for MongoDB
    public ComplianceReportEntity() {
    }

    // Builder pattern for entity creation
    private ComplianceReportEntity(Builder builder) {
        this.id = builder.id;
        this.tenantId = builder.tenantId;
        this.reportId = builder.reportId;
        this.reportNumber = builder.reportNumber;
        this.reportType = builder.reportType;
        this.countryCode = builder.countryCode;
        this.periodStart = builder.periodStart;
        this.periodEnd = builder.periodEnd;
        this.status = builder.status;
        this.totalRequirements = builder.totalRequirements;
        this.passedChecks = builder.passedChecks;
        this.failedChecks = builder.failedChecks;
        this.pendingChecks = builder.pendingChecks;
        this.complianceScore = builder.complianceScore;
        this.criticalIssues = builder.criticalIssues;
        this.recommendations = builder.recommendations;
        this.preparedBy = builder.preparedBy;
        this.preparedByName = builder.preparedByName;
        this.approvedBy = builder.approvedBy;
        this.approvedByName = builder.approvedByName;
        this.submittedDate = builder.submittedDate;
        this.approvedDate = builder.approvedDate;
        this.summary = builder.summary;
        this.department = builder.department;
        this.region = builder.region;
        this.includedRequirements = builder.includedRequirements;
        this.includedChecks = builder.includedChecks;
        this.notes = builder.notes;
        this.rejectionReason = builder.rejectionReason;
        this.domainEvents = builder.domainEvents;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.updatedBy = builder.updatedBy;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getReportId() { return reportId; }
    public String getReportNumber() { return reportNumber; }
    public String getReportType() { return reportType; }
    public String getCountryCode() { return countryCode; }
    public LocalDate getPeriodStart() { return periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
    public String getStatus() { return status; }
    public Integer getTotalRequirements() { return totalRequirements; }
    public Integer getPassedChecks() { return passedChecks; }
    public Integer getFailedChecks() { return failedChecks; }
    public Integer getPendingChecks() { return pendingChecks; }
    public Double getComplianceScore() { return complianceScore; }
    public List<String> getCriticalIssues() { return criticalIssues; }
    public List<String> getRecommendations() { return recommendations; }
    public String getPreparedBy() { return preparedBy; }
    public String getPreparedByName() { return preparedByName; }
    public String getApprovedBy() { return approvedBy; }
    public String getApprovedByName() { return approvedByName; }
    public LocalDate getSubmittedDate() { return submittedDate; }
    public LocalDate getApprovedDate() { return approvedDate; }
    public String getSummary() { return summary; }
    public String getDepartment() { return department; }
    public String getRegion() { return region; }
    public List<String> getIncludedRequirements() { return includedRequirements; }
    public List<String> getIncludedChecks() { return includedChecks; }
    public String getNotes() { return notes; }
    public String getRejectionReason() { return rejectionReason; }
    public List<Object> getDomainEvents() { return domainEvents; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getUpdatedBy() { return updatedBy; }

    // Setters (for MongoDB mapping)
    public void setId(String id) { this.id = id; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public void setReportId(String reportId) { this.reportId = reportId; }
    public void setReportNumber(String reportNumber) { this.reportNumber = reportNumber; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }
    public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalRequirements(Integer totalRequirements) { this.totalRequirements = totalRequirements; }
    public void setPassedChecks(Integer passedChecks) { this.passedChecks = passedChecks; }
    public void setFailedChecks(Integer failedChecks) { this.failedChecks = failedChecks; }
    public void setPendingChecks(Integer pendingChecks) { this.pendingChecks = pendingChecks; }
    public void setComplianceScore(Double complianceScore) { this.complianceScore = complianceScore; }
    public void setCriticalIssues(List<String> criticalIssues) { this.criticalIssues = criticalIssues; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    public void setPreparedBy(String preparedBy) { this.preparedBy = preparedBy; }
    public void setPreparedByName(String preparedByName) { this.preparedByName = preparedByName; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    public void setApprovedByName(String approvedByName) { this.approvedByName = approvedByName; }
    public void setSubmittedDate(LocalDate submittedDate) { this.submittedDate = submittedDate; }
    public void setApprovedDate(LocalDate approvedDate) { this.approvedDate = approvedDate; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setDepartment(String department) { this.department = department; }
    public void setRegion(String region) { this.region = region; }
    public void setIncludedRequirements(List<String> includedRequirements) { this.includedRequirements = includedRequirements; }
    public void setIncludedChecks(List<String> includedChecks) { this.includedChecks = includedChecks; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public void setDomainEvents(List<Object> domainEvents) { this.domainEvents = domainEvents; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplianceReportEntity that = (ComplianceReportEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    public static class Builder {
        private String id;
        private String tenantId;
        private String reportId;
        private String reportNumber;
        private String reportType;
        private String countryCode;
        private LocalDate periodStart;
        private LocalDate periodEnd;
        private String status;
        private Integer totalRequirements;
        private Integer passedChecks;
        private Integer failedChecks;
        private Integer pendingChecks;
        private Double complianceScore;
        private List<String> criticalIssues;
        private List<String> recommendations;
        private String preparedBy;
        private String preparedByName;
        private String approvedBy;
        private String approvedByName;
        private LocalDate submittedDate;
        private LocalDate approvedDate;
        private String summary;
        private String department;
        private String region;
        private List<String> includedRequirements;
        private List<String> includedChecks;
        private String notes;
        private String rejectionReason;
        private List<Object> domainEvents;
        private Instant createdAt;
        private Instant updatedAt;
        private String updatedBy;

        public Builder id(String id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder reportId(String reportId) { this.reportId = reportId; return this; }
        public Builder reportNumber(String reportNumber) { this.reportNumber = reportNumber; return this; }
        public Builder reportType(String reportType) { this.reportType = reportType; return this; }
        public Builder countryCode(String countryCode) { this.countryCode = countryCode; return this; }
        public Builder periodStart(LocalDate periodStart) { this.periodStart = periodStart; return this; }
        public Builder periodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder totalRequirements(Integer totalRequirements) { this.totalRequirements = totalRequirements; return this; }
        public Builder passedChecks(Integer passedChecks) { this.passedChecks = passedChecks; return this; }
        public Builder failedChecks(Integer failedChecks) { this.failedChecks = failedChecks; return this; }
        public Builder pendingChecks(Integer pendingChecks) { this.pendingChecks = pendingChecks; return this; }
        public Builder complianceScore(Double complianceScore) { this.complianceScore = complianceScore; return this; }
        public Builder criticalIssues(List<String> criticalIssues) { this.criticalIssues = criticalIssues; return this; }
        public Builder recommendations(List<String> recommendations) { this.recommendations = recommendations; return this; }
        public Builder preparedBy(String preparedBy) { this.preparedBy = preparedBy; return this; }
        public Builder preparedByName(String preparedByName) { this.preparedByName = preparedByName; return this; }
        public Builder approvedBy(String approvedBy) { this.approvedBy = approvedBy; return this; }
        public Builder approvedByName(String approvedByName) { this.approvedByName = approvedByName; return this; }
        public Builder submittedDate(LocalDate submittedDate) { this.submittedDate = submittedDate; return this; }
        public Builder approvedDate(LocalDate approvedDate) { this.approvedDate = approvedDate; return this; }
        public Builder summary(String summary) { this.summary = summary; return this; }
        public Builder department(String department) { this.department = department; return this; }
        public Builder region(String region) { this.region = region; return this; }
        public Builder includedRequirements(List<String> includedRequirements) { this.includedRequirements = includedRequirements; return this; }
        public Builder includedChecks(List<String> includedChecks) { this.includedChecks = includedChecks; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder rejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; return this; }
        public Builder domainEvents(List<Object> domainEvents) { this.domainEvents = domainEvents; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder updatedBy(String updatedBy) { this.updatedBy = updatedBy; return this; }

        public ComplianceReportEntity build() {
            return new ComplianceReportEntity(this);
        }
    }
}
