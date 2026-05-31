package com.gogidix.courier.tenantservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing a Tenant.
 * Multi-tenant configuration management for courier services.
 * All queries MUST filter by tenantId for data isolation.
 */
@Document(collection = "tenants")
@CompoundIndex(name = "idx_tenant_id", def = "{'tenantId': 1, 'id': 1}")
public class Tenant {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("status")
    private TenantStatus status;

    @Field("config")
    private TenantConfig config;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("activated_at")
    private Instant activatedAt;

    @Field("deactivated_at")
    private Instant deactivatedAt;

    /**
     * Default constructor for persistence.
     */
    protected Tenant() {
    }

    /**
     * Create a new Tenant.
     *
     * @param tenantId    the unique tenant identifier
     * @param name        the tenant name
     * @param description the tenant description
     */
    public Tenant(String tenantId, String name, String description) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.name = Objects.requireNonNull(name, "name is required");
        this.description = description;
        this.status = TenantStatus.PENDING;
        this.config = new TenantConfig();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Domain Logic Methods

    /**
     * Activate the tenant.
     *
     * @throws IllegalStateException if tenant is already active
     */
    public void activate() {
        if (this.status == TenantStatus.ACTIVE) {
            throw new IllegalStateException("Tenant is already active");
        }
        if (this.status == TenantStatus.TERMINATED) {
            throw new IllegalStateException("Cannot activate a terminated tenant");
        }
        this.status = TenantStatus.ACTIVE;
        this.activatedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Deactivate the tenant.
     *
     * @throws IllegalStateException if tenant is not active
     */
    public void deactivate() {
        if (this.status != TenantStatus.ACTIVE) {
            throw new IllegalStateException("Cannot deactivate tenant with status: " + this.status);
        }
        this.status = TenantStatus.INACTIVE;
        this.deactivatedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Terminate the tenant.
     *
     * @throws IllegalStateException if tenant has active operations
     */
    public void terminate() {
        if (this.status == TenantStatus.TERMINATED) {
            throw new IllegalStateException("Tenant is already terminated");
        }
        this.status = TenantStatus.TERMINATED;
        this.deactivatedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Update tenant details.
     *
     * @param name        the new name
     * @param description the new description
     */
    public void updateDetails(String name, String description) {
        if (this.status == TenantStatus.TERMINATED) {
            throw new IllegalStateException("Cannot update terminated tenant");
        }
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        this.description = description;
        this.updatedAt = Instant.now();
    }

    /**
     * Update tenant configuration.
     *
     * @param config the new configuration
     */
    public void updateConfig(TenantConfig config) {
        if (this.status == TenantStatus.TERMINATED) {
            throw new IllegalStateException("Cannot update config for terminated tenant");
        }
        this.config = Objects.requireNonNull(config, "config cannot be null");
        this.updatedAt = Instant.now();
    }

    /**
     * Update a specific configuration setting.
     *
     * @param key   the configuration key
     * @param value the configuration value
     */
    public void updateConfigValue(String key, Object value) {
        if (this.status == TenantStatus.TERMINATED) {
            throw new IllegalStateException("Cannot update config for terminated tenant");
        }
        if (this.config == null) {
            this.config = new TenantConfig();
        }
        this.config.setSetting(key, value);
        this.updatedAt = Instant.now();
    }

    /**
     * Check if the tenant is active.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return this.status == TenantStatus.ACTIVE;
    }

    /**
     * Check if the tenant can be modified.
     *
     * @return true if modifiable, false otherwise
     */
    public boolean isModifiable() {
        return this.status != TenantStatus.TERMINATED;
    }

    /**
     * Validate the tenant state.
     *
     * @throws IllegalArgumentException if validation fails
     */
    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("name must not exceed 100 characters");
        }
        if (config == null) {
            throw new IllegalArgumentException("config is required");
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TenantStatus getStatus() {
        return status;
    }

    public TenantConfig getConfig() {
        return config;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getActivatedAt() {
        return activatedAt;
    }

    public Instant getDeactivatedAt() {
        return deactivatedAt;
    }

    // Setters for persistence/MongoDB mapping
    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    protected void setStatus(TenantStatus status) {
        this.status = status;
    }

    protected void setConfig(TenantConfig config) {
        this.config = config;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void setActivatedAt(Instant activatedAt) {
        this.activatedAt = activatedAt;
    }

    protected void setDeactivatedAt(Instant deactivatedAt) {
        this.deactivatedAt = deactivatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tenant tenant = (Tenant) o;
        return Objects.equals(id, tenant.id) &&
                Objects.equals(tenantId, tenant.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId);
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "id='" + id + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    /**
     * Tenant status enum.
     */
    public enum TenantStatus {
        PENDING,
        ACTIVE,
        INACTIVE,
        TERMINATED
    }
}
