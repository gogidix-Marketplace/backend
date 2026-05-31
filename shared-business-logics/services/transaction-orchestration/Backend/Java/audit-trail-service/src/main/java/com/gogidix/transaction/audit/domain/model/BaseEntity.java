package com.gogidix.transaction.audit.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base entity class for all JPA entities.
 * Provides common fields for auditing and multi-tenancy.
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     * Tenant ID for multi-tenancy isolation
     */
    @Column(name = "tenant_id", length = 50)
    protected String tenantId;

    /**
     * Date and time when the entity was created
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    /**
     * Date and time when the entity was last updated
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    /**
     * User who created this entity
     */
    @CreatedBy
    @Column(name = "created_by", length = 100)
    protected String createdBy;

    /**
     * User who last updated this entity
     */
    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    protected String updatedBy;

    /**
     * Version field for optimistic locking
     */
    @Version
    @Column(name = "version")
    protected Long version;

    /**
     * Indicates whether the entity is active
     */
    @Column(name = "is_active")
    protected Boolean isActive = true;

    /**
     * Indicates whether the entity is deleted (soft delete)
     */
    @Column(name = "is_deleted")
    protected Boolean isDeleted = false;

    /**
     * Additional metadata stored as JSON
     */
    @Column(name = "metadata", columnDefinition = "TEXT")
    protected String metadata;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
