package com.gogidix.dashboard.shared.adapter.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Enterprise base entity for dashboard domain models.
 * 
 * <p>This class provides a clean base for dashboard entities without requiring
 * inheritance from the Foundation shared-model library's BaseEntity, which has
 * different constructor requirements.
 * 
 * <p>This entity uses String-based IDs for compatibility with existing dashboard
 * service schemas, while providing proper audit fields.
 * 
 * <p><strong>Usage Example:</strong>
 * <pre>{@code
 * @Entity
 * @Table(name = "dashboards")
 * public class Dashboard extends DashboardBaseEntity {
 *     
 *     @Column(name = "name")
 *     private String name;
 *     
 *     @Override
 *     public void validateBusinessRules() {
 *         if (name == null || name.isBlank()) {
 *             throw new DashboardValidationException("Dashboard name is required");
 *         }
 *     }
 * }
 * }</pre>
 * 
 * @author Gogidix Dashboard Team
 * @since 1.0.0
 */
@MappedSuperclass
public abstract class DashboardBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Column(name = "is_deleted")
    private Boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        if (active == null) {
            active = true;
        }
        if (deleted == null) {
            deleted = false;
        }
        validateBusinessRules();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        validateBusinessRules();
    }

    /**
     * Validates business rules before persisting the entity.
     * 
     * <p>Subclasses should override this method to implement
     * domain-specific validation rules.
     * 
     * @throws com.gogidix.dashboard.shared.adapter.exception.DashboardValidationException if validation fails
     */
    protected void validateBusinessRules() {
        // Default implementation - no validation
        // Subclasses should override to add business rules
    }

    /**
     * Marks the entity as deleted (soft delete).
     */
    public void softDelete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * Restores a soft-deleted entity.
     */
    public void restore() {
        this.deleted = false;
        this.deletedAt = null;
    }

    /**
     * Activates the entity.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Deactivates the entity.
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Checks if this entity is active and not deleted.
     * 
     * @return true if entity is active and not deleted
     */
    public boolean isAccessible() {
        return Boolean.TRUE.equals(active) && !Boolean.TRUE.equals(deleted);
    }
}
