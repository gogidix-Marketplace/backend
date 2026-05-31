package com.gogidix.platform.platform.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Service health status tracking.
 *
 * Monitors health of all platform services:
 * - Service availability
 * - Response times
 * - Error tracking
 * - Consecutive failure detection
 * - Recovery tracking
 */
@Entity
@Table(name = "service_health_status", indexes = {
    @Index(name = "idx_service_health_tenant", columnList = "tenant_id"),
    @Index(name = "idx_service_health_name", columnList = "service_name"),
    @Index(name = "idx_service_health_status", columnList = "status"),
    @Index(name = "idx_service_health_last_check", columnList = "last_health_check")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceHealthStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Service name (e.g., "tenant-registry-service")
     */
    @Column(name = "service_name", nullable = false, length = 255)
    private String serviceName;

    /**
     * Service instance identifier (for multi-instance services)
     */
    @Column(name = "service_instance", length = 255)
    private String serviceInstance;

    /**
     * Current health status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private HealthStatus status;

    /**
     * Last health check timestamp
     */
    @Column(name = "last_health_check", nullable = false)
    private LocalDateTime lastHealthCheck;

    /**
     * Response time in milliseconds
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "response_time_ms")
    private Integer responseTimeMs;

    /**
     * Error message if unhealthy
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /**
     * Number of errors in current window
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "error_count")
    @Builder.Default
    private Integer errorCount = 0;

    /**
     * Number of consecutive failures
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "consecutive_failures")
    @Builder.Default
    private Integer consecutiveFailures = 0;

    /**
     * Last recovery timestamp
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "last_recovery")
    private LocalDateTime lastRecovery;

    /**
     * Additional metadata
     */
    @Column(name = "metadata", columnDefinition = "JSONB")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Update health status
     */
    public void updateHealth(HealthStatus newStatus, Integer responseTimeMs, String errorMessage) {
        this.status = newStatus;
        this.lastHealthCheck = LocalDateTime.now();
        this.responseTimeMs = responseTimeMs;
        this.errorMessage = errorMessage;

        if (newStatus == HealthStatus.UP) {
            if (this.consecutiveFailures > 0) {
                this.lastRecovery = LocalDateTime.now();
            }
            this.consecutiveFailures = 0;
        } else if (newStatus == HealthStatus.DOWN || newStatus == HealthStatus.DEGRADED) {
            this.consecutiveFailures++;
            this.errorCount++;
        }
    }

    /**
     * Check if service is healthy
     */
    public boolean isHealthy() {
        return status == HealthStatus.UP;
    }

    /**
     * Check if service is down
     */
    public boolean isDown() {
        return status == HealthStatus.DOWN;
    }

    /**
     * Check if service is degraded
     */
    public boolean isDegraded() {
        return status == HealthStatus.DEGRADED;
    }

    public enum HealthStatus {
        UP,         // Service is healthy
        DOWN,       // Service is completely down
        DEGRADED,   // Service is partially functional
        UNKNOWN     // Service status is unknown
    }
}
