package com.gogidix.hr.globalcompliance.infrastructure.persistence.mongodb.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * MongoDB Entity for AuditTrail
 * Maps domain model to database representation
 */
@Document(collection = "compliance_audit_trail")
public class AuditTrailEntity {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("audit_id")
    private String auditId;

    @Field("requirement_id")
    private String requirementId;

    @Field("check_id")
    private String checkId;

    @Field("issue_id")
    private String issueId;

    @Field("report_id")
    private String reportId;

    @Field("action")
    private String action;

    @Field("actioned_by")
    private String actionedBy;

    @Field("actioned_by_name")
    private String actionedByName;

    @Field("action_date")
    private LocalDate actionDate;

    @Field("action_timestamp")
    private Instant actionTimestamp;

    @Field("previous_value")
    private String previousValue;

    @Field("new_value")
    private String newValue;

    @Field("reason")
    private String reason;

    @Field("ip_address")
    private String ipAddress;

    @Field("user_agent")
    private String userAgent;

    @Field("entity_type")
    private String entityType;

    @Field("entity_id")
    private String entityId;

    @Field("country_code")
    private String countryCode;

    @Field("department")
    private String department;

    @Field("changes")
    private String changes;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Default constructor for MongoDB
    public AuditTrailEntity() {
    }

    // Builder pattern for entity creation
    private AuditTrailEntity(Builder builder) {
        this.id = builder.id;
        this.tenantId = builder.tenantId;
        this.auditId = builder.auditId;
        this.requirementId = builder.requirementId;
        this.checkId = builder.checkId;
        this.issueId = builder.issueId;
        this.reportId = builder.reportId;
        this.action = builder.action;
        this.actionedBy = builder.actionedBy;
        this.actionedByName = builder.actionedByName;
        this.actionDate = builder.actionDate;
        this.actionTimestamp = builder.actionTimestamp;
        this.previousValue = builder.previousValue;
        this.newValue = builder.newValue;
        this.reason = builder.reason;
        this.ipAddress = builder.ipAddress;
        this.userAgent = builder.userAgent;
        this.entityType = builder.entityType;
        this.entityId = builder.entityId;
        this.countryCode = builder.countryCode;
        this.department = builder.department;
        this.changes = builder.changes;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getAuditId() { return auditId; }
    public String getRequirementId() { return requirementId; }
    public String getCheckId() { return checkId; }
    public String getIssueId() { return issueId; }
    public String getReportId() { return reportId; }
    public String getAction() { return action; }
    public String getActionedBy() { return actionedBy; }
    public String getActionedByName() { return actionedByName; }
    public LocalDate getActionDate() { return actionDate; }
    public Instant getActionTimestamp() { return actionTimestamp; }
    public String getPreviousValue() { return previousValue; }
    public String getNewValue() { return newValue; }
    public String getReason() { return reason; }
    public String getIpAddress() { return ipAddress; }
    public String getUserAgent() { return userAgent; }
    public String getEntityType() { return entityType; }
    public String getEntityId() { return entityId; }
    public String getCountryCode() { return countryCode; }
    public String getDepartment() { return department; }
    public String getChanges() { return changes; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // Setters (for MongoDB mapping)
    public void setId(String id) { this.id = id; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public void setAuditId(String auditId) { this.auditId = auditId; }
    public void setRequirementId(String requirementId) { this.requirementId = requirementId; }
    public void setCheckId(String checkId) { this.checkId = checkId; }
    public void setIssueId(String issueId) { this.issueId = issueId; }
    public void setReportId(String reportId) { this.reportId = reportId; }
    public void setAction(String action) { this.action = action; }
    public void setActionedBy(String actionedBy) { this.actionedBy = actionedBy; }
    public void setActionedByName(String actionedByName) { this.actionedByName = actionedByName; }
    public void setActionDate(LocalDate actionDate) { this.actionDate = actionDate; }
    public void setActionTimestamp(Instant actionTimestamp) { this.actionTimestamp = actionTimestamp; }
    public void setPreviousValue(String previousValue) { this.previousValue = previousValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    public void setReason(String reason) { this.reason = reason; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public void setDepartment(String department) { this.department = department; }
    public void setChanges(String changes) { this.changes = changes; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditTrailEntity that = (AuditTrailEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    public static class Builder {
        private String id;
        private String tenantId;
        private String auditId;
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
        private Instant createdAt;
        private Instant updatedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder auditId(String auditId) { this.auditId = auditId; return this; }
        public Builder requirementId(String requirementId) { this.requirementId = requirementId; return this; }
        public Builder checkId(String checkId) { this.checkId = checkId; return this; }
        public Builder issueId(String issueId) { this.issueId = issueId; return this; }
        public Builder reportId(String reportId) { this.reportId = reportId; return this; }
        public Builder action(String action) { this.action = action; return this; }
        public Builder actionedBy(String actionedBy) { this.actionedBy = actionedBy; return this; }
        public Builder actionedByName(String actionedByName) { this.actionedByName = actionedByName; return this; }
        public Builder actionDate(LocalDate actionDate) { this.actionDate = actionDate; return this; }
        public Builder actionTimestamp(Instant actionTimestamp) { this.actionTimestamp = actionTimestamp; return this; }
        public Builder previousValue(String previousValue) { this.previousValue = previousValue; return this; }
        public Builder newValue(String newValue) { this.newValue = newValue; return this; }
        public Builder reason(String reason) { this.reason = reason; return this; }
        public Builder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public Builder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        public Builder entityType(String entityType) { this.entityType = entityType; return this; }
        public Builder entityId(String entityId) { this.entityId = entityId; return this; }
        public Builder countryCode(String countryCode) { this.countryCode = countryCode; return this; }
        public Builder department(String department) { this.department = department; return this; }
        public Builder changes(String changes) { this.changes = changes; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        public AuditTrailEntity build() {
            return new AuditTrailEntity(this);
        }
    }
}
