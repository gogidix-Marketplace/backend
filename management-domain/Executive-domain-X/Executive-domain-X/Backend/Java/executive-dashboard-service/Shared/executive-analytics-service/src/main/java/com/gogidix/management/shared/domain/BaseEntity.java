package com.gogidix.management.shared.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

@Document
@CompoundIndex(name = "tenant_entity_idx", def = "{'tenantId': 1, '_id': 1}")
public abstract class BaseEntity implements Persistable<String> {
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
    @Transient
    private boolean isNew = false;

    protected BaseEntity(String tenantId) {
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.id = java.util.UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.isNew = true;
    }

    protected BaseEntity() {
    }

    @Override
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void touch(String updaterId) {
        this.updatedAt = Instant.now();
        if (updaterId != null) {
            this.updatedBy = updaterId;
        }
    }

    public void touch() {
        touch(null);
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    public void markAsSaved() {
        this.isNew = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "id='" + id + '\'' +
            ", tenantId='" + tenantId + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
    }
}
