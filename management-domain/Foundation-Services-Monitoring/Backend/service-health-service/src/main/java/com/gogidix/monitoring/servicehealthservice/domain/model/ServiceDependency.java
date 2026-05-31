package com.gogidix.monitoring.servicehealthservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Domain model for service dependencies.
 * Tracks which services depend on which other services.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDependency {

    /**
     * Unique identifier.
     */
    private String id;

    /**
     * Tenant identifier.
     */
    private String tenantId;

    /**
     * Service that has the dependency (the dependent).
     */
    private String serviceName;

    /**
     * Service that is depended upon (the dependency).
     */
    private String dependsOnService;

    /**
     * Type of dependency.
     */
    private DependencyType dependencyType;

    /**
     * Whether this dependency is critical.
     */
    private Boolean isCritical;

    /**
     * Dependency health impact score (0-1).
     */
    private Double healthImpact;

    /**
     * Last time this dependency was verified.
     */
    private Instant lastVerifiedAt;

    /**
     * Dependency status.
     */
    private DependencyStatus status;

    /**
     * Additional metadata.
     */
    private List<DependencyEndpoint> endpoints;

    /**
     * Timestamp when created.
     */
    private Instant createdAt;

    /**
     * Timestamp when updated.
     */
    private Instant updatedAt;

    /**
     * Dependency type enumeration.
     */
    public enum DependencyType {
        REST_API,
        GRPC,
        MESSAGE_QUEUE,
        DATABASE,
        CACHE,
        EVENT_STREAM,
        INTERNAL
    }

    /**
     * Dependency status enumeration.
     */
    public enum DependencyStatus {
        ACTIVE,
        INACTIVE,
        DEGRADED,
        FAILED,
        UNKNOWN
    }

    /**
     * Endpoint information for the dependency.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DependencyEndpoint {
        private String url;
        private String method;
        private Double averageLatency;
        private Double successRate;
    }
}
