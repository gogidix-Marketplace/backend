package com.gogidix.shared.model.domain.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

/**
 * Rich Domain Model Base Entity - HEXAGONAL ARCHITECTURE TEMPLATE
 * 
 * This serves as the foundation base entity for all Agent B domain models.
 * Demonstrates:
 * - Zero infrastructure dependencies (NO Lombok, JPA, etc.)
 * - Immutable design with builder pattern
 * - Comprehensive audit trail business logic
 * - Entity lifecycle management
 * - Domain-driven design patterns
 * 
 * @author Agent A - Foundation Lead
 * @template-for Agent B Domain Models
 * @version 1.0.0
 */
public abstract class BaseEntity {
    
    // Core entity fields (immutable)
    private final UUID id;
    private final Long version;
    private final String entityType;
    
    // Audit fields (immutable)
    private final LocalDateTime createdAt;
    private final String createdBy;
    private final LocalDateTime updatedAt;
    private final String updatedBy;
    
    // Soft delete fields (immutable)
    private final boolean deleted;
    private final LocalDateTime deletedAt;
    private final String deletedBy;
    private final String deletionReason;
    
    // Entity state fields (immutable)
    private final EntityStatus status;
    private final String statusReason;
    private final LocalDateTime statusChangedAt;
    private final String statusChangedBy;
    
    // Protected constructor for subclasses
    protected BaseEntity(UUID id, Long version, String entityType,
                        LocalDateTime createdAt, String createdBy,
                        LocalDateTime updatedAt, String updatedBy,
                        boolean deleted, LocalDateTime deletedAt, String deletedBy, String deletionReason,
                        EntityStatus status, String statusReason, LocalDateTime statusChangedAt, String statusChangedBy) {
        this.id = id;
        this.version = version != null ? version : 0L;
        this.entityType = entityType;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
        this.deletionReason = deletionReason;
        this.status = status != null ? status : EntityStatus.ACTIVE;
        this.statusReason = statusReason;
        this.statusChangedAt = statusChangedAt;
        this.statusChangedBy = statusChangedBy;
    }
    
    // ==============================================
    // ENTITY BUSINESS LOGIC METHODS - Template
    // ==============================================
    
    /**
     * Business Rule: Check if entity is new (not persisted yet)
     */
    public boolean isNew() {
        return id == null || version == 0L;
    }
    
    /**
     * Business Rule: Check if entity is soft deleted
     */
    public boolean isDeleted() {
        return deleted;
    }
    
    /**
     * Business Rule: Check if entity is active and available
     */
    public boolean isActive() {
        return !deleted && status == EntityStatus.ACTIVE;
    }
    
    /**
     * Business Rule: Check if entity is in draft state
     */
    public boolean isDraft() {
        return status == EntityStatus.DRAFT;
    }
    
    /**
     * Business Rule: Check if entity is suspended/disabled
     */
    public boolean isSuspended() {
        return status == EntityStatus.SUSPENDED;
    }
    
    /**
     * Business Rule: Check if entity is archived
     */
    public boolean isArchived() {
        return status == EntityStatus.ARCHIVED;
    }
    
    /**
     * Business Calculation: Get entity age in days
     */
    public long getAgeInDays() {
        if (createdAt == null) return 0L;
        return Duration.between(createdAt, LocalDateTime.now()).toDays();
    }
    
    /**
     * Business Calculation: Get minutes since last update
     */
    public long getMinutesSinceLastUpdate() {
        LocalDateTime lastUpdate = updatedAt != null ? updatedAt : createdAt;
        if (lastUpdate == null) return 0L;
        return Duration.between(lastUpdate, LocalDateTime.now()).toMinutes();
    }
    
    /**
     * Business Rule: Check if entity was recently updated (within last hour)
     */
    public boolean isRecentlyUpdated() {
        return getMinutesSinceLastUpdate() < 60;
    }
    
    /**
     * Business Rule: Check if entity is stale (not updated for specified days)
     */
    public boolean isStale(int staleDays) {
        long daysSinceUpdate = getMinutesSinceLastUpdate() / (24 * 60);
        return daysSinceUpdate > staleDays;
    }
    
    /**
     * Business Rule: Check if entity can be deleted
     */
    public boolean canBeDeleted() {
        return !deleted && status != EntityStatus.PROCESSING;
    }
    
    /**
     * Business Rule: Check if entity can be restored
     */
    public boolean canBeRestored() {
        return deleted && status != EntityStatus.PERMANENTLY_DELETED;
    }
    
    /**
     * Business Rule: Check if entity can be archived
     */
    public boolean canBeArchived() {
        return !deleted && !isArchived() && status != EntityStatus.PROCESSING;
    }
    
    /**
     * Business Rule: Check if entity can be activated
     */
    public boolean canBeActivated() {
        return !deleted && (status == EntityStatus.DRAFT || status == EntityStatus.SUSPENDED);
    }
    
    /**
     * Business Rule: Check if entity requires attention (has issues)
     */
    public boolean requiresAttention() {
        return status == EntityStatus.ERROR || 
               status == EntityStatus.SUSPENDED ||
               (isStale(30) && status == EntityStatus.ACTIVE); // 30 days stale
    }
    
    /**
     * Business Calculation: Get entity health score (0-100)
     */
    public int getHealthScore() {
        int score = 100;
        
        // Deduct for age
        long ageInDays = getAgeInDays();
        if (ageInDays > 365) score -= 20; // Very old
        else if (ageInDays > 180) score -= 10; // Old
        
        // Deduct for staleness
        if (isStale(30)) score -= 15;
        else if (isStale(7)) score -= 5;
        
        // Deduct for status issues
        switch (status) {
            case ERROR -> score -= 50;
            case SUSPENDED -> score -= 30;
            case ARCHIVED -> score -= 20;
            case DRAFT -> score -= 10;
            case PROCESSING -> score -= 5;
            default -> { /* No deduction for active */ }
        }
        
        // Deduct if deleted
        if (deleted) score -= 80;
        
        return Math.max(0, score);
    }
    
    /**
     * Business Logic: Get entity lifecycle stage
     */
    public EntityLifecycleStage getLifecycleStage() {
        if (deleted) return EntityLifecycleStage.DELETED;
        
        return switch (status) {
            case DRAFT, PENDING -> EntityLifecycleStage.CREATION;
            case ACTIVE -> {
                if (getAgeInDays() < 30) yield EntityLifecycleStage.ACTIVE_NEW;
                else if (getAgeInDays() < 365) yield EntityLifecycleStage.ACTIVE_MATURE;
                else yield EntityLifecycleStage.ACTIVE_LEGACY;
            }
            case INACTIVE -> EntityLifecycleStage.SUSPENDED;
            case SUSPENDED -> EntityLifecycleStage.SUSPENDED;
            case ARCHIVED -> EntityLifecycleStage.ARCHIVED;
            case ERROR -> EntityLifecycleStage.ERROR;
            case PROCESSING -> EntityLifecycleStage.TRANSITIONAL;
            case DELETED, PERMANENTLY_DELETED -> EntityLifecycleStage.DELETED;
        };
    }
    
    /**
     * Business Rule: Check if entity has complete audit trail
     */
    public boolean hasCompleteAuditTrail() {
        return id != null &&
               createdAt != null &&
               createdBy != null &&
               updatedAt != null &&
               updatedBy != null &&
               version != null &&
               entityType != null;
    }
    
    /**
     * Business Validation: Validate entity consistency
     */
    public boolean isConsistent() {
        // Basic consistency checks
        if (deleted && deletedAt == null) return false;
        if (deleted && deletedBy == null) return false;
        if (!deleted && (deletedAt != null || deletedBy != null)) return false;
        
        // Status consistency
        if (deleted && status != EntityStatus.PERMANENTLY_DELETED && status != EntityStatus.ARCHIVED) {
            // Deleted entities should have appropriate status
        }
        
        // Timestamp consistency
        if (createdAt != null && updatedAt != null && createdAt.isAfter(updatedAt)) return false;
        if (deletedAt != null && createdAt != null && deletedAt.isBefore(createdAt)) return false;
        
        // Version consistency
        if (version != null && version < 0) return false;
        
        return true;
    }
    
    /**
     * Business Logic: Get time until entity becomes stale
     */
    public Duration getTimeUntilStale(int staleDays) {
        if (isStale(staleDays)) return Duration.ZERO;
        
        LocalDateTime lastUpdate = updatedAt != null ? updatedAt : createdAt;
        if (lastUpdate == null) return Duration.ZERO;
        
        LocalDateTime staleTime = lastUpdate.plusDays(staleDays);
        return Duration.between(LocalDateTime.now(), staleTime);
    }
    
    /**
     * Business Rule: Check if entity was created by specific user
     */
    public boolean wasCreatedBy(String userId) {
        return createdBy != null && createdBy.equals(userId);
    }
    
    /**
     * Business Rule: Check if entity was last updated by specific user
     */
    public boolean wasLastUpdatedBy(String userId) {
        return updatedBy != null && updatedBy.equals(userId);
    }
    
    /**
     * Business Rule: Check if entity was deleted by specific user
     */
    public boolean wasDeletedBy(String userId) {
        return deleted && deletedBy != null && deletedBy.equals(userId);
    }
    
    /**
     * Business Logic: Get entity summary for reporting
     */
    public EntitySummary getSummary() {
        return new EntitySummary(
            id,
            entityType,
            status,
            getLifecycleStage(),
            getHealthScore(),
            getAgeInDays(),
            isRecentlyUpdated(),
            requiresAttention()
        );
    }
    
    /**
     * Abstract method: Each entity must implement its own validation
     */
    public abstract boolean isValid();
    
    /**
     * Abstract method: Each entity must define what constitutes completeness
     */
    public abstract boolean isComplete();
    
    /**
     * Abstract method: Each entity must implement business rule validation
     */
    public abstract ValidationResult validateBusinessRules();
    
    // Getters (immutable access)
    public UUID getId() { return id; }
    public Long getVersion() { return version; }
    public String getEntityType() { return entityType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getUpdatedBy() { return updatedBy; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public String getDeletedBy() { return deletedBy; }
    public String getDeletionReason() { return deletionReason; }
    public EntityStatus getStatus() { return status; }
    public String getStatusReason() { return statusReason; }
    public LocalDateTime getStatusChangedAt() { return statusChangedAt; }
    public String getStatusChangedBy() { return statusChangedBy; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("%s{id=%s, version=%d, status=%s, deleted=%s}", 
                           getClass().getSimpleName(), id, version, status, deleted);
    }
    
    // Static factory method for creating new entities
    public static BaseEntityBuilder builder() {
        return new BaseEntityBuilder();
    }
    
    // Builder class for entity creation
    public static class BaseEntityBuilder {
        private UUID id;
        private Long version = 0L;
        private String entityType;
        private LocalDateTime createdAt;
        private String createdBy;
        private LocalDateTime updatedAt;
        private String updatedBy;
        private boolean deleted = false;
        private LocalDateTime deletedAt;
        private String deletedBy;
        private String deletionReason;
        private EntityStatus status = EntityStatus.DRAFT;
        private String statusReason;
        private LocalDateTime statusChangedAt;
        private String statusChangedBy;
        
        public BaseEntityBuilder id(UUID id) { this.id = id; return this; }
        public BaseEntityBuilder version(Long version) { this.version = version; return this; }
        public BaseEntityBuilder entityType(String entityType) { this.entityType = entityType; return this; }
        public BaseEntityBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public BaseEntityBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public BaseEntityBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public BaseEntityBuilder updatedBy(String updatedBy) { this.updatedBy = updatedBy; return this; }
        public BaseEntityBuilder deleted(boolean deleted) { this.deleted = deleted; return this; }
        public BaseEntityBuilder deletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; return this; }
        public BaseEntityBuilder deletedBy(String deletedBy) { this.deletedBy = deletedBy; return this; }
        public BaseEntityBuilder deletionReason(String deletionReason) { this.deletionReason = deletionReason; return this; }
        public BaseEntityBuilder status(EntityStatus status) { this.status = status; return this; }
        public BaseEntityBuilder statusReason(String statusReason) { this.statusReason = statusReason; return this; }
        public BaseEntityBuilder statusChangedAt(LocalDateTime statusChangedAt) { this.statusChangedAt = statusChangedAt; return this; }
        public BaseEntityBuilder statusChangedBy(String statusChangedBy) { this.statusChangedBy = statusChangedBy; return this; }
        
        public BaseEntityBuilder initializeForCreation(String createdBy) {
            LocalDateTime now = LocalDateTime.now();
            this.id = UUID.randomUUID();
            this.version = 0L;
            this.createdAt = now;
            this.createdBy = createdBy;
            this.updatedAt = now;
            this.updatedBy = createdBy;
            this.status = EntityStatus.DRAFT;
            this.statusChangedAt = now;
            this.statusChangedBy = createdBy;
            return this;
        }
    }
}


/**
 * Entity Lifecycle Stage enumeration for business analysis
 */
enum EntityLifecycleStage {
    CREATION, ACTIVE_NEW, ACTIVE_MATURE, ACTIVE_LEGACY, SUSPENDED, ARCHIVED, ERROR, TRANSITIONAL, DELETED
}

/**
 * Entity Summary record for reporting and analytics
 */
record EntitySummary(
    UUID id,
    String entityType,
    EntityStatus status,
    EntityLifecycleStage lifecycleStage,
    int healthScore,
    long ageInDays,
    boolean recentlyUpdated,
    boolean requiresAttention
) {}

