package com.gogidix.infrastructure.lockservice.domain.repository;

import com.gogidix.infrastructure.lockservice.domain.model.Lock;
import com.gogidix.infrastructure.lockservice.domain.model.LockAcquisitionResult;
import com.gogidix.infrastructure.lockservice.domain.model.LockReleaseResult;
import com.gogidix.infrastructure.lockservice.domain.model.LockRequest;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for lock operations.
 * Defines the contract for lock persistence and retrieval.
 */
public interface LockRepository {

    /**
     * Attempt to acquire a lock based on the request.
     *
     * @param request the lock request
     * @return result of the acquisition attempt
     */
    LockAcquisitionResult acquireLock(LockRequest request);

    /**
     * Release a held lock.
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @param holderId    the holder ID
     * @param lockId      the lock ID
     * @return result of the release operation
     */
    LockReleaseResult releaseLock(String tenantId, String resourceKey, String holderId, String lockId);

    /**
     * Extend the TTL of an existing lock.
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @param holderId    the holder ID
     * @param lockId      the lock ID
     * @param additionalTtlSeconds additional TTL in seconds
     * @return true if the lock was extended, false otherwise
     */
    boolean extendLock(String tenantId, String resourceKey, String holderId, String lockId, long additionalTtlSeconds);

    /**
     * Find a lock by tenant and resource key.
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @return the lock if found
     */
    Optional<Lock> findLock(String tenantId, String resourceKey);

    /**
     * Find a lock by its unique ID.
     *
     * @param lockId the lock ID
     * @return the lock if found
     */
    Optional<Lock> findLockById(String lockId);

    /**
     * Find all active locks for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of active locks
     */
    List<Lock> findActiveLocksByTenant(String tenantId);

    /**
     * Find all locks held by a specific holder.
     *
     * @param tenantId the tenant ID
     * @param holderId the holder ID
     * @return list of locks held by the holder
     */
    List<Lock> findLocksByHolder(String tenantId, String holderId);

    /**
     * Find all expired locks for cleanup.
     *
     * @param tenantId the tenant ID (null for all tenants)
     * @return list of expired locks
     */
    List<Lock> findExpiredLocks(String tenantId);

    /**
     * Check if a resource is currently locked.
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @return true if the resource is locked
     */
    boolean isLocked(String tenantId, String resourceKey);

    /**
     * Check if a resource is locked by a specific holder.
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @param holderId    the holder ID
     * @return true if the resource is locked by the holder
     */
    boolean isLockedBy(String tenantId, String resourceKey, String holderId);

    /**
     * Force unlock a resource (admin operation).
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @return true if the lock was removed
     */
    boolean forceUnlock(String tenantId, String resourceKey);

    /**
     * Clean up expired locks.
     *
     * @param tenantId the tenant ID (null for all tenants)
     * @return number of locks cleaned up
     */
    long cleanupExpiredLocks(String tenantId);

    /**
     * Get total active lock count for a tenant.
     *
     * @param tenantId the tenant ID
     * @return count of active locks
     */
    long getActiveLockCount(String tenantId);
}
