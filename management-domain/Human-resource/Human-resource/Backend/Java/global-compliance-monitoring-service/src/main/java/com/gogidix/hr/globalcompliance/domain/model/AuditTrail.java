package com.gogidix.hr.globalcompliance.domain.model;

import com.gogidix.hr.globalcompliance.domain.model.enums.AuditAction;
import com.gogidix.hr.globalcompliance.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Audit Trail Domain Entity
 * Tracks all compliance-related actions for audit purposes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "compliance_audit_trail")
public class AuditTrail extends BaseEntity {

    private String auditId;

    private String tenantId;

    private String requirementId;

    private String checkId;

    private String issueId;

    private String reportId;

    private String action;

    private String actionedBy;

    private String actionedByName;

    private LocalDate actionDate;

    private Instant actionTimestamp;

    private String previousValue;

    private String newValue;

    private String reason;

    private String ipAddress;

    private String userAgent;

    private String entityType;

    private String entityId;

    private String countryCode;

    private String department;

    private String changes;

    /**
     * Creates a new audit trail entry
     */
    public static AuditTrail create(String tenantId, String action,
                                     String actionedBy, String actionedByName,
                                     String entityType, String entityId,
                                     String reason, String ipAddress,
                                     String userAgent, String countryCode) {
        AuditTrail audit = new AuditTrail();
        audit.setTenantId(tenantId);
        audit.setAction(action);
        audit.setActionedBy(actionedBy);
        audit.setActionedByName(actionedByName);
        audit.setEntityType(entityType);
        audit.setEntityId(entityId);
        audit.setReason(reason);
        audit.setIpAddress(ipAddress);
        audit.setUserAgent(userAgent);
        audit.setCountryCode(countryCode);
        audit.setActionDate(LocalDate.now());
        audit.setActionTimestamp(Instant.now());

        audit.generateAuditId();

        return audit;
    }

    /**
     * Creates a requirement audit entry
     */
    public static AuditTrail forRequirement(String tenantId, String requirementId,
                                              String action, String actionedBy,
                                              String actionedByName, String previousValue,
                                              String newValue, String reason,
                                              String ipAddress, String countryCode) {
        AuditTrail audit = create(tenantId, action, actionedBy, actionedByName,
                                   "ComplianceRequirement", requirementId,
                                   reason, ipAddress, null, countryCode);

        audit.setRequirementId(requirementId);
        audit.setPreviousValue(previousValue);
        audit.setNewValue(newValue);
        audit.generateChangesDescription();

        return audit;
    }

    /**
     * Creates a check audit entry
     */
    public static AuditTrail forCheck(String tenantId, String checkId,
                                       String action, String actionedBy,
                                       String actionedByName, String previousValue,
                                       String newValue, String reason,
                                       String ipAddress, String countryCode) {
        AuditTrail audit = create(tenantId, action, actionedBy, actionedByName,
                                   "ComplianceCheck", checkId,
                                   reason, ipAddress, null, countryCode);

        audit.setCheckId(checkId);
        audit.setPreviousValue(previousValue);
        audit.setNewValue(newValue);
        audit.generateChangesDescription();

        return audit;
    }

    /**
     * Creates an issue audit entry
     */
    public static AuditTrail forIssue(String tenantId, String issueId,
                                       String action, String actionedBy,
                                       String actionedByName, String previousValue,
                                       String newValue, String reason,
                                       String ipAddress, String countryCode) {
        AuditTrail audit = create(tenantId, action, actionedBy, actionedByName,
                                   "NonComplianceIssue", issueId,
                                   reason, ipAddress, null, countryCode);

        audit.setIssueId(issueId);
        audit.setPreviousValue(previousValue);
        audit.setNewValue(newValue);
        audit.generateChangesDescription();

        return audit;
    }

    /**
     * Creates a report audit entry
     */
    public static AuditTrail forReport(String tenantId, String reportId,
                                        String action, String actionedBy,
                                        String actionedByName, String previousValue,
                                        String newValue, String reason,
                                        String ipAddress, String countryCode) {
        AuditTrail audit = create(tenantId, action, actionedBy, actionedByName,
                                   "ComplianceReport", reportId,
                                   reason, ipAddress, null, countryCode);

        audit.setReportId(reportId);
        audit.setPreviousValue(previousValue);
        audit.setNewValue(newValue);
        audit.generateChangesDescription();

        return audit;
    }

    /**
     * Updates entity references
     */
    public void setEntityReference(String entityType, String entityId) {
        this.entityType = entityType;
        this.entityId = entityId;

        switch (entityType) {
            case "ComplianceRequirement" -> this.requirementId = entityId;
            case "ComplianceCheck" -> this.checkId = entityId;
            case "NonComplianceIssue" -> this.issueId = entityId;
            case "ComplianceReport" -> this.reportId = entityId;
        }
    }

    /**
     * Sets department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Sets user agent
     */
    public void setUserAgentDetails(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * Generates changes description
     */
    public void generateChangesDescription() {
        if (this.previousValue != null || this.newValue != null) {
            this.changes = "Changed from: [" + (this.previousValue != null ? this.previousValue : "null") +
                         "] to: [" + (this.newValue != null ? this.newValue : "null") + "]";
        }
    }

    /**
     * Gets formatted action description
     */
    public String getActionDescription() {
        try {
            AuditAction auditAction = AuditAction.valueOf(this.action);
            return auditAction.getDescription();
        } catch (IllegalArgumentException e) {
            return this.action;
        }
    }

    /**
     * Checks if this is a create action
     */
    public boolean isCreateAction() {
        return AuditAction.CREATED.name().equals(this.action);
    }

    /**
     * Checks if this is an update action
     */
    public boolean isUpdateAction() {
        return AuditAction.UPDATED.name().equals(this.action);
    }

    /**
     * Checks if this is a delete action
     */
    public boolean isDeleteAction() {
        return AuditAction.DELETED.name().equals(this.action);
    }

    /**
     * Checks if this is a critical action
     */
    public boolean isCriticalAction() {
        return AuditAction.DELETED.name().equals(this.action) ||
               AuditAction.ESCALATED.name().equals(this.action) ||
               AuditAction.REJECTED.name().equals(this.action);
    }

    /**
     * Gets the target entity display name
     */
    public String getTargetEntityDisplay() {
        return this.entityType + " [" + this.entityId + "]";
    }

    /**
     * Generates unique audit ID
     */
    private void generateAuditId() {
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String randomPart = java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.auditId = "AUD-" + timestamp.substring(timestamp.length() - 10) + "-" + randomPart;
    }
}
