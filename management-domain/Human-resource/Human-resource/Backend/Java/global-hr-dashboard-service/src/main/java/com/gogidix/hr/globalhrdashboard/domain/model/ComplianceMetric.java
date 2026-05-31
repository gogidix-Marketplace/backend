package com.gogidix.hr.globalhrdashboard.domain.model;

import com.gogidix.hr.globalhrdashboard.shared.base.BaseEntity;
import com.gogidix.hr.globalhrdashboard.shared.exception.ValidationException;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Domain Entity - Compliance Metric
 * Tracks HR compliance metrics at country level
 */
@Document(collection = "compliance_metrics")
public class ComplianceMetric extends BaseEntity {

    @Indexed
    private String countryCode;

    @Indexed
    private String countryName;

    @Indexed
    private String regionCode;

    @Indexed
    private Double complianceScore;

    @Indexed
    private Integer totalRequirements;

    @Indexed
    private Integer passedChecks;

    @Indexed
    private Integer failedChecks;

    @Indexed
    private Integer criticalIssues;

    @Indexed
    private String period;

    @Indexed
    private ComplianceStatus status;

    private List<ComplianceIssue> issues;

    private List<String> pendingActions;

    @Indexed
    private Instant lastAssessed;

    private String assessedBy;

    private String notes;

    private Boolean isActive;

    protected ComplianceMetric() {
        super();
        this.issues = new ArrayList<>();
        this.pendingActions = new ArrayList<>();
        this.isActive = true;
    }

    public ComplianceMetric(String tenantId, String countryCode, String countryName,
                             String regionCode, Double complianceScore, String period) {
        super(tenantId);
        setCountryCode(countryCode);
        setCountryName(countryName);
        setRegionCode(regionCode);
        setComplianceScore(complianceScore);
        setPeriod(period);
        this.totalRequirements = 0;
        this.passedChecks = 0;
        this.failedChecks = 0;
        this.criticalIssues = 0;
        this.issues = new ArrayList<>();
        this.pendingActions = new ArrayList<>();
        this.isActive = true;
        this.lastAssessed = Instant.now();
    }

    public void setCountryCode(String countryCode) {
        if (countryCode == null || countryCode.isBlank()) {
            throw new ValidationException("countryCode", "Country code cannot be null or blank");
        }
        if (countryCode.length() != 2) {
            throw new ValidationException("countryCode", "Country code must be 2 characters (ISO 3166-1 alpha-2)");
        }
        this.countryCode = countryCode.toUpperCase();
        updateTimestamp();
    }

    public void setCountryName(String countryName) {
        if (countryName == null || countryName.isBlank()) {
            throw new ValidationException("countryName", "Country name cannot be null or blank");
        }
        this.countryName = countryName;
        updateTimestamp();
    }

    public void setRegionCode(String regionCode) {
        if (regionCode == null || regionCode.isBlank()) {
            throw new ValidationException("regionCode", "Region code cannot be null or blank");
        }
        this.regionCode = regionCode;
        updateTimestamp();
    }

    public void setComplianceScore(Double complianceScore) {
        if (complianceScore == null || complianceScore < 0 || complianceScore > 100) {
            throw new ValidationException("complianceScore", "Compliance score must be between 0 and 100");
        }
        this.complianceScore = complianceScore;
        this.status = ComplianceStatus.fromScore(complianceScore);
        this.lastAssessed = Instant.now();
        updateTimestamp();
    }

    public void setTotalRequirements(Integer totalRequirements) {
        if (totalRequirements != null && totalRequirements < 0) {
            throw new ValidationException("totalRequirements", "Total requirements must be non-negative");
        }
        this.totalRequirements = totalRequirements;
        updateTimestamp();
    }

    public void setPassedChecks(Integer passedChecks) {
        if (passedChecks != null && passedChecks < 0) {
            throw new ValidationException("passedChecks", "Passed checks must be non-negative");
        }
        this.passedChecks = passedChecks;
        updateTimestamp();
    }

    public void setFailedChecks(Integer failedChecks) {
        if (failedChecks != null && failedChecks < 0) {
            throw new ValidationException("failedChecks", "Failed checks must be non-negative");
        }
        this.failedChecks = failedChecks;
        updateTimestamp();
    }

    public void setCriticalIssues(Integer criticalIssues) {
        if (criticalIssues != null && criticalIssues < 0) {
            throw new ValidationException("criticalIssues", "Critical issues must be non-negative");
        }
        this.criticalIssues = criticalIssues;
        updateTimestamp();
    }

    public void setPeriod(String period) {
        if (period == null || period.isBlank()) {
            throw new ValidationException("period", "Period cannot be null or blank");
        }
        this.period = period;
        updateTimestamp();
    }

    public void setStatus(ComplianceStatus status) {
        this.status = Objects.requireNonNull(status, "status is required");
        updateTimestamp();
    }

    public void setIssues(List<ComplianceIssue> issues) {
        this.issues = issues != null ? issues : new ArrayList<>();
        updateTimestamp();
    }

    public void setPendingActions(List<String> pendingActions) {
        this.pendingActions = pendingActions != null ? pendingActions : new ArrayList<>();
        updateTimestamp();
    }

    public void setLastAssessed(Instant lastAssessed) {
        this.lastAssessed = lastAssessed;
    }

    public void setAssessedBy(String assessedBy) {
        this.assessedBy = assessedBy;
        updateTimestamp();
    }

    public void setNotes(String notes) {
        this.notes = notes;
        updateTimestamp();
    }

    public void setActive(Boolean active) {
        this.isActive = active;
        updateTimestamp();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public Double getComplianceScore() {
        return complianceScore;
    }

    public Integer getTotalRequirements() {
        return totalRequirements;
    }

    public Integer getPassedChecks() {
        return passedChecks;
    }

    public Integer getFailedChecks() {
        return failedChecks;
    }

    public Integer getCriticalIssues() {
        return criticalIssues;
    }

    public String getPeriod() {
        return period;
    }

    public ComplianceStatus getStatus() {
        return status;
    }

    public List<ComplianceIssue> getIssues() {
        return issues;
    }

    public List<String> getPendingActions() {
        return pendingActions;
    }

    public Instant getLastAssessed() {
        return lastAssessed;
    }

    public String getAssessedBy() {
        return assessedBy;
    }

    public String getNotes() {
        return notes;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Double getPassRate() {
        if (totalRequirements == null || totalRequirements == 0) {
            return 0.0;
        }
        int passed = passedChecks != null ? passedChecks : 0;
        return ((double) passed / totalRequirements) * 100;
    }

    public Double getFailRate() {
        if (totalRequirements == null || totalRequirements == 0) {
            return 0.0;
        }
        int failed = failedChecks != null ? failedChecks : 0;
        return ((double) failed / totalRequirements) * 100;
    }

    public boolean isCompliant() {
        return status == ComplianceStatus.COMPLIANT;
    }

    public boolean isAtRisk() {
        return status == ComplianceStatus.AT_RISK;
    }

    public boolean isNonCompliant() {
        return status == ComplianceStatus.NON_COMPLIANT;
    }

    public boolean hasCriticalIssues() {
        return criticalIssues != null && criticalIssues > 0;
    }

    public boolean requiresImmediateAction() {
        return hasCriticalIssues() || isNonCompliant();
    }

    public void addIssue(ComplianceIssue issue) {
        if (this.issues == null) {
            this.issues = new ArrayList<>();
        }
        this.issues.add(issue);
        if (issue.isCritical()) {
            this.criticalIssues = (this.criticalIssues != null ? this.criticalIssues : 0) + 1;
        }
        updateTimestamp();
    }

    public void addPendingAction(String action) {
        if (this.pendingActions == null) {
            this.pendingActions = new ArrayList<>();
        }
        this.pendingActions.add(action);
        updateTimestamp();
    }

    public void removePendingAction(String action) {
        if (this.pendingActions != null) {
            this.pendingActions.remove(action);
            updateTimestamp();
        }
    }

    public void recordAssessment(String assessedBy) {
        this.lastAssessed = Instant.now();
        this.assessedBy = assessedBy;
        updateTimestamp(assessedBy);
    }

    public void recalculateScore() {
        if (totalRequirements != null && totalRequirements > 0) {
            int passed = passedChecks != null ? passedChecks : 0;
            double score = ((double) passed / totalRequirements) * 100;
            setComplianceScore(score);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ComplianceMetric that = (ComplianceMetric) o;
        return Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), countryCode, period);
    }

    @Override
    public String toString() {
        return "ComplianceMetric{" +
                "id='" + id + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", complianceScore=" + complianceScore +
                ", status=" + status +
                ", period='" + period + '\'' +
                '}';
    }

    /**
     * Embedded class for compliance issues
     */
    public static class ComplianceIssue {
        private String issueId;
        private String title;
        private String description;
        private IssueSeverity severity;
        private String category;
        private Instant identifiedDate;
        private Instant targetResolutionDate;
        private String assignedTo;
        private IssueStatus status;

        public enum IssueSeverity {
            CRITICAL, HIGH, MEDIUM, LOW
        }

        public enum IssueStatus {
            OPEN, IN_PROGRESS, RESOLVED, CLOSED, ESCALATED
        }

        public ComplianceIssue() {
        }

        public ComplianceIssue(String issueId, String title, IssueSeverity severity) {
            this.issueId = issueId;
            this.title = title;
            this.severity = severity;
            this.status = IssueStatus.OPEN;
            this.identifiedDate = Instant.now();
        }

        public String getIssueId() {
            return issueId;
        }

        public void setIssueId(String issueId) {
            this.issueId = issueId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public IssueSeverity getSeverity() {
            return severity;
        }

        public void setSeverity(IssueSeverity severity) {
            this.severity = severity;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Instant getIdentifiedDate() {
            return identifiedDate;
        }

        public void setIdentifiedDate(Instant identifiedDate) {
            this.identifiedDate = identifiedDate;
        }

        public Instant getTargetResolutionDate() {
            return targetResolutionDate;
        }

        public void setTargetResolutionDate(Instant targetResolutionDate) {
            this.targetResolutionDate = targetResolutionDate;
        }

        public String getAssignedTo() {
            return assignedTo;
        }

        public void setAssignedTo(String assignedTo) {
            this.assignedTo = assignedTo;
        }

        public IssueStatus getStatus() {
            return status;
        }

        public void setStatus(IssueStatus status) {
            this.status = status;
        }

        public boolean isCritical() {
            return severity == IssueSeverity.CRITICAL;
        }

        public boolean isOpen() {
            return status == IssueStatus.OPEN || status == IssueStatus.IN_PROGRESS;
        }

        public boolean isOverdue() {
            return targetResolutionDate != null && Instant.now().isAfter(targetResolutionDate) && isOpen();
        }
    }
}
