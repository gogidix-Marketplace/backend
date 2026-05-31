package com.gogidix.shared.infrastructure.services.security.management.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Entity: SecurityManagement
 * Represents security management configuration and policies
 */
public class SecurityManagement {

    private String id;
    private String name;
    private String description;
    private String type;
    private String status;
    private String policy;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SecurityManagement() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public SecurityManagement(String name, String description, String type, String policy, String createdBy) {
        this();
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = "ACTIVE";
        this.policy = policy;
        this.createdBy = createdBy;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPolicy() { return policy; }
    public void setPolicy(String policy) { this.policy = policy; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "SecurityManagement{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
