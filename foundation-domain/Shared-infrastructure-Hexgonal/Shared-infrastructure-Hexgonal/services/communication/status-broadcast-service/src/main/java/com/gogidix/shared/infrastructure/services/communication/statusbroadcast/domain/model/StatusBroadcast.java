package com.gogidix.shared.infrastructure.services.communication.statusbroadcast.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Status Broadcast entity with multi-tenant support
 */
@Document(collection = "status_broadcasts")
public class StatusBroadcast {
    
    @Id
    private String id;
    
    @Indexed
    private String tenantId;
    
    @Indexed
    private String broadcastType; // SYSTEM, SERVICE, INCIDENT, MAINTENANCE
    
    @Indexed
    private String status; // DRAFT, SCHEDULED, SENT, EXPIRED
    
    private String title;
    private String message;
    private String severity; // INFO, WARNING, ERROR, CRITICAL
    
    @Indexed
    private LocalDateTime scheduledAt;
    
    @Indexed
    private LocalDateTime expiresAt;
    
    @Indexed
    private LocalDateTime sentAt;
    
    private Map<String, Object> metadata;
    private String targetAudience; // ALL, ADMINS, USERS, TENANTS
    private String[] targetTenantIds;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private String createdBy;
    
    protected StatusBroadcast() {
        // MongoDB
    }
    
    public StatusBroadcast(String tenantId, String broadcastType, String title, String message) {
        this.tenantId = tenantId;
        this.broadcastType = broadcastType;
        this.title = title;
        this.message = message;
        this.status = "DRAFT";
        this.severity = "INFO";
        this.targetAudience = "ALL";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Business logic methods
    public void send() {
        this.status = "SENT";
        this.sentAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void schedule(LocalDateTime scheduledAt) {
        this.status = "SCHEDULED";
        this.scheduledAt = scheduledAt;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void expire() {
        this.status = "EXPIRED";
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isSent() {
        return "SENT".equals(this.status);
    }
    
    public boolean isScheduled() {
        return "SCHEDULED".equals(this.status);
    }
    
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    
    public String getBroadcastType() { return broadcastType; }
    public void setBroadcastType(String broadcastType) { this.broadcastType = broadcastType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public String getTargetAudience() { return targetAudience; }
    public void setTargetAudience(String targetAudience) { this.targetAudience = targetAudience; }
    
    public String[] getTargetTenantIds() { return targetTenantIds; }
    public void setTargetTenantIds(String[] targetTenantIds) { this.targetTenantIds = targetTenantIds; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
