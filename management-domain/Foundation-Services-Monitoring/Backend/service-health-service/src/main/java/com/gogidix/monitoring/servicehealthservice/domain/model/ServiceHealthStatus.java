package com.gogidix.monitoring.servicehealthservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Domain model for service health status.
 * Tracks the current health state of a service.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceHealthStatus {

    /**
     * Unique identifier for this health status record.
     */
    private String id;

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
    private com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint.ServiceType serviceType;

    /**
     * Current health status.
     */
    private HealthStatus status;

    /**
     * Health score (0-100).
     */
    private Integer healthScore;

    /**
     * Score components.
     */
    private ScoreComponents scoreComponents;

    /**
     * Additional details.
     */
    private Map<String, Object> details;

    /**
     * Last health check timestamp.
     */
    private Instant lastCheckAt;

    /**
     * Consecutive failure count.
     */
    private Integer consecutiveFailures;

    /**
     * Last successful check timestamp.
     */
    private Instant lastSuccessAt;

    /**
     * Last failure timestamp.
     */
    private Instant lastFailureAt;

    /**
     * Average response time in milliseconds.
     */
    private Double averageResponseTime;

    /**
     * Error rate (0-1).
     */
    private Double errorRate;

    /**
     * Uptime percentage (0-100).
     */
    private Double uptimePercentage;

    /**
     * Service instance information.
     */
    private String instanceId;

    /**
     * Service host.
     */
    private String host;

    /**
     * Timestamp when this record was created.
     */
    private Instant createdAt;

    /**
     * Health status enumeration.
     */
    public enum HealthStatus {
        HEALTHY,
        DEGRADED,
        UNHEALTHY,
        DOWN,
        UNKNOWN
    }

    /**
     * Score components for health calculation.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScoreComponents {
        private Double uptimeScore;
        private Double responseTimeScore;
        private Double errorRateScore;
        private Double dependencyScore;
    }

    /**
     * Determines if the service is currently healthy.
     */
    public boolean isHealthy() {
        return status == HealthStatus.HEALTHY;
    }

    /**
     * Determines if the service is down.
     */
    public boolean isDown() {
        return status == HealthStatus.DOWN;
    }

    /**
     * Calculates the health status from the health score.
     */
    public static HealthStatus statusFromScore(Integer score) {
        if (score == null) {
            return HealthStatus.UNKNOWN;
        }
        if (score >= 90) {
            return HealthStatus.HEALTHY;
        } else if (score >= 70) {
            return HealthStatus.DEGRADED;
        } else if (score >= 50) {
            return HealthStatus.UNHEALTHY;
        } else {
            return HealthStatus.DOWN;
        }
    }
}
