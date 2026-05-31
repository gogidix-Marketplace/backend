package com.gogidix.shared.model.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clean domain base entity without infrastructure dependencies.
 * Provides common domain entity behavior and business rules.
 */
@Data
public abstract class DomainEntity {
    
    private UUID id;
    private Long version;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;
    
    /**
     * Determines if this is a new entity (not yet persisted)
     */
    public boolean isNew() {
        return id == null;
    }
    
    /**
     * Determines if the entity has been soft deleted
     */
    public boolean isDeleted() {
        return deleted;
    }
    
    /**
     * Marks the entity as deleted (soft delete)
     */
    public void markAsDeleted(String deletedBy) {
        LocalDateTime now = LocalDateTime.now();
        this.deleted = true;
        this.deletedAt = now;
        this.deletedBy = deletedBy;
        this.updatedAt = now;
        this.updatedBy = deletedBy;
    }
    
    /**
     * Restores a soft-deleted entity
     */
    public void restore(String restoredBy) {
        LocalDateTime now = LocalDateTime.now();
        this.deleted = false;
        this.deletedAt = null;
        this.deletedBy = null;
        this.updatedAt = now;
        this.updatedBy = restoredBy;
    }
    
    /**
     * Updates the entity with audit information
     */
    public void updateWith(String updatedBy) {
        LocalDateTime now = LocalDateTime.now();
        this.updatedAt = now;
        this.updatedBy = updatedBy;
    }
    
    /**
     * Initializes the entity with initial audit data
     */
    public void initializeForCreation(String createdBy) {
        LocalDateTime now = LocalDateTime.now();
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        this.version = 0L;
        this.createdAt = now;
        this.createdBy = createdBy;
        this.updatedAt = now;
        this.updatedBy = createdBy;
        this.deleted = false;
    }
    
    /**
     * Validates if the entity is in a valid state
     */
    public abstract boolean isValid();
    
    /**
     * Gets the entity type for business logic purposes
     */
    public abstract String getEntityType();
    
    /**
     * Determines if this entity can be deleted
     */
    public boolean canBeDeleted() {
        return !isDeleted();
    }
    
    /**
     * Determines if this entity can be restored
     */
    public boolean canBeRestored() {
        return isDeleted();
    }
    
    /**
     * Gets the age of the entity in days
     */
    public long getAgeInDays() {
        if (createdAt == null) return 0;
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toDays();
    }
    
    /**
     * Gets the time since last update in minutes
     */
    public long getMinutesSinceLastUpdate() {
        if (updatedAt == null) return 0;
        return java.time.Duration.between(updatedAt, LocalDateTime.now()).toMinutes();
    }
    
    /**
     * Determines if the entity has been recently updated (within last hour)
     */
    public boolean isRecentlyUpdated() {
        return getMinutesSinceLastUpdate() < 60;
    }
    
    /**
     * Determines if the entity is stale (not updated for more than specified days)
     */
    public boolean isStale(int staleDays) {
        return getMinutesSinceLastUpdate() > (staleDays * 24 * 60);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainEntity)) return false;
        DomainEntity that = (DomainEntity) o;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", version=" + version +
                ", deleted=" + deleted +
                '}';
    }
}