package com.gogidix.infrastructure.lockservice.domain.repository;

import com.gogidix.infrastructure.lockservice.domain.model.LockStatistics;

import java.util.Map;

/**
 * Domain repository interface for lock statistics.
 * Defines the contract for querying lock metrics and monitoring data.
 */
public interface LockStatisticsRepository {

    /**
     * Get statistics for a specific tenant.
     *
     * @param tenantId the tenant ID
     * @return lock statistics for the tenant
     */
    LockStatistics getStatistics(String tenantId);

    /**
     * Get global statistics across all tenants.
     *
     * @return aggregated lock statistics
     */
    LockStatistics getGlobalStatistics();

    /**
     * Get active lock count by tenant.
     *
     * @return map of tenant ID to active lock count
     */
    Map<String, Long> getActiveLockCountByTenant();

    /**
     * Increment the failed acquisition counter.
     *
     * @param tenantId the tenant ID
     */
    void incrementFailedAttempts(String tenantId);

    /**
     * Increment the total operations counter.
     *
     * @param tenantId the tenant ID
     */
    void incrementTotalOperations(String tenantId);

    /**
     * Record acquisition time for statistics.
     *
     * @param tenantId the tenant ID
     * @param timeMs   the acquisition time in milliseconds
     */
    void recordAcquisitionTime(String tenantId, long timeMs);

    /**
     * Record hold time for statistics.
     *
     * @param tenantId the tenant ID
     * @param timeMs   the hold time in milliseconds
     */
    void recordHoldTime(String tenantId, long timeMs);

    /**
     * Update peak concurrent locks if current is higher.
     *
     * @param tenantId  the tenant ID
     * @param currentCount the current concurrent lock count
     */
    void updatePeakConcurrentLocks(String tenantId, long currentCount);
}
