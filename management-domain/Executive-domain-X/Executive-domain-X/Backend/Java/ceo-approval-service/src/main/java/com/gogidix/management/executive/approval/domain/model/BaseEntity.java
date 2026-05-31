package com.gogidix.management.executive.approval.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;
import java.util.Objects;

/**
 * Base entity for all domain models
 * Provides common fields like id, tenantId, timestamps, and soft delete support
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
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

    @Field("deleted_at")
    protected Instant deletedAt;

    @Field("active")
    protected boolean active = true;

    public void updateTimestamp() {
        this.updatedAt = Instant.now();
    }

    /**
     * Soft delete - marks entity as deleted
     */
    public void markAsDeleted() {
        this.deletedAt = Instant.now();
        this.active = false;
    }

    /**
     * Check if entity is active (not deleted)
     */
    public boolean isActive() {
        return active && deletedAt == null;
    }

    /**
     * Restore a soft-deleted entity
     */
    public void restore() {
        this.deletedAt = null;
        this.active = true;
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
