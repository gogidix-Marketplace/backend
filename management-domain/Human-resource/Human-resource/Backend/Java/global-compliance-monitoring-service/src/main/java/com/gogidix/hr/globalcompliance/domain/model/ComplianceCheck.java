package com.gogidix.hr.globalcompliance.domain.model;

import com.gogidix.hr.globalcompliance.domain.event.ComplianceCheckCompletedEvent;
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
 * Compliance Check Domain Entity
 * Multi-tenant compliance check management
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "compliance_checks")
public class ComplianceCheck extends BaseEntity {

    private String checkId;

    private String tenantId;

    private String checkNumber;

    private String requirementId;

    private String requirementName;

    private String countryCode;

    private String status;

    private LocalDate scheduledDate;

    private LocalDate completedDate;

    private String checkedBy;

    private String checkedByName;

    private String result;

    private String findings;

    private String correctiveAction;

    private LocalDate targetCompletionDate;

    private LocalDate actualCompletionDate;

    private List<String> supportingDocuments;

    private List<IssueReference> issues;

    private String comments;

    private String frequency;

    @Builder.Default
    private List<ComplianceCheckCompletedEvent> domainEvents = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IssueReference {
        private String issueId;
        private String issueNumber;
        private String severity;
    }

    /**
     * Creates a new compliance check
     */
    public static ComplianceCheck create(String tenantId, String requirementId,
                                          String requirementName, String countryCode,
                                          LocalDate scheduledDate, String frequency,
                                          String createdBy) {
        ComplianceCheck check = new ComplianceCheck();
        check.setTenantId(tenantId);
        check.setRequirementId(requirementId);
        check.setRequirementName(requirementName);
        check.setCountryCode(countryCode);
        check.setScheduledDate(scheduledDate);
        check.setFrequency(frequency);
        check.setStatus(CheckStatus.PENDING.name());
        check.setResult(CheckResult.NOT_TESTED.name());
        check.setSupportingDocuments(new ArrayList<>());
        check.setIssues(new ArrayList<>());

        check.generateCheckNumber();
        check.generateCheckId();

        return check;
    }

    /**
     * Starts the compliance check
     */
    public void start(String checkedBy, String checkedByName) {
        if (!this.status.equals(CheckStatus.PENDING.name())) {
            throw new IllegalStateException("Can only start pending checks");
        }

        this.status = CheckStatus.IN_PROGRESS.name();
        this.checkedBy = checkedBy;
        this.checkedByName = checkedByName;
    }

    /**
     * Completes the compliance check with result
     */
    public void complete(String result, String findings, String correctiveAction,
                         LocalDate targetCompletionDate, List<String> supportingDocuments) {
        if (!this.status.equals(CheckStatus.IN_PROGRESS.name())) {
            throw new IllegalStateException("Can only complete checks in progress");
        }

        validateResult(result);

        this.status = determineStatusFromResult(result);
        this.result = result;
        this.findings = findings;
        this.correctiveAction = correctiveAction;
        this.targetCompletionDate = targetCompletionDate;
        this.completedDate = LocalDate.now();

        if (supportingDocuments != null && !supportingDocuments.isEmpty()) {
            if (this.supportingDocuments == null) {
                this.supportingDocuments = new ArrayList<>();
            }
            this.supportingDocuments.addAll(supportingDocuments);
        }

        addCompletionEvent();
    }

    /**
     * Marks the check as passed
     */
    public void markAsPassed(String findings, List<String> supportingDocuments) {
        complete(CheckResult.COMPLIANT.name(), findings, null, null, supportingDocuments);
    }

    /**
     * Marks the check as failed
     */
    public void markAsFailed(String findings, String correctiveAction,
                              LocalDate targetCompletionDate, List<String> supportingDocuments) {
        complete(CheckResult.NON_COMPLIANT.name(), findings, correctiveAction,
                targetCompletionDate, supportingDocuments);
    }

    /**
     * Marks the check as partially compliant
     */
    public void markAsPartiallyCompliant(String findings, String correctiveAction,
                                          LocalDate targetCompletionDate, List<String> supportingDocuments) {
        complete(CheckResult.PARTIALLY_COMPLIANT.name(), findings, correctiveAction,
                targetCompletionDate, supportingDocuments);
    }

    /**
     * Waives the compliance check
     */
    public void waive(String reason, String waivedBy) {
        if (!this.status.equals(CheckStatus.PENDING.name()) &&
            !this.status.equals(CheckStatus.IN_PROGRESS.name())) {
            throw new IllegalStateException("Can only waive pending or in-progress checks");
        }

        this.status = CheckStatus.WAIVED.name();
        this.result = CheckResult.NOT_TESTED.name();
        this.findings = "WAIVED: " + reason;
        this.completedDate = LocalDate.now();
    }

    /**
     * Marks the check as not applicable
     */
    public void markAsNotApplicable(String reason) {
        if (!this.status.equals(CheckStatus.PENDING.name()) &&
            !this.status.equals(CheckStatus.IN_PROGRESS.name())) {
            throw new IllegalStateException("Can only mark pending or in-progress checks as not applicable");
        }

        this.status = CheckStatus.NOT_APPLICABLE.name();
        this.result = CheckResult.NOT_TESTED.name();
        this.findings = "NOT APPLICABLE: " + reason;
        this.completedDate = LocalDate.now();
    }

    /**
     * Adds an issue reference
     */
    public void addIssue(String issueId, String issueNumber, String severity) {
        if (this.issues == null) {
            this.issues = new ArrayList<>();
        }
        IssueReference issueRef = IssueReference.builder()
                .issueId(issueId)
                .issueNumber(issueNumber)
                .severity(severity)
                .build();
        this.issues.add(issueRef);

        // Update status if issues are critical
        if (IssueSeverity.CRITICAL.name().equals(severity) &&
            this.result.equals(CheckResult.COMPLIANT.name())) {
            this.result = CheckResult.PARTIALLY_COMPLIANT.name();
        }
    }

    /**
     * Removes an issue reference
     */
    public void removeIssue(String issueId) {
        if (this.issues != null) {
            this.issues.removeIf(issue -> issue.getIssueId().equals(issueId));
        }
    }

    /**
     * Updates corrective action
     */
    public void updateCorrectiveAction(String correctiveAction, LocalDate actualCompletionDate) {
        this.correctiveAction = correctiveAction;
        this.actualCompletionDate = actualCompletionDate;
    }

    /**
     * Adds a supporting document
     */
    public void addSupportingDocument(String documentUrl) {
        if (this.supportingDocuments == null) {
            this.supportingDocuments = new ArrayList<>();
        }
        if (!this.supportingDocuments.contains(documentUrl)) {
            this.supportingDocuments.add(documentUrl);
        }
    }

    /**
     * Removes a supporting document
     */
    public void removeSupportingDocument(String documentUrl) {
        if (this.supportingDocuments != null) {
            this.supportingDocuments.remove(documentUrl);
        }
    }

    /**
     * Adds comments
     */
    public void addComment(String comment) {
        if (this.comments == null || this.comments.isBlank()) {
            this.comments = comment;
        } else {
            this.comments = this.comments + "\n\n" + comment;
        }
    }

    /**
     * Reschedules the check
     */
    public void reschedule(LocalDate newScheduledDate, String reason) {
        if (!this.status.equals(CheckStatus.PENDING.name())) {
            throw new IllegalStateException("Can only reschedule pending checks");
        }
        if (newScheduledDate.isBefore(LocalDate.now())) {
            throw new ValidationException("scheduledDate", "Cannot reschedule to past date");
        }

        this.scheduledDate = newScheduledDate;
        addComment("RESCHEDULED: " + reason);
    }

    /**
     * Checks if the check is overdue
     */
    public boolean isOverdue() {
        return this.scheduledDate != null &&
               LocalDate.now().isAfter(this.scheduledDate) &&
               !isCompleted();
    }

    /**
     * Checks if the check is completed
     */
    public boolean isCompleted() {
        return this.status.equals(CheckStatus.PASSED.name()) ||
               this.status.equals(CheckStatus.FAILED.name()) ||
               this.status.equals(CheckStatus.WAIVED.name()) ||
               this.status.equals(CheckStatus.NOT_APPLICABLE.name());
    }

    /**
     * Checks if the check has failed or partially compliant
     */
    public boolean hasFailed() {
        return this.result.equals(CheckResult.NON_COMPLIANT.name()) ||
               this.result.equals(CheckResult.PARTIALLY_COMPLIANT.name());
    }

    /**
     * Gets days until scheduled date
     */
    public long getDaysUntilScheduled() {
        return this.scheduledDate != null ?
                Period.between(LocalDate.now(), this.scheduledDate).getDays() : 0;
    }

    /**
     * Gets days overdue
     */
    public long getDaysOverdue() {
        if (this.scheduledDate != null && LocalDate.now().isAfter(this.scheduledDate) && !isCompleted()) {
            return Period.between(this.scheduledDate, LocalDate.now()).getDays();
        }
        return 0;
    }

    /**
     * Gets critical issues count
     */
    public long getCriticalIssuesCount() {
        return this.issues != null ?
                this.issues.stream()
                        .filter(issue -> IssueSeverity.CRITICAL.name().equals(issue.getSeverity()))
                        .count() : 0;
    }

    /**
     * Generates unique check number
     */
    private void generateCheckNumber() {
        String datePart = LocalDate.now().toString().replace("-", "");
        String randomPart = java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.checkNumber = "CHK-" + datePart + "-" + randomPart;
    }

    /**
     * Generates unique check ID
     */
    private void generateCheckId() {
        this.checkId = "CHK-" + this.countryCode + "-" +
                       java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Validates result
     */
    private void validateResult(String result) {
        try {
            CheckResult.valueOf(result);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("result", "Invalid check result: " + result);
        }
    }

    /**
     * Determines status based on result
     */
    private String determineStatusFromResult(String result) {
        return switch (result) {
            case "COMPLIANT" -> CheckStatus.PASSED.name();
            case "NON_COMPLIANT", "PARTIALLY_COMPLIANT" -> CheckStatus.FAILED.name();
            default -> CheckStatus.PENDING.name();
        };
    }

    /**
     * Adds completion event
     */
    private void addCompletionEvent() {
        ComplianceCheckCompletedEvent event = ComplianceCheckCompletedEvent.builder()
                .checkId(this.checkId)
                .tenantId(this.tenantId)
                .requirementId(this.requirementId)
                .checkNumber(this.checkNumber)
                .status(this.status)
                .result(this.result)
                .countryCode(this.countryCode)
                .timestamp(java.time.Instant.now())
                .eventType("CHECK_COMPLETED")
                .checkedBy(this.checkedBy)
                .build();

        addDomainEvent(event);
    }

    public void addDomainEvent(ComplianceCheckCompletedEvent event) {
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
