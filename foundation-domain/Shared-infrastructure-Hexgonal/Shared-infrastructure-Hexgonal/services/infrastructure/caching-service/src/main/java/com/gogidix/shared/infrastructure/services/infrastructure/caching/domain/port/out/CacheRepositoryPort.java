package com.gogidix.shared.infrastructure.services.infrastructure.caching.domain.port.out;

import com.gogidix.shared.infrastructure.services.infrastructure.caching.domain.model.CacheEntry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Output port for CacheEntry repository.
 * Defines the contract for persistence operations.
 */
public interface CacheRepositoryPort {

    /**
     * Save a cache entry.
     *
     * @param entry the cache entry to save
     * @return the saved cache entry
     */
    CacheEntry save(CacheEntry entry);

    /**
     * Find cache entry by tenant ID and key.
     *
     * @param tenantId the tenant ID
     * @param key the cache key
     * @return optional containing the cache entry if found
     */
    Optional<CacheEntry> findByTenantIdAndKey(String tenantId, String key);

    /**
     * Find all cache entries for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of cache entries
     */
    List<CacheEntry> findByTenantId(String tenantId);

    /**
     * Find all expired cache entries for a tenant.
     *
     * @param tenantId the tenant ID
     * @param now the current time
     * @return list of expired cache entries
     */
    List<CacheEntry> findExpiredEntries(String tenantId, LocalDateTime now);

    /**
     * Find cache entries by tenant ID and keys.
     *
     * @param tenantId the tenant ID
     * @param keys set of cache keys
     * @return list of cache entries
     */
    List<CacheEntry> findByTenantIdAndKeyIn(String tenantId, Set<String> keys);

    /**
     * Delete cache entry by tenant ID and key.
     *
     * @param tenantId the tenant ID
     * @param key the cache key
     */
    void deleteByTenantIdAndKey(String tenantId, String key);

    /**
     * Delete all cache entries for a tenant.
     *
     * @param tenantId the tenant ID
     */
    void deleteByTenantId(String tenantId);

    /**
     * Delete cache entries by tenant ID and keys.
     *
     * @param tenantId the tenant ID
     * @param keys set of cache keys
     */
    void deleteByTenantIdAndKeyIn(String tenantId, Set<String> keys);

    /**
     * Delete expired cache entries for a tenant.
     *
     * @param tenantId the tenant ID
     * @param now the current time
     * @return number of deleted entries
     */
    long deleteExpiredEntries(String tenantId, LocalDateTime now);

    /**
     * Count cache entries for a tenant.
     *
     * @param tenantId the tenant ID
     * @return the count of cache entries
     */
    long countByTenantId(String tenantId);

    /**
     * Check if cache entry exists by tenant ID and key.
     *
     * @param tenantId the tenant ID
     * @param key the cache key
     * @return true if cache entry exists
     */
    boolean existsByTenantIdAndKey(String tenantId, String key);
}
