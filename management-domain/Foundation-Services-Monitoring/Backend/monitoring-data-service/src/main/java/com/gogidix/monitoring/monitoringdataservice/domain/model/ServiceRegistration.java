package com.gogidix.monitoring.monitoringdataservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Domain model for service registration.
 * Tracks which services are registered for monitoring and their configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRegistration {

    /**
     * Unique service identifier.
     */
    private String serviceId;

    /**
     * Tenant identifier.
     */
    private String tenantId;

    /**
     * Service name.
     */
    private String serviceName;

    /**
     * Service type.
     */
    private MetricDataPoint.ServiceType serviceType;

    /**
     * Service category for grouping.
     */
    private String category;

    /**
     * Service version.
     */
    private String version;

    /**
     * Service description.
     */
    private String description;

    /**
     * Base URL of the service.
     */
    private String baseUrl;

    /**
     * Health check endpoint.
     */
    private String healthEndpoint;

    /**
     * Metrics endpoint.
     */
    private String metricsEndpoint;

    /**
     * Collection interval in seconds.
     */
    private Integer collectionInterval;

    /**
     * Whether the service is enabled for monitoring.
     */
    @Builder.Default
    private Boolean enabled = true;

    /**
     * Tags/labels for the service.
     */
    private Map<String, String> tags;

    /**
     * Metadata about the service.
     */
    private Map<String, Object> metadata;

    /**
     * Registration timestamp.
     */
    private Instant registeredAt;

    /**
     * Last heartbeat timestamp.
     */
    private Instant lastHeartbeat;

    /**
     * Service status.
     */
    @Builder.Default
    private ServiceStatus status = ServiceStatus.REGISTERED;

    /**
     * Service status enumeration.
     */
    public enum ServiceStatus {
        REGISTERED,
        ACTIVE,
        INACTIVE,
        DEGRADED,
        UNREGISTERED
    }

    /**
     * Checks if service is currently active.
     */
    public boolean isActive() {
        return status == ServiceStatus.ACTIVE && enabled;
    }

    /**
     * Updates the heartbeat timestamp.
     */
    public void updateHeartbeat() {
        this.lastHeartbeat = Instant.now();
        if (this.status == ServiceStatus.REGISTERED || this.status == ServiceStatus.INACTIVE) {
            this.status = ServiceStatus.ACTIVE;
        }
    }

    /**
     * Marks service as inactive.
     */
    public void markInactive() {
        this.status = ServiceStatus.INACTIVE;
    }

    /**
     * Gets full health check URL.
     */
    public String getHealthCheckUrl() {
        if (baseUrl != null && healthEndpoint != null) {
            return baseUrl + healthEndpoint;
        }
        return null;
    }

    /**
     * Gets full metrics URL.
     */
    public String getMetricsUrl() {
        if (baseUrl != null && metricsEndpoint != null) {
            return baseUrl + metricsEndpoint;
        }
        return null;
    }
}
