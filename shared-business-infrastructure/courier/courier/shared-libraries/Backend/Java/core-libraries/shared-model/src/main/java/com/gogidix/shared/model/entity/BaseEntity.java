package com.gogidix.shared.model.entity;

import com.gogidix.shared.model.domain.model.EntityStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Facade for BaseEntity - provides simpler import path.
 * <p>
 * This class provides backward compatibility for services expecting the simpler
 * package structure ({@code com.gogidix.shared.model.entity}).
 * All functionality is delegated to the canonical
 * {@link com.gogidix.shared.model.domain.model.BaseEntity} class
 * in the domain.model package.
 * </p>
 * <p>
 * <strong>Note:</strong> This is an abstract base class. Extend this class
 * to create concrete entities, implementing the required validation methods.
 * </p>
 * <p>
 * For builder pattern access, use:
 * <pre>{@code
 * import com.gogidix.shared.model.domain.model.BaseEntity;
 * BaseEntity.BaseEntityBuilder builder = BaseEntity.builder();
 * }</pre>
 * </p>
 *
 * @deprecated Use {@link com.gogidix.shared.model.domain.model.BaseEntity} instead.
 *             This facade exists for backward compatibility with existing service imports.
 */
@Deprecated
public abstract class BaseEntity extends com.gogidix.shared.model.domain.model.BaseEntity {

    /**
     * Protected constructor for subclasses with basic parameters.
     *
     * @param id the entity ID
     * @param version the version for optimistic locking
     * @param entityType the type of entity
     * @param createdAt when the entity was created
     * @param createdBy who created the entity
     * @param updatedAt when the entity was last updated
     * @param updatedBy who last updated the entity
     */
    protected BaseEntity(UUID id, Long version, String entityType,
                        LocalDateTime createdAt, String createdBy,
                        LocalDateTime updatedAt, String updatedBy) {
        super(id, version, entityType, createdAt, createdBy, updatedAt, updatedBy,
              false, null, null, null, EntityStatus.ACTIVE, null, null, null);
    }

    /**
     * Protected constructor for subclasses with all parameters.
     */
    protected BaseEntity(UUID id, Long version, String entityType,
                        LocalDateTime createdAt, String createdBy,
                        LocalDateTime updatedAt, String updatedBy,
                        boolean deleted, LocalDateTime deletedAt, String deletedBy, String deletionReason,
                        EntityStatus status, String statusReason, LocalDateTime statusChangedAt, String statusChangedBy) {
        super(id, version, entityType, createdAt, createdBy, updatedAt, updatedBy,
              deleted, deletedAt, deletedBy, deletionReason,
              status, statusReason, statusChangedAt, statusChangedBy);
    }
}
