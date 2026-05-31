package com.gogidix.centralizeddashboard.metrics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * MongoDB Document representing the health status of a service instance
 */
@Document(collection = "service_health")
public class ServiceHealth {

    @Id
    @Field("id")
    @Indexed
    private String id;

    @Field("service_name")
    @Indexed
    private String serviceName;

    @Field("instance_id")
    @Indexed
    private String instanceId;

    @Field("status")
    @Indexed
    private HealthStatus status;

    @Field("last_updated")
    @Indexed
    private LocalDateTime lastUpdated;

    @Field("last_check_time")
    @Indexed
    private LocalDateTime lastCheckTime;

    @Field("details")
    private String details;

    // Default constructor
    public ServiceHealth() {
        this.id = UUID.randomUUID().toString();
        this.lastUpdated = LocalDateTime.now();
        this.lastCheckTime = LocalDateTime.now();
    }

    // Constructor with required fields
    public ServiceHealth(String serviceName, String instanceId, HealthStatus status) {
        this();
        this.serviceName = serviceName;
        this.instanceId = instanceId;
        this.status = status;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public HealthStatus getStatus() {
        return status;
    }

    public void setStatus(HealthStatus status) {
        this.status = status;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(LocalDateTime lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * Update the health status and timestamps
     */
    public void updateStatus(HealthStatus newStatus, String newDetails) {
        this.status = newStatus;
        this.details = newDetails;
        this.lastUpdated = LocalDateTime.now();
        this.lastCheckTime = LocalDateTime.now();
    }

    /**
     * Update just the check time without changing the status
     */
    public void updateCheckTime() {
        this.lastCheckTime = LocalDateTime.now();
    }

    /**
     * Enum representing the health status of a service
     */
    public enum HealthStatus {
        HEALTHY,
        DEGRADED,
        UNHEALTHY,
        UNKNOWN,
        MAINTENANCE,
        DRAINING
    }
}
