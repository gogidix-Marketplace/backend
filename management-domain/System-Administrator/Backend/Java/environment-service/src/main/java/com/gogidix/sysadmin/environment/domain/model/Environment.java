package com.gogidix.sysadmin.environment.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "environments")
public class Environment {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    private String name;
    private String displayName;
    private EnvironmentType type;
    private EnvironmentStatus status;
    private String description;
    private String region;
    private String cloudProvider;
    private List<String> resourceIds;
    private Map<String, String> variables;
    private List<String> networkCidrs;
    private Map<String, String> tags;
    private String owner;
    private String costCenter;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant provisionedAt;
    private Instant deprovisionedAt;
    private String createdBy;
    private String lastModifiedBy;
    private Map<String, Object> metadata;

    public enum EnvironmentType {
        DEVELOPMENT, TESTING, STAGING, UAT, PRODUCTION, DR
    }

    public enum EnvironmentStatus {
        ACTIVE, INACTIVE, PROVISIONING, DEPROVISIONING,
        MAINTENANCE, LOCKED, ARCHIVED, ERROR
    }

    public Environment() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = EnvironmentStatus.ACTIVE;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public EnvironmentType getType() { return type; }
    public void setType(EnvironmentType type) { this.type = type; }

    public EnvironmentStatus getStatus() { return status; }
    public void setStatus(EnvironmentStatus status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getCloudProvider() { return cloudProvider; }
    public void setCloudProvider(String cloudProvider) { this.cloudProvider = cloudProvider; }

    public List<String> getResourceIds() { return resourceIds; }
    public void setResourceIds(List<String> resourceIds) { this.resourceIds = resourceIds; }

    public Map<String, String> getVariables() { return variables; }
    public void setVariables(Map<String, String> variables) { this.variables = variables; }

    public List<String> getNetworkCidrs() { return networkCidrs; }
    public void setNetworkCidrs(List<String> networkCidrs) { this.networkCidrs = networkCidrs; }

    public Map<String, String> getTags() { return tags; }
    public void setTags(Map<String, String> tags) { this.tags = tags; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getCostCenter() { return costCenter; }
    public void setCostCenter(String costCenter) { this.costCenter = costCenter; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Instant getProvisionedAt() { return provisionedAt; }
    public void setProvisionedAt(Instant provisionedAt) { this.provisionedAt = provisionedAt; }

    public Instant getDeprovisionedAt() { return deprovisionedAt; }
    public void setDeprovisionedAt(Instant deprovisionedAt) { this.deprovisionedAt = deprovisionedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public void activate() {
        this.status = EnvironmentStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.status = EnvironmentStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    public void lock() {
        this.status = EnvironmentStatus.LOCKED;
        this.updatedAt = Instant.now();
    }

    public void startMaintenance() {
        this.status = EnvironmentStatus.MAINTENANCE;
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Environment environment = (Environment) o;
        return Objects.equals(id, environment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
