package com.gogidix.shared.infrastructure.services.security.analytics.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Document(collection = "security_events")
public class SecurityEvent {
    @Indexed
    private TenantId tenantId;
    @Id
    private String id;
    @Indexed
    private String eventId;
    @NotBlank
    @Indexed
    private String eventType;
    @Indexed
    private String severity;
    private String source;
    private String description;
    @Indexed
    private LocalDateTime timestamp;
    @CreatedDate
    private LocalDateTime createdAt;
    protected SecurityEvent() {}
    public SecurityEvent(TenantId tenantId, String eventType, String severity) {
        this.tenantId = tenantId;
        this.eventType = eventType;
        this.severity = severity;
        this.eventId = java.util.UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }
    // Getters and Setters
    public TenantId getTenantId() { return tenantId; }
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
