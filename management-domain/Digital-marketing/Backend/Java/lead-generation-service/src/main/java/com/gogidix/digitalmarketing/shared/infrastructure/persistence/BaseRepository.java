package com.gogidix.digitalmarketing.shared.infrastructure.persistence;

import com.gogidix.digitalmarketing.shared.domain.BaseEntity;
import com.gogidix.digitalmarketing.shared.requestcontext.RequestContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Base Repository with automatic tenant isolation.
 *
 * <p>All repositories MUST extend this interface to ensure tenant isolation.</p>
 *
 * <p>This repository automatically filters all queries by the current tenant's ID
 * from the RequestContext.</p>
 *
 * @param <T> the entity type, must extend BaseEntity
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends MongoRepository<T, String> {

    /**
     * Find all entities for the current tenant.
     *
     * @param tenantId the tenant ID
     * @return list of entities
     */
    @Query("{ 'tenantId': ?0 }")
    List<T> findAllByTenantId(String tenantId);

    /**
     * Find all entities for the current tenant with pagination.
     *
     * @param tenantId the tenant ID
     * @param pageable pagination parameters
     * @return page of entities
     */
    @Query("{ 'tenantId': ?0 }")
    Page<T> findAllByTenantId(String tenantId, Pageable pageable);

    /**
     * Find entity by ID for the current tenant.
     *
     * @param id the entity ID
     * @param tenantId the tenant ID
     * @return optional containing the entity, or empty if not found
     */
    @Query("{ '_id': ?0, 'tenantId': ?1 }")
    Optional<T> findByIdAndTenantId(String id, String tenantId);

    /**
     * Count entities for the current tenant.
     *
     * @param tenantId the tenant ID
     * @return count of entities
     */
    @Query("{ 'tenantId': ?0 }")
    long countByTenantId(String tenantId);

    /**
     * Check if entity exists by ID for the current tenant.
     *
     * @param id the entity ID
     * @param tenantId the tenant ID
     * @return true if exists, false otherwise
     */
    @Query(value = "{ '_id': ?0, 'tenantId': ?1 }", exists = true)
    boolean existsByIdAndTenantId(String id, String tenantId);

    /**
     * Delete entity by ID for the current tenant.
     *
     * @param id the entity ID
     * @param tenantId the tenant ID
     */
    @Query(value = "{ '_id': ?0, 'tenantId': ?1 }", delete = true)
    void deleteByIdAndTenantId(String id, String tenantId);

    /**
     * Get current tenant ID from RequestContext.
     *
     * @return current tenant ID
     * @throws IllegalStateException if RequestContext is not set
     */
    default String getCurrentTenantId() {
        return RequestContextHolder.getTenantId();
    }

    /**
     * Find all entities for the current tenant using RequestContext.
     *
     * @return list of entities
     */
    default List<T> findAllForCurrentTenant() {
        return findAllByTenantId(getCurrentTenantId());
    }

    /**
     * Find entity by ID for the current tenant using RequestContext.
     *
     * @param id the entity ID
     * @return optional containing the entity, or empty if not found
     */
    default Optional<T> findByIdForCurrentTenant(String id) {
        return findByIdAndTenantId(id, getCurrentTenantId());
    }

    /**
     * Count entities for the current tenant using RequestContext.
     *
     * @return count of entities
     */
    default long countForCurrentTenant() {
        return countByTenantId(getCurrentTenantId());
    }

    /**
     * Check if entity exists for current tenant using RequestContext.
     *
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    default boolean existsForCurrentTenant(String id) {
        return existsByIdAndTenantId(id, getCurrentTenantId());
    }

    /**
     * Delete entity by ID for current tenant using RequestContext.
     *
     * @param id the entity ID
     */
    default void deleteForCurrentTenant(String id) {
        deleteByIdAndTenantId(id, getCurrentTenantId());
    }
}
