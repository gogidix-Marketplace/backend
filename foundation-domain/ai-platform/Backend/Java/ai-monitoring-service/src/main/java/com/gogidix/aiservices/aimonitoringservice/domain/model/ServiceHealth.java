package com.gogidix.aiservices.aimonitoringservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Domain Entity representing a Service Health Status.
 */
@Document(collection = "service_health")
public class ServiceHealth {

    @Id
    private String id;

    @Indexed
    private String serviceName;

    @Indexed
    private String tenantId;

    private HealthStatus status;

    private String message;

    private Map<String, Object> metrics;

    private Instant lastCheckAt;

    private Instant createdAt;

    private Long totalChecks;

    private Long failedChecks;

    public ServiceHealth() {
        this.totalChecks = 0L;
        this.failedChecks = 0L;
    }

    public ServiceHealth(String serviceName, String tenantId) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.serviceName = serviceName;
        this.tenantId = tenantId;
        this.status = HealthStatus.UNKNOWN;
        this.lastCheckAt = Instant.now();
        this.createdAt = Instant.now();
    }

    public void updateStatus(HealthStatus status, String message) {
        this.status = status;
        this.message = message;
        this.lastCheckAt = Instant.now();
        this.totalChecks++;
        if (status == HealthStatus.DOWN || status == HealthStatus.DEGRADED) {
            this.failedChecks++;
        }
    }

    public void updateMetrics(Map<String, Object> metrics) {
        this.metrics = metrics;
        this.lastCheckAt = Instant.now();
    }

    public double getSuccessRate() {
        if (totalChecks == 0) return 0.0;
        return (double) (totalChecks - failedChecks) / totalChecks;
    }

    // Getters
    public String getId() { return id; }
    public String getServiceName() { return serviceName; }
    public String getTenantId() { return tenantId; }
    public HealthStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public Map<String, Object> getMetrics() { return metrics; }
    public Instant getLastCheckAt() { return lastCheckAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Long getTotalChecks() { return totalChecks; }
    public Long getFailedChecks() { return failedChecks; }

    // Setters for persistence
    protected void setId(String id) { this.id = id; }
    protected void setServiceName(String serviceName) { this.serviceName = serviceName; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setStatus(HealthStatus status) { this.status = status; }
    protected void setMessage(String message) { this.message = message; }
    protected void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }
    protected void setLastCheckAt(Instant lastCheckAt) { this.lastCheckAt = lastCheckAt; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    protected void setTotalChecks(Long totalChecks) { this.totalChecks = totalChecks; }
    protected void setFailedChecks(Long failedChecks) { this.failedChecks = failedChecks; }

    public enum HealthStatus {
        UP,
        DOWN,
        DEGRADED,
        UNKNOWN
    }
}
