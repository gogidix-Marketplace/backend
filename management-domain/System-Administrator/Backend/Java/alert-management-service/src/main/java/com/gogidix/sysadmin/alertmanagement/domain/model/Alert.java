package com.gogidix.sysadmin.alertmanagement.domain.model;

import lombok.Builder;
import lombok.AllArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "alerts")
@Builder
@AllArgsConstructor
public class Alert {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    private String alertRuleId;
    private String title;
    private String description;
    private AlertSeverity severity;
    private AlertStatus status;
    private String source;
    private String sourceType;
    private String affectedResourceId;
    private String affectedResourceName;
    private Map<String, Object> data;
    private List<AlertAction> actions;
    private String assignedTo;
    private String acknowledgedBy;
    private Instant acknowledgedAt;
    private String resolvedBy;
    private Instant resolvedAt;
    private String resolutionNotes;
    private Integer occurrenceCount;
    private Instant firstOccurredAt;
    private Instant lastOccurredAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Map<String, String> tags;

    public enum AlertSeverity {
        CRITICAL, HIGH, MEDIUM, LOW, INFO
    }

    public enum AlertStatus {
        OPEN, ACKNOWLEDGED, RESOLVED, CLOSED, SUPPRESSED
    }

    public enum SourceType {
        SYSTEM, MONITORING, SECURITY, USER, API
    }

    public static class AlertAction {
        private String actionType;
        private String target;
        private Map<String, String> parameters;
        private Instant executedAt;
        private String status;

        public String getActionType() { return actionType; }
        public void setActionType(String actionType) { this.actionType = actionType; }
        public String getTarget() { return target; }
        public void setTarget(String target) { this.target = target; }
        public Map<String, String> getParameters() { return parameters; }
        public void setParameters(Map<String, String> parameters) { this.parameters = parameters; }
        public Instant getExecutedAt() { return executedAt; }
        public void setExecutedAt(Instant executedAt) { this.executedAt = executedAt; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public Alert() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.firstOccurredAt = Instant.now();
        this.lastOccurredAt = Instant.now();
        this.occurrenceCount = 1;
        this.status = AlertStatus.OPEN;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getAlertRuleId() { return alertRuleId; }
    public void setAlertRuleId(String alertRuleId) { this.alertRuleId = alertRuleId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public AlertSeverity getSeverity() { return severity; }
    public void setSeverity(AlertSeverity severity) { this.severity = severity; }

    public AlertStatus getStatus() { return status; }
    public void setStatus(AlertStatus status) { this.status = status; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

    public String getAffectedResourceId() { return affectedResourceId; }
    public void setAffectedResourceId(String affectedResourceId) { this.affectedResourceId = affectedResourceId; }

    public String getAffectedResourceName() { return affectedResourceName; }
    public void setAffectedResourceName(String affectedResourceName) { this.affectedResourceName = affectedResourceName; }

    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }

    public List<AlertAction> getActions() { return actions; }
    public void setActions(List<AlertAction> actions) { this.actions = actions; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public String getAcknowledgedBy() { return acknowledgedBy; }
    public void setAcknowledgedBy(String acknowledgedBy) { this.acknowledgedBy = acknowledgedBy; }

    public Instant getAcknowledgedAt() { return acknowledgedAt; }
    public void setAcknowledgedAt(Instant acknowledgedAt) { this.acknowledgedAt = acknowledgedAt; }

    public String getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; }

    public Instant getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Instant resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public Integer getOccurrenceCount() { return occurrenceCount; }
    public void setOccurrenceCount(Integer occurrenceCount) { this.occurrenceCount = occurrenceCount; }

    public Instant getFirstOccurredAt() { return firstOccurredAt; }
    public void setFirstOccurredAt(Instant firstOccurredAt) { this.firstOccurredAt = firstOccurredAt; }

    public Instant getLastOccurredAt() { return lastOccurredAt; }
    public void setLastOccurredAt(Instant lastOccurredAt) { this.lastOccurredAt = lastOccurredAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Map<String, String> getTags() { return tags; }
    public void setTags(Map<String, String> tags) { this.tags = tags; }

    public void acknowledge(String acknowledgedBy) {
        this.status = AlertStatus.ACKNOWLEDGED;
        this.acknowledgedBy = acknowledgedBy;
        this.acknowledgedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void resolve(String resolvedBy, String notes) {
        this.status = AlertStatus.RESOLVED;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = Instant.now();
        this.resolutionNotes = notes;
        this.updatedAt = Instant.now();
    }

    public void incrementOccurrence() {
        this.occurrenceCount++;
        this.lastOccurredAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alert alert = (Alert) o;
        return Objects.equals(id, alert.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Alert instance = new Alert();

        public Builder tenantId(String tenantId) { instance.setTenantId(tenantId); return this; }
        public Builder alertRuleId(String alertRuleId) { instance.setAlertRuleId(alertRuleId); return this; }
        public Builder title(String title) { instance.setTitle(title); return this; }
        public Builder description(String description) { instance.setDescription(description); return this; }
        public Builder severity(AlertSeverity severity) { instance.setSeverity(severity); return this; }
        public Builder source(String source) { instance.setSource(source); return this; }
        public Builder affectedResourceId(String affectedResourceId) { instance.setAffectedResourceId(affectedResourceId); return this; }
        public Builder affectedResourceName(String affectedResourceName) { instance.setAffectedResourceName(affectedResourceName); return this; }
        public Builder data(Map<String, Object> data) { instance.setData(data); return this; }
        public Builder tags(Map<String, String> tags) { instance.setTags(tags); return this; }

        public Alert build() {
            return instance;
        }
    }
}
