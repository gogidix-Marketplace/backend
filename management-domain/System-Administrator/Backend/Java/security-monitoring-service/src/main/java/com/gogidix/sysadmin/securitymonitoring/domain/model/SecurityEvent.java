package com.gogidix.sysadmin.securitymonitoring.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Document(collection = "security_events")
public class SecurityEvent {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    private String eventId;
    private EventType eventType;
    private EventSeverity severity;
    private EventCategory category;
    private String title;
    private String description;
    private String sourceIp;
    private String sourceHost;
    private String targetResource;
    private String targetResourceType;
    private String userId;
    private String username;
    private Boolean authenticated;
    private Map<String, Object> details;
    private EventStatus status;
    private String assignedTo;
    private String resolvedBy;
    private Instant resolvedAt;
    private String resolutionNotes;
    private Boolean isFalsePositive;
    private Instant detectedAt;
    private Instant createdAt;

    public enum EventType {
        UNAUTHORIZED_ACCESS, BRUTE_FORCE_ATTEMPT, MALWARE_DETECTED,
        INTRUSION_DETECTED, DATA_EXFILTRATION, POLICY_VIOLATION,
        SUSPICIOUS_ACTIVITY, VULNERABILITY_EXPLOITED, PHISHING_ATTEMPT,
        DDOS_ATTEMPT, ACCOUNT_TAKEOVER, PRIVILEGE_ESCALATION
    }

    public enum EventSeverity {
        CRITICAL, HIGH, MEDIUM, LOW, INFO
    }

    public enum EventCategory {
        NETWORK_SECURITY, APPLICATION_SECURITY, DATA_SECURITY,
        IDENTITY_ACCESS, COMPLIANCE, THREAT_INTELLIGENCE
    }

    public enum EventStatus {
        OPEN, INVESTIGATING, RESOLVED, FALSE_POSITIVE, ESCALATED
    }

    public SecurityEvent() {
        this.createdAt = Instant.now();
        this.detectedAt = Instant.now();
        this.status = EventStatus.OPEN;
        this.isFalsePositive = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }

    public EventSeverity getSeverity() { return severity; }
    public void setSeverity(EventSeverity severity) { this.severity = severity; }

    public EventCategory getCategory() { return category; }
    public void setCategory(EventCategory category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSourceIp() { return sourceIp; }
    public void setSourceIp(String sourceIp) { this.sourceIp = sourceIp; }

    public String getSourceHost() { return sourceHost; }
    public void setSourceHost(String sourceHost) { this.sourceHost = sourceHost; }

    public String getTargetResource() { return targetResource; }
    public void setTargetResource(String targetResource) { this.targetResource = targetResource; }

    public String getTargetResourceType() { return targetResourceType; }
    public void setTargetResourceType(String targetResourceType) { this.targetResourceType = targetResourceType; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Boolean getAuthenticated() { return authenticated; }
    public void setAuthenticated(Boolean authenticated) { this.authenticated = authenticated; }

    public Map<String, Object> getDetails() { return details; }
    public void setDetails(Map<String, Object> details) { this.details = details; }

    public EventStatus getStatus() { return status; }
    public void setStatus(EventStatus status) { this.status = status; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public String getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; }

    public Instant getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Instant resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public Boolean getIsFalsePositive() { return isFalsePositive; }
    public void setIsFalsePositive(Boolean isFalsePositive) { this.isFalsePositive = isFalsePositive; }

    public Instant getDetectedAt() { return detectedAt; }
    public void setDetectedAt(Instant detectedAt) { this.detectedAt = detectedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public void markAsFalsePositive(String resolvedBy) {
        this.status = EventStatus.FALSE_POSITIVE;
        this.isFalsePositive = true;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = Instant.now();
    }

    public void resolve(String resolvedBy, String notes) {
        this.status = EventStatus.RESOLVED;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = Instant.now();
        this.resolutionNotes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityEvent that = (SecurityEvent) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
