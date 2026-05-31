package com.gogidix.hr.globalcompliance.domain.model;

import com.gogidix.hr.globalcompliance.domain.event.ComplianceReportGeneratedEvent;
import com.gogidix.hr.globalcompliance.domain.model.enums.*;
import com.gogidix.hr.globalcompliance.shared.base.BaseEntity;
import com.gogidix.hr.globalcompliance.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Compliance Report Domain Entity
 * Multi-tenant compliance report management
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

    @Builder.Default
    private List<ComplianceReportGeneratedEvent> domainEvents = new ArrayList<>();

    /**
     * Creates a new compliance report
     */
    public static ComplianceReport create(String tenantId, String reportType,
                                           String countryCode, LocalDate periodStart,
                                           LocalDate periodEnd, String preparedBy,
                                           String preparedByName, String department) {
        validatePeriod(periodStart, periodEnd);

        ComplianceReport report = new ComplianceReport();
        report.setTenantId(tenantId);
        report.setReportType(reportType);
        report.setCountryCode(countryCode);
        report.setPeriodStart(periodStart);
        report.setPeriodEnd(periodEnd);
        report.setStatus(ReportStatus.DRAFT.name());
        report.setPreparedBy(preparedBy);
        report.setPreparedByName(preparedByName);
        report.setDepartment(department);
        report.setTotalRequirements(0);
        report.setPassedChecks(0);
        report.setFailedChecks(0);
        report.setPendingChecks(0);
        report.setComplianceScore(0.0);
        report.setCriticalIssues(new ArrayList<>());
        report.setRecommendations(new ArrayList<>());
        report.setIncludedRequirements(new ArrayList<>());
        report.setIncludedChecks(new ArrayList<>());

        report.generateReportNumber();
        report.generateReportId();

        return report;
    }

    /**
     * Sets compliance metrics
     */
    public void setMetrics(Integer totalRequirements, Integer passedChecks,
                           Integer failedChecks, Integer pendingChecks) {
        this.totalRequirements = totalRequirements != null ? totalRequirements : 0;
        this.passedChecks = passedChecks != null ? passedChecks : 0;
        this.failedChecks = failedChecks != null ? failedChecks : 0;
        this.pendingChecks = pendingChecks != null ? pendingChecks : 0;
        calculateComplianceScore();
    }

    /**
     * Calculates compliance score
     */
    public void calculateComplianceScore() {
        int totalChecks = this.passedChecks + this.failedChecks + this.pendingChecks;
        if (totalChecks == 0) {
            this.complianceScore = 0.0;
            return;
        }

        double score = ((double) this.passedChecks / totalChecks) * 100;
        this.complianceScore = Math.round(score * 100.0) / 100.0;
    }

    /**
     * Adds a critical issue
     */
    public void addCriticalIssue(String issue) {
        if (this.criticalIssues == null) {
            this.criticalIssues = new ArrayList<>();
        }
        if (!this.criticalIssues.contains(issue)) {
            this.criticalIssues.add(issue);
        }
    }

    /**
     * Removes a critical issue
     */
    public void removeCriticalIssue(String issue) {
        if (this.criticalIssues != null) {
            this.criticalIssues.remove(issue);
        }
    }

    /**
     * Adds a recommendation
     */
    public void addRecommendation(String recommendation) {
        if (this.recommendations == null) {
            this.recommendations = new ArrayList<>();
        }
        this.recommendations.add(recommendation);
    }

    /**
     * Removes a recommendation
     */
    public void removeRecommendation(String recommendation) {
        if (this.recommendations != null) {
            this.recommendations.remove(recommendation);
        }
    }

    /**
     * Sets the summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Includes a requirement in the report
     */
    public void includeRequirement(String requirementId) {
        if (this.includedRequirements == null) {
            this.includedRequirements = new ArrayList<>();
        }
        if (!this.includedRequirements.contains(requirementId)) {
            this.includedRequirements.add(requirementId);
            this.totalRequirements = this.includedRequirements.size();
        }
    }

    /**
     * Includes a check in the report
     */
    public void includeCheck(String checkId) {
        if (this.includedChecks == null) {
            this.includedChecks = new ArrayList<>();
        }
        if (!this.includedChecks.contains(checkId)) {
            this.includedChecks.add(checkId);
        }
    }

    /**
     * Submits the report for approval
     */
    public void submit() {
        if (!this.status.equals(ReportStatus.DRAFT.name())) {
            throw new IllegalStateException("Can only submit draft reports");
        }

        if (this.totalRequirements == 0) {
            throw new ValidationException("totalRequirements", "Report must include at least one requirement");
        }

        this.status = ReportStatus.SUBMITTED.name();
        this.submittedDate = LocalDate.now();
    }

    /**
     * Approves the report
     */
    public void approve(String approvedBy, String approvedByName) {
        if (!this.status.equals(ReportStatus.SUBMITTED.name())) {
            throw new IllegalStateException("Can only approve submitted reports");
        }

        this.status = ReportStatus.APPROVED.name();
        this.approvedBy = approvedBy;
        this.approvedByName = approvedByName;
        this.approvedDate = LocalDate.now();

        addGeneratedEvent();
    }

    /**
     * Rejects the report
     */
    public void reject(String rejectionReason) {
        if (!this.status.equals(ReportStatus.SUBMITTED.name())) {
            throw new IllegalStateException("Can only reject submitted reports");
        }

        this.status = ReportStatus.REJECTED.name();
        this.rejectionReason = rejectionReason;
    }

    /**
     * Publishes the report
     */
    public void publish() {
        if (!this.status.equals(ReportStatus.APPROVED.name())) {
            throw new IllegalStateException("Can only publish approved reports");
        }

        this.status = ReportStatus.PUBLISHED.name();
    }

    /**
     * Returns to draft status
     */
    public void returnToDraft(String reason) {
        if (this.status.equals(ReportStatus.PUBLISHED.name())) {
            throw new IllegalStateException("Cannot return published report to draft");
        }

        this.status = ReportStatus.DRAFT.name();
        this.rejectionReason = null;
        if (reason != null && !reason.isBlank()) {
            this.notes = (this.notes != null ? this.notes + "\n\n" : "") +
                        "Returned to draft: " + reason;
        }
    }

    /**
     * Updates notes
     */
    public void updateNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Adds department and region
     */
    public void setLocationDetails(String department, String region) {
        this.department = department;
        this.region = region;
    }

    /**
     * Gets compliance rating
     */
    public String getComplianceRating() {
        if (this.complianceScore == null) {
            return "NOT_RATED";
        }

        if (this.complianceScore >= 95) {
            return "EXCELLENT";
        } else if (this.complianceScore >= 85) {
            return "GOOD";
        } else if (this.complianceScore >= 70) {
            return "SATISFACTORY";
        } else if (this.complianceScore >= 50) {
            return "NEEDS_IMPROVEMENT";
        } else {
            return "POOR";
        }
    }

    /**
     * Checks if report has critical issues
     */
    public boolean hasCriticalIssues() {
        return this.criticalIssues != null && !this.criticalIssues.isEmpty();
    }

    /**
     * Gets critical issues count
     */
    public int getCriticalIssuesCount() {
        return this.criticalIssues != null ? this.criticalIssues.size() : 0;
    }

    /**
     * Gets recommendations count
     */
    public int getRecommendationsCount() {
        return this.recommendations != null ? this.recommendations.size() : 0;
    }

    /**
     * Gets report duration in days
     */
    public long getReportDurationDays() {
        if (this.periodStart != null && this.periodEnd != null) {
            return Period.between(this.periodStart, this.periodEnd).getDays();
        }
        return 0;
    }

    /**
     * Checks if report is due for submission
     */
    public boolean isDueForSubmission() {
        if (this.periodEnd == null) {
            return false;
        }
        return LocalDate.now().isAfter(this.periodEnd.plusDays(7)) &&
               this.status.equals(ReportStatus.DRAFT.name());
    }

    /**
     * Generates unique report number
     */
    private void generateReportNumber() {
        String typeCode = switch (this.reportType) {
            case "MONTHLY" -> "MTH";
            case "QUARTERLY" -> "QTR";
            case "ANNUAL" -> "ANN";
            case "AUDIT" -> "ADT";
            case "INCIDENT" -> "INC";
            default -> "RPT";
        };

        String datePart = LocalDate.now().toString().replace("-", "");
        String randomPart = java.util.UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        this.reportNumber = typeCode + "-" + datePart + "-" + randomPart;
    }

    /**
     * Generates unique report ID
     */
    private void generateReportId() {
        this.reportId = "RPT-" + this.countryCode + "-" +
                       java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Validates period dates
     */
    private static void validatePeriod(LocalDate periodStart, LocalDate periodEnd) {
        if (periodStart == null) {
            throw new ValidationException("periodStart", "Period start date is required");
        }
        if (periodEnd == null) {
            throw new ValidationException("periodEnd", "Period end date is required");
        }
        if (periodEnd.isBefore(periodStart)) {
            throw new ValidationException("periodEnd", "Period end date cannot be before start date");
        }
    }

    /**
     * Adds generated event
     */
    private void addGeneratedEvent() {
        ComplianceReportGeneratedEvent event = ComplianceReportGeneratedEvent.builder()
                .reportId(this.reportId)
                .tenantId(this.tenantId)
                .reportNumber(this.reportNumber)
                .reportType(this.reportType)
                .countryCode(this.countryCode)
                .complianceScore(this.complianceScore)
                .timestamp(java.time.Instant.now())
                .eventType("REPORT_APPROVED")
                .preparedBy(this.preparedBy)
                .build();

        addDomainEvent(event);
    }

    public void addDomainEvent(ComplianceReportGeneratedEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
