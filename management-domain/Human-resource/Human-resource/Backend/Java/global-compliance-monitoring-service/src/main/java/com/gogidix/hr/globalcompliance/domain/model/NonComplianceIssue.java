package com.gogidix.hr.globalcompliance.domain.model;

import com.gogidix.hr.globalcompliance.domain.event.NonComplianceIssueCreatedEvent;
import com.gogidix.hr.globalcompliance.domain.event.NonComplianceIssueResolvedEvent;
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
 * Non-Compliance Issue Domain Entity
 * Multi-tenant non-compliance issue management
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "non_compliance_issues")
public class NonComplianceIssue extends BaseEntity {

    private String issueId;

    private String tenantId;

    private String issueNumber;

    private String requirementId;

    private String checkId;

    private String countryCode;

    private String title;

    private String description;

    private String severity;

    private String status;

    private LocalDate identifiedDate;

    private String identifiedBy;

    private String identifiedByName;

    private String assignedTo;

    private String assignedToName;

    private LocalDate dueDate;

    private LocalDate resolvedDate;

    private String resolution;

    private String rootCause;

    private List<String> affectedEmployees;

    private Double financialImpact;

    private String currency;

    private List<String> actions;

    private String department;

    private String location;

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    /**
     * Creates a new non-compliance issue
     */
    public static NonComplianceIssue create(String tenantId, String requirementId,
                                              String checkId, String countryCode,
                                              String title, String description,
                                              String severity, String identifiedBy,
                                              String identifiedByName, String department) {
        NonComplianceIssue issue = new NonComplianceIssue();
        issue.setTenantId(tenantId);
        issue.setRequirementId(requirementId);
        issue.setCheckId(checkId);
        issue.setCountryCode(countryCode);
        issue.setTitle(title);
        issue.setDescription(description);
        issue.setSeverity(severity);
        issue.setStatus(IssueStatus.OPEN.name());
        issue.setIdentifiedDate(LocalDate.now());
        issue.setIdentifiedBy(identifiedBy);
        issue.setIdentifiedByName(identifiedByName);
        issue.setAssignedTo(identifiedBy);
        issue.setAssignedToName(identifiedByName);
        issue.setAffectedEmployees(new ArrayList<>());
        issue.setActions(new ArrayList<>());
        issue.setDepartment(department);

        issue.generateIssueId();
        issue.calculateDueDate();

        issue.addDomainEvent(NonComplianceIssueCreatedEvent.builder()
                .issueId(issue.getIssueId())
                .tenantId(tenantId)
                .issueNumber(issue.getIssueNumber())
                .requirementId(requirementId)
                .checkId(checkId)
                .severity(severity)
                .countryCode(countryCode)
                .timestamp(java.time.Instant.now())
                .eventType("ISSUE_CREATED")
                .identifiedBy(identifiedBy)
                .build());

        return issue;
    }

    /**
     * Assigns the issue to a user
     */
    public void assignTo(String assignedTo, String assignedToName) {
        if (this.status.equals(IssueStatus.CLOSED.name()) ||
            this.status.equals(IssueStatus.RESOLVED.name())) {
            throw new IllegalStateException("Cannot assign closed or resolved issues");
        }

        this.assignedTo = assignedTo;
        this.assignedToName = assignedToName;
    }

    /**
     * Starts working on the issue
     */
    public void startProgress() {
        if (!this.status.equals(IssueStatus.OPEN.name())) {
            throw new IllegalStateException("Can only start progress on open issues");
        }

        this.status = IssueStatus.IN_PROGRESS.name();
    }

    /**
     * Resolves the issue
     */
    public void resolve(String resolution, String rootCause, String resolvedBy) {
        if (!this.status.equals(IssueStatus.OPEN.name()) &&
            !this.status.equals(IssueStatus.IN_PROGRESS.name()) &&
            !this.status.equals(IssueStatus.ESCALATED.name())) {
            throw new IllegalStateException("Can only resolve open, in-progress, or escalated issues");
        }

        this.status = IssueStatus.RESOLVED.name();
        this.resolution = resolution;
        this.rootCause = rootCause;
        this.resolvedDate = LocalDate.now();

        addResolvedEvent(resolvedBy);
    }

    /**
     * Closes the issue
     */
    public void close(String closedBy) {
        if (!this.status.equals(IssueStatus.RESOLVED.name())) {
            throw new IllegalStateException("Can only close resolved issues");
        }

        this.status = IssueStatus.CLOSED.name();
    }

    /**
     * Escalates the issue
     */
    public void escalate(String reason, String escalatedBy) {
        if (this.status.equals(IssueStatus.CLOSED.name()) ||
            this.status.equals(IssueStatus.ESCALATED.name())) {
            throw new IllegalStateException("Cannot escalate closed or already escalated issues");
        }

        String previousStatus = this.status;
        this.status = IssueStatus.ESCALATED.name();

        if (this.actions == null) {
            this.actions = new ArrayList<>();
        }
        this.actions.add("Escalated by " + escalatedBy + " on " + LocalDate.now() +
                        ". Previous status: " + previousStatus + ". Reason: " + reason);
    }

    /**
     * Reopens the issue
     */
    public void reopen(String reason) {
        if (!this.status.equals(IssueStatus.RESOLVED.name()) &&
            !this.status.equals(IssueStatus.CLOSED.name())) {
            throw new IllegalStateException("Can only reopen resolved or closed issues");
        }

        this.status = IssueStatus.OPEN.name();
        this.resolution = null;
        this.resolvedDate = null;

        if (this.actions == null) {
            this.actions = new ArrayList<>();
        }
        this.actions.add("Reopened on " + LocalDate.now() + ". Reason: " + reason);
    }

    /**
     * Updates severity
     */
    public void updateSeverity(String newSeverity, String reason) {
        String previousSeverity = this.severity;
        this.severity = newSeverity;
        recalculateDueDate();

        if (this.actions == null) {
            this.actions = new ArrayList<>();
        }
        this.actions.add("Severity changed from " + previousSeverity + " to " + newSeverity +
                        " on " + LocalDate.now() + ". Reason: " + reason);
    }

    /**
     * Adds an affected employee
     */
    public void addAffectedEmployee(String employeeId) {
        if (this.affectedEmployees == null) {
            this.affectedEmployees = new ArrayList<>();
        }
        if (!this.affectedEmployees.contains(employeeId)) {
            this.affectedEmployees.add(employeeId);
        }
    }

    /**
     * Removes an affected employee
     */
    public void removeAffectedEmployee(String employeeId) {
        if (this.affectedEmployees != null) {
            this.affectedEmployees.remove(employeeId);
        }
    }

    /**
     * Sets financial impact
     */
    public void setFinancialImpactDetails(Double financialImpact, String currency) {
        if (financialImpact != null && financialImpact < 0) {
            throw new ValidationException("financialImpact", "Financial impact cannot be negative");
        }
        this.financialImpact = financialImpact;
        this.currency = currency != null ? currency : "USD";
    }

    /**
     * Adds an action taken
     */
    public void addAction(String action) {
        if (this.actions == null) {
            this.actions = new ArrayList<>();
        }
        this.actions.add(action + " (Date: " + LocalDate.now() + ")");
    }

    /**
     * Updates due date
     */
    public void updateDueDate(LocalDate newDueDate, String reason) {
        if (newDueDate.isBefore(LocalDate.now())) {
            throw new ValidationException("dueDate", "Due date cannot be in the past");
        }
        LocalDate previousDueDate = this.dueDate;
        this.dueDate = newDueDate;

        if (this.actions == null) {
            this.actions = new ArrayList<>();
        }
        this.actions.add("Due date changed from " + previousDueDate + " to " + newDueDate +
                        ". Reason: " + reason);
    }

    /**
     * Checks if the issue is overdue
     */
    public boolean isOverdue() {
        return this.dueDate != null &&
               LocalDate.now().isAfter(this.dueDate) &&
               !this.status.equals(IssueStatus.CLOSED.name()) &&
               !this.status.equals(IssueStatus.RESOLVED.name());
    }

    /**
     * Checks if the issue is critical
     */
    public boolean isCritical() {
        return IssueSeverity.CRITICAL.name().equals(this.severity);
    }

    /**
     * Gets days until due date
     */
    public long getDaysUntilDue() {
        if (this.dueDate == null) {
            return 0;
        }
        if (LocalDate.now().isAfter(this.dueDate)) {
            return -Period.between(this.dueDate, LocalDate.now()).getDays();
        }
        return Period.between(LocalDate.now(), this.dueDate).getDays();
    }

    /**
     * Gets days to resolve
     */
    public long getDaysToResolve() {
        if (this.resolvedDate == null || this.identifiedDate == null) {
            return 0;
        }
        return Period.between(this.identifiedDate, this.resolvedDate).getDays();
    }

    /**
     * Gets the priority level (1-4, 1 being highest)
     */
    public int getPriorityLevel() {
        return switch (this.severity) {
            case "CRITICAL" -> 1;
            case "HIGH" -> 2;
            case "MEDIUM" -> 3;
            case "LOW" -> 4;
            default -> 5;
        };
    }

    /**
     * Checks if issue requires immediate attention
     */
    public boolean requiresImmediateAttention() {
        return isCritical() || isOverdue();
    }

    /**
     * Generates unique issue ID
     */
    private void generateIssueId() {
        String datePart = LocalDate.now().toString().replace("-", "");
        String randomPart = java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.issueNumber = "ISS-" + datePart + "-" + randomPart;
        this.issueId = "ISS-" + this.countryCode + "-" +
                       java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Calculates due date based on severity
     */
    private void calculateDueDate() {
        recalculateDueDate();
    }

    /**
     * Recalculates due date based on current severity
     */
    private void recalculateDueDate() {
        LocalDate baseDate = LocalDate.now();
        this.dueDate = switch (this.severity) {
            case "CRITICAL" -> baseDate.plusDays(3);
            case "HIGH" -> baseDate.plusDays(7);
            case "MEDIUM" -> baseDate.plusDays(14);
            case "LOW" -> baseDate.plusDays(30);
            default -> baseDate.plusDays(14);
        };
    }

    /**
     * Adds resolved event
     */
    private void addResolvedEvent(String resolvedBy) {
        NonComplianceIssueResolvedEvent event = NonComplianceIssueResolvedEvent.builder()
                .issueId(this.issueId)
                .tenantId(this.tenantId)
                .issueNumber(this.issueNumber)
                .requirementId(this.requirementId)
                .resolution(this.resolution)
                .countryCode(this.countryCode)
                .timestamp(java.time.Instant.now())
                .eventType("ISSUE_RESOLVED")
                .resolvedBy(resolvedBy)
                .build();

        addDomainEvent(event);
    }

    public void addDomainEvent(Object event) {
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
