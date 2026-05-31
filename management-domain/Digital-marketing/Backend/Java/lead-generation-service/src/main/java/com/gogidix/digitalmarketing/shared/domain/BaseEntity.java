package com.gogidix.digitalmarketing.shared.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Objects;

/**
 * BaseEntity - Base class for all domain entities with multi-tenancy support.
 *
 * <p>All entities MUST extend this class to ensure proper tenant isolation.</p>
 *
 * <p>IMPORTANT: The tenantId field is MANDATORY for multi-tenancy.
 * All repository queries MUST filter by tenantId.</p>
 */
@Document
@CompoundIndex(name = "tenant_entity_idx", def = "{'tenantId': 1, '_id': 1}")
@Getter
@Setter
@SuperBuilder
public abstract class BaseEntity implements Persistable<String> {

    @Id
    protected String id;

    @Indexed
    protected String tenantId;

    protected Instant createdAt;

    protected Instant updatedAt;

    protected String createdBy;

    protected String updatedBy;

    @Transient
    private boolean isNew = false;

    /**
     * Constructor for new entities.
     * Automatically generates ID and sets timestamps.
     *
     * @param tenantId the tenant ID (required)
     * @throws IllegalArgumentException if tenantId is null
     */
    protected BaseEntity(String tenantId) {
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.id = java.util.UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.isNew = true;
    }

    /**
     * Default constructor for JPA/framework use.
     * Subclasses MUST call setTenantId() before persisting.
     */
    protected BaseEntity() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Get the tenant ID for this entity.
     * All queries MUST filter by this field.
     *
     * @return the tenant ID, never null after construction
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * Set the tenant ID for this entity.
     * MUST be set before persisting.
     *
     * @param tenantId the tenant ID
     */
    public void setTenantId(String tenantId) {
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
    }

    /**
     * Get the creation timestamp.
     *
     * @return when this entity was created
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Set the creation timestamp.
     * Typically only called by the framework.
     *
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Get the last update timestamp.
     *
     * @return when this entity was last updated
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Set the last update timestamp.
     * Called automatically during updates.
     *
     * @param updatedAt the update timestamp
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Get the user who created this entity.
     *
     * @return the creator user ID, or null if not set
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Set the user who created this entity.
     *
     * @param createdBy the creator user ID
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the user who last updated this entity.
     *
     * @return the updater user ID, or null if not set
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Set the user who last updated this entity.
     *
     * @param updatedBy the updater user ID
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Update the timestamp and optionally the updater.
     * Call this method before saving updates.
     *
     * @param updaterId the user making the update (optional)
     */
    public void touch(String updaterId) {
        this.updatedAt = Instant.now();
        if (updaterId != null) {
            this.updatedBy = updaterId;
        }
    }

    /**
     * Update the timestamp without changing the updater.
     */
    public void touch() {
        touch(null);
    }

    @Override
    @Transient
    public boolean isNew() {
        return this.isNew;
    }

    /**
     * Mark this entity as not new (after saving).
     */
    public void markAsSaved() {
        this.isNew = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id)
            && Objects.equals(tenantId, that.tenantId);
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
