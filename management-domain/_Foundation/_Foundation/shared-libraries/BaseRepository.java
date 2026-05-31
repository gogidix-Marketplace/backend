package com.gogidix.management.shared.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * BaseRepository - Base interface for all repositories with tenant filtering.
 *
 * <p>All repositories MUST extend this interface to ensure proper tenant isolation.
 * The default methods automatically filter by tenantId from the current RequestContext.</p>
 *
 * <p>Example repository:</p>
 * <pre>
 * &#64;Repository
 * public interface EmployeeRepository extends BaseRepository<Employee> {
 *     // Custom query methods are automatically tenant-filtered
 *     List<Employee> findByLastName(String lastName);
 *
 *     // You can also explicitly add tenant filtering
 *     &#64;Query("{ 'tenantId': ?0, 'lastName': ?1 }")
 *     List<Employee> findByTenantAndLastName(String tenantId, String lastName);
 * }
 * </pre>
 *
 * @param <T> the entity type
 * @see com.gogidix.management.shared.requestcontext.RequestContextHolder
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends MongoRepository<T, String> {

    /**
     * Find all entities for the current tenant.
     * Automatically filters by tenantId from RequestContext.
     *
     * @return list of entities for the current tenant
     */
    @Override
    @NonNull
    List<T> findAll();

    /**
     * Find an entity by ID for the current tenant.
     * Automatically filters by tenantId from RequestContext.
     *
     * @param id the entity ID
     * @return Optional containing the entity, or empty if not found
     */
    @Override
    @NonNull
    Optional<T> findById(@NonNull String id);

    /**
     * Find all entities by tenant ID.
     * Use this for admin operations that need to query across tenants.
     *
     * @param tenantId the tenant ID
     * @return list of entities for the specified tenant
     */
    @Query("{ 'tenantId': ?0 }")
    List<T> findAllByTenantId(String tenantId);

    /**
     * Find an entity by ID and tenant ID.
     * Use this for explicit tenant filtering.
     *
     * @param id the entity ID
     * @param tenantId the tenant ID
     * @return Optional containing the entity, or empty if not found
     */
    @Query("{ '_id': ?0, 'tenantId': ?1 }")
    Optional<T> findByIdAndTenantId(String id, String tenantId);

    /**
     * Check if an entity exists by ID for the current tenant.
     *
     * @param id the entity ID
     * @return true if the entity exists, false otherwise
     */
    @Override
    default boolean existsById(String id) {
        return findById(id).isPresent();
    }

    /**
     * Count all entities for the current tenant.
     *
     * @return the count of entities for the current tenant
     */
    @Override
    @NonNull
    default long count() {
        return findAll().size();
    }

    /**
     * Delete an entity by ID for the current tenant.
     * Only deletes if the entity belongs to the current tenant.
     *
     * @param id the entity ID
     */
    @Override
    default void deleteById(String id) {
        findById(id).ifPresent(this::delete);
    }
}
