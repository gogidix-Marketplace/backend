package com.gogidix.shared.infrastructure.services.security.threat.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Document(collection = "threat_indicators")
public class ThreatIndicator {
    @Indexed
    private TenantId tenantId;
    @Id
    private String id;
    @Indexed
    private String indicatorId;
    @NotBlank
    @Indexed
    private String indicatorType;
    @Indexed
    private String value;
    @Indexed
    private ThreatSeverity severity;
    private String description;
    private Boolean active = true;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    public ThreatIndicator() {}
    public ThreatIndicator(TenantId tenantId, String indicatorType, String value) {
        this.tenantId = tenantId;
        this.indicatorType = indicatorType;
        this.value = value;
        this.indicatorId = java.util.UUID.randomUUID().toString();
    }
    // Getters and Setters
    public TenantId getTenantId() { return tenantId; }
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getIndicatorId() { return indicatorId; }
    public void setIndicatorId(String indicatorId) { this.indicatorId = indicatorId; }
    public String getIndicatorType() { return indicatorType; }
    public void setIndicatorType(String indicatorType) { this.indicatorType = indicatorType; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public ThreatSeverity getSeverity() { return severity; }
    public void setSeverity(ThreatSeverity severity) { this.severity = severity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public enum ThreatSeverity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
