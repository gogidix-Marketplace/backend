package com.gogidix.sysadmin.infrastructuremonitoring.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.sysadmin.infrastructuremonitoring.domain.model.MonitoringAlert;

import java.time.Instant;
import java.util.Map;

public class MonitoringAlertDTO {

    private String id;
    private String infrastructureId;
    private String infrastructureName;
    private MonitoringAlert.AlertSeverity severity;
    private MonitoringAlert.AlertType type;
    private String title;
    private String description;
    private Map<String, Object> details;
    private MonitoringAlert.AlertStatus status;
    private String acknowledgedBy;
    private String resolvedBy;
    private String resolutionNotes;
    private Integer occurrenceCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant firstOccurredAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant lastOccurredAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createdAt;

    public static MonitoringAlertDTO fromEntity(MonitoringAlert entity) {
        if (entity == null) return null;
        MonitoringAlertDTO dto = new MonitoringAlertDTO();
        dto.id = entity.getId();
        dto.infrastructureId = entity.getInfrastructureId();
        dto.infrastructureName = entity.getInfrastructureName();
        dto.severity = entity.getSeverity();
        dto.type = entity.getType();
        dto.title = entity.getTitle();
        dto.description = entity.getDescription();
        dto.details = entity.getDetails();
        dto.status = entity.getStatus();
        dto.acknowledgedBy = entity.getAcknowledgedBy();
        dto.resolvedBy = entity.getResolvedBy();
        dto.resolutionNotes = entity.getResolutionNotes();
        dto.occurrenceCount = entity.getOccurrenceCount();
        dto.firstOccurredAt = entity.getFirstOccurredAt();
        dto.lastOccurredAt = entity.getLastOccurredAt();
        dto.createdAt = entity.getCreatedAt();
        return dto;
    }

    public MonitoringAlert toEntity() {
        MonitoringAlert entity = new MonitoringAlert();
        entity.setId(this.id);
        entity.setInfrastructureId(this.infrastructureId);
        entity.setInfrastructureName(this.infrastructureName);
        entity.setSeverity(this.severity);
        entity.setType(this.type);
        entity.setTitle(this.title);
        entity.setDescription(this.description);
        entity.setDetails(this.details);
        entity.setStatus(this.status);
        entity.setResolutionNotes(this.resolutionNotes);
        return entity;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getInfrastructureId() { return infrastructureId; }
    public void setInfrastructureId(String infrastructureId) { this.infrastructureId = infrastructureId; }

    public String getInfrastructureName() { return infrastructureName; }
    public void setInfrastructureName(String infrastructureName) { this.infrastructureName = infrastructureName; }

    public MonitoringAlert.AlertSeverity getSeverity() { return severity; }
    public void setSeverity(MonitoringAlert.AlertSeverity severity) { this.severity = severity; }

    public MonitoringAlert.AlertType getType() { return type; }
    public void setType(MonitoringAlert.AlertType type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Map<String, Object> getDetails() { return details; }
    public void setDetails(Map<String, Object> details) { this.details = details; }

    public MonitoringAlert.AlertStatus getStatus() { return status; }
    public void setStatus(MonitoringAlert.AlertStatus status) { this.status = status; }

    public String getAcknowledgedBy() { return acknowledgedBy; }
    public void setAcknowledgedBy(String acknowledgedBy) { this.acknowledgedBy = acknowledgedBy; }

    public String getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; }

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
}
