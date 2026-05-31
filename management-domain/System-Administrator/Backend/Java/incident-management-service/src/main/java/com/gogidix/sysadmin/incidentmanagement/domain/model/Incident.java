package com.gogidix.sysadmin.incidentmanagement.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "incidents")
public class Incident {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    private String incidentNumber;
    private String title;
    private String description;
    private IncidentStatus status;
    private IncidentPriority priority;
    private IncidentType type;
    private String assignedTo;
    private String assignedTeam;
    private String createdBy;
    private String resolvedBy;
    private Instant resolvedAt;
    private String closedBy;
    private Instant closedAt;
    private String closureNotes;
    private String affectedService;
    private List<String> affectedResources;
    private String rootCause;
    private List<String> relatedAlertIds;
    private List<String> relatedIncidentIds;
    private List<IncidentUpdate> updates;
    private List<String> tags;
    private Instant detectedAt;
    private Instant acknowledgedAt;
    private Instant firstResponseAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Map<String, Object> metadata;
    private String slaBreach;
    private Instant resolvedTarget;

    public enum IncidentStatus {
        OPEN, ACKNOWLEDGED, IN_PROGRESS, RESOLVED, CLOSED
    }

    public enum IncidentPriority {
        P1_CRITICAL, P2_HIGH, P3_MEDIUM, P4_LOW
    }

    public enum IncidentType {
        OUTAGE, DEGRADATION, SECURITY, DATA_LOSS, PERFORMANCE, OTHER
    }

    public static class IncidentUpdate {
        private String id;
        private String updatedBy;
        private String update;
        private Boolean isInternal;
        private Instant createdAt;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getUpdatedBy() { return updatedBy; }
        public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
        public String getUpdate() { return update; }
        public void setUpdate(String update) { this.update = update; }
        public Boolean getIsInternal() { return isInternal; }
        public void setIsInternal(Boolean isInternal) { this.isInternal = isInternal; }
        public Instant getCreatedAt() { return createdAt; }
        public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    }

    public Incident() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.detectedAt = Instant.now();
        this.status = IncidentStatus.OPEN;
        this.priority = IncidentPriority.P3_MEDIUM;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getIncidentNumber() { return incidentNumber; }
    public void setIncidentNumber(String incidentNumber) { this.incidentNumber = incidentNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public IncidentStatus getStatus() { return status; }
    public void setStatus(IncidentStatus status) { this.status = status; }

    public IncidentPriority getPriority() { return priority; }
    public void setPriority(IncidentPriority priority) { this.priority = priority; }

    public IncidentType getType() { return type; }
    public void setType(IncidentType type) { this.type = type; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public String getAssignedTeam() { return assignedTeam; }
    public void setAssignedTeam(String assignedTeam) { this.assignedTeam = assignedTeam; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; }

    public Instant getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Instant resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getClosedBy() { return closedBy; }
    public void setClosedBy(String closedBy) { this.closedBy = closedBy; }

    public Instant getClosedAt() { return closedAt; }
    public void setClosedAt(Instant closedAt) { this.closedAt = closedAt; }

    public String getClosureNotes() { return closureNotes; }
    public void setClosureNotes(String closureNotes) { this.closureNotes = closureNotes; }

    public String getAffectedService() { return affectedService; }
    public void setAffectedService(String affectedService) { this.affectedService = affectedService; }

    public List<String> getAffectedResources() { return affectedResources; }
    public void setAffectedResources(List<String> affectedResources) { this.affectedResources = affectedResources; }

    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }

    public List<String> getRelatedAlertIds() { return relatedAlertIds; }
    public void setRelatedAlertIds(List<String> relatedAlertIds) { this.relatedAlertIds = relatedAlertIds; }

    public List<String> getRelatedIncidentIds() { return relatedIncidentIds; }
    public void setRelatedIncidentIds(List<String> relatedIncidentIds) { this.relatedIncidentIds = relatedIncidentIds; }

    public List<IncidentUpdate> getUpdates() { return updates; }
    public void setUpdates(List<IncidentUpdate> updates) { this.updates = updates; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Instant getDetectedAt() { return detectedAt; }
    public void setDetectedAt(Instant detectedAt) { this.detectedAt = detectedAt; }

    public Instant getAcknowledgedAt() { return acknowledgedAt; }
    public void setAcknowledgedAt(Instant acknowledgedAt) { this.acknowledgedAt = acknowledgedAt; }

    public Instant getFirstResponseAt() { return firstResponseAt; }
    public void setFirstResponseAt(Instant firstResponseAt) { this.firstResponseAt = firstResponseAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public String getSlaBreach() { return slaBreach; }
    public void setSlaBreach(String slaBreach) { this.slaBreach = slaBreach; }

    public Instant getResolvedTarget() { return resolvedTarget; }
    public void setResolvedTarget(Instant resolvedTarget) { this.resolvedTarget = resolvedTarget; }

    public void acknowledge(String acknowledgedBy) {
        this.status = IncidentStatus.ACKNOWLEDGED;
        this.acknowledgedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void resolve(String resolvedBy) {
        this.status = IncidentStatus.RESOLVED;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void close(String closedBy, String notes) {
        this.status = IncidentStatus.CLOSED;
        this.closedBy = closedBy;
        this.closedAt = Instant.now();
        this.closureNotes = notes;
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Incident incident = (Incident) o;
        return Objects.equals(id, incident.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
