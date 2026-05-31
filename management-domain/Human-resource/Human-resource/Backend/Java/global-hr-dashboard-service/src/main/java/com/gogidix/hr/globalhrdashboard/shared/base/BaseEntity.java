package com.gogidix.hr.globalhrdashboard.shared.base;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;
import java.util.Objects;

/**
 * Base entity class for all domain entities
 * Provides common fields and methods for all entities in the system
 */
public abstract class BaseEntity {

    @Id
    protected String id;

    @Indexed
    @Field("tenant_id")
    protected String tenantId;

    @Field("created_at")
    protected Instant createdAt;

    @Field("updated_at")
    protected Instant updatedAt;

    @Field("created_by")
    protected String createdBy;

    @Field("updated_by")
    protected String updatedBy;

    protected BaseEntity() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    protected BaseEntity(String tenantId) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    protected BaseEntity(String tenantId, String userId) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.createdBy = userId;
        this.updatedBy = userId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void updateTimestamp() {
        this.updatedAt = Instant.now();
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void updateTimestamp(String userId) {
        this.updatedAt = Instant.now();
        this.updatedBy = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
