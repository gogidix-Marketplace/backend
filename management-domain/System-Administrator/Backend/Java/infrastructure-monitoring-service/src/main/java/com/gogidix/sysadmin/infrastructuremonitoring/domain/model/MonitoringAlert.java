package com.gogidix.sysadmin.infrastructuremonitoring.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Document(collection = "monitoring_alerts")
public class MonitoringAlert {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    private String infrastructureId;
    private String infrastructureName;
    private AlertSeverity severity;
    private AlertType type;
    private String title;
    private String description;
    private Map<String, Object> details;
    private AlertStatus status;
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

    public enum AlertSeverity {
        CRITICAL, HIGH, MEDIUM, LOW, INFO
    }

    public enum AlertType {
        SERVICE_DOWN, HIGH_CPU, HIGH_MEMORY, HIGH_DISK, NETWORK_ISSUE,
        DATABASE_CONNECTION_ERROR, SLOW_RESPONSE, CERTIFICATE_EXPIRING,
        REPLATION_LAG, QUEUE_BUILDUP, CUSTOM
    }

    public enum AlertStatus {
        OPEN, ACKNOWLEDGED, RESOLVED, CLOSED, SUPPRESSED
    }

    public MonitoringAlert() {
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

    public String getInfrastructureId() { return infrastructureId; }
    public void setInfrastructureId(String infrastructureId) { this.infrastructureId = infrastructureId; }

    public String getInfrastructureName() { return infrastructureName; }
    public void setInfrastructureName(String infrastructureName) { this.infrastructureName = infrastructureName; }

    public AlertSeverity getSeverity() { return severity; }
    public void setSeverity(AlertSeverity severity) { this.severity = severity; }

    public AlertType getType() { return type; }
    public void setType(AlertType type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Map<String, Object> getDetails() { return details; }
    public void setDetails(Map<String, Object> details) { this.details = details; }

    public AlertStatus getStatus() { return status; }
    public void setStatus(AlertStatus status) { this.status = status; }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonitoringAlert that = (MonitoringAlert) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final MonitoringAlert instance = new MonitoringAlert();

        public Builder id(String id) { instance.setId(id); return this; }
        public Builder tenantId(String tenantId) { instance.setTenantId(tenantId); return this; }
        public Builder infrastructureId(String infrastructureId) { instance.setInfrastructureId(infrastructureId); return this; }
        public Builder infrastructureName(String infrastructureName) { instance.setInfrastructureName(infrastructureName); return this; }
        public Builder severity(AlertSeverity severity) { instance.setSeverity(severity); return this; }
        public Builder type(AlertType type) { instance.setType(type); return this; }
        public Builder title(String title) { instance.setTitle(title); return this; }
        public Builder description(String description) { instance.setDescription(description); return this; }
        public Builder details(Map<String, Object> details) { instance.setDetails(details); return this; }

        public MonitoringAlert build() {
            return instance;
        }
    }
}
