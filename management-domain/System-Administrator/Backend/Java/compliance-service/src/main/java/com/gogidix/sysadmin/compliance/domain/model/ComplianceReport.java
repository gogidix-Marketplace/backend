package com.gogidix.sysadmin.compliance.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "compliance_reports")
public class ComplianceReport {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    private String reportNumber;
    private String title;
    private ReportType reportType;
    private ComplianceStandard standard;
    private ReportStatus status;
    private Instant reportingPeriodStart;
    private Instant reportingPeriodEnd;
    private String generatedBy;
    private String reviewedBy;
    private String approvedBy;
    private Instant generatedAt;
    private Instant reviewedAt;
    private Instant approvedAt;
    private List<ComplianceFinding> findings;
    private ComplianceScore overallScore;
    private Map<String, ComplianceScore> categoryScores;
    private Integer totalControls;
    private Integer passedControls;
    private Integer failedControls;
    private Integer skippedControls;
    private List<String> recommendations;
    private Map<String, Object> evidence;
    private String executiveSummary;
    private Instant createdAt;
    private Instant updatedAt;

    public enum ReportType {
        ANNUAL, QUARTERLY, MONTHLY, ON_DEMAND, AUDIT, ASSESSMENT
    }

    public enum ReportStatus {
        DRAFT, UNDER_REVIEW, APPROVED, PUBLISHED, ARCHIVED
    }

    public enum ComplianceStandard {
        ISO_27001, SOC_2, HIPAA, PCI_DSS, GDPR, NIST_800_53,
        CIS_CONTROLS, COBIT, ITIL, CUSTOM
    }

    public static class ComplianceFinding {
        private String controlId;
        private String controlTitle;
        private FindingSeverity severity;
        private String description;
        private String location;
        private List<String> affectedResources;
        private String remediation;
        private String assignedTo;
        private Instant targetDate;
        private FindingStatus status;

        public String getControlId() { return controlId; }
        public void setControlId(String controlId) { this.controlId = controlId; }
        public String getControlTitle() { return controlTitle; }
        public void setControlTitle(String controlTitle) { this.controlTitle = controlTitle; }
        public FindingSeverity getSeverity() { return severity; }
        public void setSeverity(FindingSeverity severity) { this.severity = severity; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public List<String> getAffectedResources() { return affectedResources; }
        public void setAffectedResources(List<String> affectedResources) { this.affectedResources = affectedResources; }
        public String getRemediation() { return remediation; }
        public void setRemediation(String remediation) { this.remediation = remediation; }
        public String getAssignedTo() { return assignedTo; }
        public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
        public Instant getTargetDate() { return targetDate; }
        public void setTargetDate(Instant targetDate) { this.targetDate = targetDate; }
        public FindingStatus getStatus() { return status; }
        public void setStatus(FindingStatus status) { this.status = status; }
    }

    public enum FindingSeverity {
        CRITICAL, HIGH, MEDIUM, LOW, INFO
    }

    public enum FindingStatus {
        OPEN, IN_PROGRESS, REMEDIATED, ACCEPTED_RISK, DEFERRED
    }

    public static class ComplianceScore {
        private Double score;
        private String grade;
        private Integer totalPoints;
        private Integer earnedPoints;

        public Double getScore() { return score; }
        public void setScore(Double score) { this.score = score; }
        public String getGrade() { return grade; }
        public void setGrade(String grade) { this.grade = grade; }
        public Integer getTotalPoints() { return totalPoints; }
        public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
        public Integer getEarnedPoints() { return earnedPoints; }
        public void setEarnedPoints(Integer earnedPoints) { this.earnedPoints = earnedPoints; }
    }

    public ComplianceReport() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = ReportStatus.DRAFT;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getReportNumber() { return reportNumber; }
    public void setReportNumber(String reportNumber) { this.reportNumber = reportNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public ReportType getReportType() { return reportType; }
    public void setReportType(ReportType reportType) { this.reportType = reportType; }

    public ComplianceStandard getStandard() { return standard; }
    public void setStandard(ComplianceStandard standard) { this.standard = standard; }

    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }

    public Instant getReportingPeriodStart() { return reportingPeriodStart; }
    public void setReportingPeriodStart(Instant reportingPeriodStart) { this.reportingPeriodStart = reportingPeriodStart; }

    public Instant getReportingPeriodEnd() { return reportingPeriodEnd; }
    public void setReportingPeriodEnd(Instant reportingPeriodEnd) { this.reportingPeriodEnd = reportingPeriodEnd; }

    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }

    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }

    public Instant getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(Instant generatedAt) { this.generatedAt = generatedAt; }

    public Instant getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(Instant reviewedAt) { this.reviewedAt = reviewedAt; }

    public Instant getApprovedAt() { return approvedAt; }
    public void setApprovedAt(Instant approvedAt) { this.approvedAt = approvedAt; }

    public List<ComplianceFinding> getFindings() { return findings; }
    public void setFindings(List<ComplianceFinding> findings) { this.findings = findings; }

    public ComplianceScore getOverallScore() { return overallScore; }
    public void setOverallScore(ComplianceScore overallScore) { this.overallScore = overallScore; }

    public Map<String, ComplianceScore> getCategoryScores() { return categoryScores; }
    public void setCategoryScores(Map<String, ComplianceScore> categoryScores) { this.categoryScores = categoryScores; }

    public Integer getTotalControls() { return totalControls; }
    public void setTotalControls(Integer totalControls) { this.totalControls = totalControls; }

    public Integer getPassedControls() { return passedControls; }
    public void setPassedControls(Integer passedControls) { this.passedControls = passedControls; }

    public Integer getFailedControls() { return failedControls; }
    public void setFailedControls(Integer failedControls) { this.failedControls = failedControls; }

    public Integer getSkippedControls() { return skippedControls; }
    public void setSkippedControls(Integer skippedControls) { this.skippedControls = skippedControls; }

    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }

    public Map<String, Object> getEvidence() { return evidence; }
    public void setEvidence(Map<String, Object> evidence) { this.evidence = evidence; }

    public String getExecutiveSummary() { return executiveSummary; }
    public void setExecutiveSummary(String executiveSummary) { this.executiveSummary = executiveSummary; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public void submitForReview() {
        this.status = ReportStatus.UNDER_REVIEW;
        this.updatedAt = Instant.now();
    }

    public void approve(String approvedBy) {
        this.status = ReportStatus.APPROVED;
        this.approvedBy = approvedBy;
        this.approvedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void publish() {
        this.status = ReportStatus.PUBLISHED;
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplianceReport that = (ComplianceReport) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
