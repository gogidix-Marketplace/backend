package com.gogidix.finance.compliance.domain.model;

import com.gogidix.finance.compliance.domain.event.ComplianceCheckCompletedEvent;
import com.gogidix.finance.compliance.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Compliance Check Domain Entity
 * Records the results of compliance rule evaluations
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

    private String ruleId;

    private String ruleName;

    private String entityType;

    private String entityId;

    private String referenceNumber;

    private CheckStatus status;

    private CheckResult result;

    private SeverityLevel severity;

    private String violationDescription;

    private Map<String, Object> evaluatedContext;

    private BigDecimal evaluatedAmount;

    private String evaluatedCurrency;

    private BigDecimal thresholdAmount;

    private String thresholdCurrency;

    private BigDecimal variance;

    private String department;

    private String costCenter;

    private String expenseCategory;

    private String evaluatedBy;

    private String evaluatedByUserId;

    private Instant evaluatedAt;

    private String approvedBy;

    private Instant approvedAt;

    private String approvalNotes;

    private Boolean waived;

    private String waivedBy;

    private Instant waivedAt;

    private String waiverReason;

    private Boolean remediationRequired;

    private String remediationAction;

    private String remediationAssignedTo;

    private Instant remediationDueDate;

    private Instant remediationCompletedAt;

    private List<String> tags;

    private String notes;

    private String correlationId;

    @Builder.Default
    private List<CheckViolationDetail> violationDetails = new ArrayList<>();

    @Builder.Default
    private List<ComplianceCheckCompletedEvent> domainEvents = new ArrayList<>();

    public enum CheckStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        CANCELLED
    }

    public enum CheckResult {
        COMPLIANT,
        NON_COMPLIANT,
        WARNING,
        NOT_APPLICABLE,
        ERROR
    }

    public enum SeverityLevel {
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckViolationDetail {
        private String field;
        private String expected;
        private String actual;
        private String message;
    }

    /**
     * Creates a new compliance check
     */
    public static ComplianceCheck create(String tenantId, String ruleId, String ruleName,
                                          String entityType, String entityId,
                                          String evaluatedByUserId) {
        ComplianceCheck check = ComplianceCheck.builder()
            .tenantId(tenantId)
            .ruleId(ruleId)
            .ruleName(ruleName)
            .entityType(entityType)
            .entityId(entityId)
            .status(CheckStatus.PENDING)
            .result(CheckResult.NOT_APPLICABLE)
            .evaluatedByUserId(evaluatedByUserId)
            .tags(new ArrayList<>())
            .violationDetails(new ArrayList<>())
            .build();

        return check;
    }

    /**
     * Starts the compliance check evaluation
     */
    public void startEvaluation() {
        if (this.status != CheckStatus.PENDING) {
            throw new IllegalStateException("Can only start pending checks");
        }

        this.status = CheckStatus.IN_PROGRESS;
        this.evaluatedAt = Instant.now();
    }

    /**
     * Marks the check as compliant
     */
    public void markAsCompliant(String evaluatedBy, Map<String, Object> context) {
        if (this.status != CheckStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only mark in-progress checks");
        }

        this.status = CheckStatus.COMPLETED;
        this.result = CheckResult.COMPLIANT;
        this.evaluatedBy = evaluatedBy;
        this.evaluatedContext = context;
        this.evaluatedAt = Instant.now();

        addDomainEvent(ComplianceCheckCompletedEvent.builder()
            .checkId(this.checkId)
            .tenantId(this.tenantId)
            .ruleId(this.ruleId)
            .entityId(this.entityId)
            .result(CheckResult.COMPLIANT.name())
            .timestamp(Instant.now())
            .eventType("COMPLIANCE_CHECK_PASSED")
            .build());
    }

    /**
     * Marks the check as non-compliant
     */
    public void markAsNonCompliant(String evaluatedBy, String violationDescription,
                                    SeverityLevel severity, Map<String, Object> context) {
        if (this.status != CheckStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only mark in-progress checks");
        }

        this.status = CheckStatus.COMPLETED;
        this.result = CheckResult.NON_COMPLIANT;
        this.evaluatedBy = evaluatedBy;
        this.violationDescription = violationDescription;
        this.severity = severity;
        this.evaluatedContext = context;
        this.evaluatedAt = Instant.now();
        this.remediationRequired = true;

        addDomainEvent(ComplianceCheckCompletedEvent.builder()
            .checkId(this.checkId)
            .tenantId(this.tenantId)
            .ruleId(this.ruleId)
            .entityId(this.entityId)
            .result(CheckResult.NON_COMPLIANT.name())
            .violationDescription(violationDescription)
            .severity(severity.name())
            .timestamp(Instant.now())
            .eventType("COMPLIANCE_VIOLATION_DETECTED")
            .build());
    }

    /**
     * Marks the check with a warning
     */
    public void markAsWarning(String evaluatedBy, String warningMessage,
                               Map<String, Object> context) {
        if (this.status != CheckStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only mark in-progress checks");
        }

        this.status = CheckStatus.COMPLETED;
        this.result = CheckResult.WARNING;
        this.evaluatedBy = evaluatedBy;
        this.violationDescription = warningMessage;
        this.severity = SeverityLevel.WARNING;
        this.evaluatedContext = context;
        this.evaluatedAt = Instant.now();

        addDomainEvent(ComplianceCheckCompletedEvent.builder()
            .checkId(this.checkId)
            .tenantId(this.tenantId)
            .ruleId(this.ruleId)
            .entityId(this.entityId)
            .result(CheckResult.WARNING.name())
            .violationDescription(warningMessage)
            .severity(SeverityLevel.WARNING.name())
            .timestamp(Instant.now())
            .eventType("COMPLIANCE_WARNING")
            .build());
    }

    /**
     * Marks the check as not applicable
     */
    public void markAsNotApplicable(String evaluatedBy, String reason) {
        if (this.status != CheckStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only mark in-progress checks");
        }

        this.status = CheckStatus.COMPLETED;
        this.result = CheckResult.NOT_APPLICABLE;
        this.evaluatedBy = evaluatedBy;
        this.notes = reason;
        this.evaluatedAt = Instant.now();
    }

    /**
     * Marks the check as failed due to error
     */
    public void markAsFailed(String evaluatedBy, String errorMessage) {
        this.status = CheckStatus.FAILED;
        this.result = CheckResult.ERROR;
        this.evaluatedBy = evaluatedBy;
        this.violationDescription = errorMessage;
        this.severity = SeverityLevel.ERROR;
        this.evaluatedAt = Instant.now();
    }

    /**
     * Approves the compliance check (for non-compliant items)
     */
    public void approve(String approvedBy, String notes) {
        if (this.result != CheckResult.NON_COMPLIANT && this.result != CheckResult.WARNING) {
            throw new IllegalStateException("Can only approve non-compliant or warning checks");
        }

        this.approvedBy = approvedBy;
        this.approvalNotes = notes;
        this.approvedAt = Instant.now();
    }

    /**
     * Waives the compliance violation
     */
    public void waive(String waivedBy, String reason) {
        if (this.result != CheckResult.NON_COMPLIANT && this.result != CheckResult.WARNING) {
            throw new IllegalStateException("Can only waive non-compliant or warning checks");
        }

        this.waived = true;
        this.waivedBy = waivedBy;
        this.waiverReason = reason;
        this.waivedAt = Instant.now();

        addDomainEvent(ComplianceCheckCompletedEvent.builder()
            .checkId(this.checkId)
            .tenantId(this.tenantId)
            .ruleId(this.ruleId)
            .entityId(this.entityId)
            .result("WAIVED")
            .timestamp(Instant.now())
            .eventType("COMPLIANCE_WAIVED")
            .build());
    }

    /**
     * Assigns remediation
     */
    public void assignRemediation(String assignedTo, String action, Instant dueDate) {
        if (this.result != CheckResult.NON_COMPLIANT) {
            throw new IllegalStateException("Remediation can only be assigned for non-compliant checks");
        }

        this.remediationRequired = true;
        this.remediationAction = action;
        this.remediationAssignedTo = assignedTo;
        this.remediationDueDate = dueDate;
    }

    /**
     * Marks remediation as completed
     */
    public void completeRemediation(String completedBy) {
        if (!this.remediationRequired) {
            throw new IllegalStateException("No remediation was required for this check");
        }

        this.remediationCompletedAt = Instant.now();
        this.notes = (this.notes != null ? this.notes + "\n" : "") +
                     "Remediation completed by " + completedBy + " at " + Instant.now();
    }

    /**
     * Adds a violation detail
     */
    public void addViolationDetail(String field, String expected, String actual, String message) {
        if (this.violationDetails == null) {
            this.violationDetails = new ArrayList<>();
        }

        CheckViolationDetail detail = CheckViolationDetail.builder()
            .field(field)
            .expected(expected)
            .actual(actual)
            .message(message)
            .build();

        this.violationDetails.add(detail);
    }

    /**
     * Calculates variance between evaluated and threshold amounts
     */
    public void calculateVariance() {
        if (this.evaluatedAmount != null && this.thresholdAmount != null &&
            this.evaluatedCurrency != null && this.evaluatedCurrency.equals(this.thresholdCurrency)) {

            this.variance = this.evaluatedAmount.subtract(this.thresholdAmount);
        }
    }

    /**
     * Adds a tag to the check
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
     * Checks if the check requires action
     */
    public boolean requiresAction() {
        return (result == CheckResult.NON_COMPLIANT || result == CheckResult.WARNING) &&
               !Boolean.TRUE.equals(waived) &&
               !Boolean.TRUE.equals(remediationCompletedAt != null);
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
