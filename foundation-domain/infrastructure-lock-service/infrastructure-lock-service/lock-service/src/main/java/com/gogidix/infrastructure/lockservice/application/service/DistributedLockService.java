package com.gogidix.infrastructure.lockservice.application.service;

import com.gogidix.infrastructure.lockservice.domain.model.*;
import com.gogidix.infrastructure.lockservice.domain.repository.LockRepository;
import com.gogidix.infrastructure.lockservice.domain.repository.LockStatisticsRepository;
import com.gogidix.infrastructure.lockservice.infrastructure.redis.RedisLockStatisticsRepository;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Service for managing distributed locks with retry mechanisms.
 * Provides high-level lock operations with configurable retry policies.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DistributedLockService {

    private final LockRepository lockRepository;
    private final LockStatisticsRepository statisticsRepository;
    private final RedisLockStatisticsRepository redisStatisticsRepository;
    private final RetryRegistry retryRegistry;

    @Value("${lock.default-ttl-seconds:300}")
    private long defaultTtlSeconds;

    @Value("${lock.default-wait-time-seconds:30}")
    private long defaultWaitTimeSeconds;

    @Value("${lock.default-max-retries:3}")
    private int defaultMaxRetries;

    @Value("${lock.default-retry-interval-ms:100}")
    private long defaultRetryIntervalMs;

    @Value("${lock.enable-auto-cleanup:true}")
    private boolean enableAutoCleanup;

    /**
     * Acquire a lock with default settings.
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @param holderId    the holder ID
     * @return acquisition result
     */
    public LockAcquisitionResult acquireLock(String tenantId, String resourceKey, String holderId) {
        LockRequest request = LockRequest.essential(tenantId, resourceKey, holderId)
            .lockType(LockType.EXCLUSIVE)
            .ttlSeconds(defaultTtlSeconds)
            .build();

        return acquireLock(request);
    }

    /**
     * Acquire a lock based on the request.
     *
     * @param request the lock request
     * @return acquisition result
     */
    public LockAcquisitionResult acquireLock(LockRequest request) {
        log.debug("Attempting to acquire lock for resource: {} by holder: {}",
            request.getResourceKey(), request.getHolderId());

        // Set defaults if not provided
        if (request.getTtlSeconds() == null) {
            request.setTtlSeconds(defaultTtlSeconds);
        }
        if (request.getWaitTimeSeconds() == null) {
            request.setWaitTimeSeconds(defaultWaitTimeSeconds);
        }
        if (request.getMaxRetries() == null) {
            request.setMaxRetries(defaultMaxRetries);
        }
        if (request.getRetryIntervalMs() == null) {
            request.setRetryIntervalMs(defaultRetryIntervalMs);
        }

        // Update statistics
        statisticsRepository.incrementTotalOperations(request.getTenantId());

        // Try immediate acquisition first
        LockAcquisitionResult result = tryAcquireWithRetry(request);

        if (result.isAcquired()) {
            redisStatisticsRepository.recordAcquisitionTime(request.getTenantId(),
                Duration.between(result.getAttemptedAt(), result.getAcquiredAt()).toMillis());
            redisStatisticsRepository.incrementActiveLocks(request.getTenantId(), request.getLockType());

            // Update peak concurrent locks
            long currentCount = lockRepository.getActiveLockCount(request.getTenantId());
            statisticsRepository.updatePeakConcurrentLocks(request.getTenantId(), currentCount);
        } else {
            statisticsRepository.incrementFailedAttempts(request.getTenantId());
        }

        return result;
    }

    /**
     * Acquire a lock with wait/retry mechanism.
     *
     * @param request the lock request
     * @return acquisition result
     */
    public LockAcquisitionResult acquireLockWithWait(LockRequest request) {
        log.debug("Attempting to acquire lock with wait for resource: {} by holder: {}",
            request.getResourceKey(), request.getHolderId());

        // Set defaults if not provided
        if (request.getTtlSeconds() == null) {
            request.setTtlSeconds(defaultTtlSeconds);
        }
        if (request.getWaitTimeSeconds() == null) {
            request.setWaitTimeSeconds(defaultWaitTimeSeconds);
        }
        if (request.getMaxRetries() == null) {
            request.setMaxRetries((int) (request.getWaitTimeSeconds() * 1000 / defaultRetryIntervalMs));
        }
        if (request.getRetryIntervalMs() == null) {
            request.setRetryIntervalMs(defaultRetryIntervalMs);
        }

        statisticsRepository.incrementTotalOperations(request.getTenantId());

        LocalDateTime startTime = LocalDateTime.now();
        int attempts = 0;
        int maxAttempts = request.getMaxRetries() + 1;

        while (attempts < maxAttempts) {
            attempts++;
            LockAcquisitionResult result = lockRepository.acquireLock(request);

            if (result.isAcquired()) {
                redisStatisticsRepository.recordAcquisitionTime(request.getTenantId(),
                    Duration.between(startTime, LocalDateTime.now()).toMillis());
                redisStatisticsRepository.incrementActiveLocks(request.getTenantId(), request.getLockType());

                long currentCount = lockRepository.getActiveLockCount(request.getTenantId());
                statisticsRepository.updatePeakConcurrentLocks(request.getTenantId(), currentCount);

                log.debug("Lock acquired for resource: {} after {} attempts", request.getResourceKey(), attempts);
                return result;
            }

            // Check if we've exceeded wait time
            if (Duration.between(startTime, LocalDateTime.now()).getSeconds() >= request.getWaitTimeSeconds()) {
                log.debug("Lock acquisition timed out for resource: {} after {} attempts", request.getResourceKey(), attempts);
                statisticsRepository.incrementFailedAttempts(request.getTenantId());
                return LockAcquisitionResult.timeout(attempts);
            }

            // Wait before retry
            if (attempts < maxAttempts) {
                try {
                    Thread.sleep(request.getRetryIntervalMs());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return LockAcquisitionResult.failure("Lock acquisition interrupted");
                }
            }
        }

        log.debug("Lock acquisition failed for resource: {} after {} attempts", request.getResourceKey(), attempts);
        statisticsRepository.incrementFailedAttempts(request.getTenantId());
        return LockAcquisitionResult.timeout(attempts);
    }

    /**
     * Try to acquire a lock using resilience4j retry.
     *
     * @param request the lock request
     * @return acquisition result
     */
    private LockAcquisitionResult tryAcquireWithRetry(LockRequest request) {
        Retry retry = retryRegistry.retry("lockAcquisition");
        retry = Retry.of(request.getResourceKey(), retry.getRetryConfig());

        Supplier<LockAcquisitionResult> retryableSupplier = Retry.decorateSupplier(retry,
            () -> lockRepository.acquireLock(request));

        try {
            LockAcquisitionResult result = retryableSupplier.get();
            return result;
        } catch (Exception e) {
            log.error("Error during lock acquisition with retry", e);
            return LockAcquisitionResult.failure("Error acquiring lock: " + e.getMessage());
        }
    }

    /**
     * Release a lock.
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @param holderId    the holder ID
     * @param lockId      the lock ID
     * @return release result
     */
    @Transactional
    public LockReleaseResult releaseLock(String tenantId, String resourceKey, String holderId, String lockId) {
        log.debug("Releasing lock for resource: {} by holder: {}", resourceKey, holderId);

        statisticsRepository.incrementTotalOperations(tenantId);

        Optional<Lock> existingLock = lockRepository.findLock(tenantId, resourceKey);
        if (existingLock.isEmpty()) {
            return LockReleaseResult.failure("Lock not found");
        }

        Lock lock = existingLock.get();

        // Record hold time
        if (lock.getAcquiredAt() != null) {
            long holdTimeMs = Duration.between(lock.getAcquiredAt(), LocalDateTime.now()).toMillis();
            redisStatisticsRepository.recordHoldTime(tenantId, holdTimeMs);
        }

        LockReleaseResult result = lockRepository.releaseLock(tenantId, resourceKey, holderId, lockId);

        if (result.isSuccess()) {
            redisStatisticsRepository.decrementActiveLocks(tenantId, lock.getLockType());
            log.debug("Lock released successfully for resource: {}", resourceKey);
        }

        return result;
    }

    /**
     * Extend a lock's TTL.
     *
     * @param tenantId           the tenant ID
     * @param resourceKey        the resource key
     * @param holderId           the holder ID
     * @param lockId             the lock ID
     * @param additionalTtlSeconds additional TTL in seconds
     * @return true if extended successfully
     */
    public boolean extendLock(String tenantId, String resourceKey, String holderId,
                             String lockId, long additionalTtlSeconds) {
        log.debug("Extending lock for resource: {} by holder: {} by {} seconds",
            resourceKey, holderId, additionalTtlSeconds);

        statisticsRepository.incrementTotalOperations(tenantId);

        boolean extended = lockRepository.extendLock(tenantId, resourceKey, holderId, lockId, additionalTtlSeconds);

        if (extended) {
            log.debug("Lock extended successfully for resource: {}", resourceKey);
        } else {
            log.warn("Failed to extend lock for resource: {}", resourceKey);
            statisticsRepository.incrementFailedAttempts(tenantId);
        }

        return extended;
    }

    /**
     * Check if a resource is locked.
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @return true if locked
     */
    public boolean isLocked(String tenantId, String resourceKey) {
        return lockRepository.isLocked(tenantId, resourceKey);
    }

    /**
     * Get lock information.
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @return lock if present
     */
    public Optional<Lock> getLock(String tenantId, String resourceKey) {
        return lockRepository.findLock(tenantId, resourceKey);
    }

    /**
     * Get all active locks for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of active locks
     */
    public List<Lock> getActiveLocks(String tenantId) {
        return lockRepository.findActiveLocksByTenant(tenantId);
    }

    /**
     * Get all locks for a holder.
     *
     * @param tenantId the tenant ID
     * @param holderId the holder ID
     * @return list of locks
     */
    public List<Lock> getLocksByHolder(String tenantId, String holderId) {
        return lockRepository.findLocksByHolder(tenantId, holderId);
    }

    /**
     * Force unlock a resource (admin operation).
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @return true if unlocked
     */
    public boolean forceUnlock(String tenantId, String resourceKey) {
        log.warn("Force unlock requested for resource: {} in tenant: {}", resourceKey, tenantId);

        Optional<Lock> lock = lockRepository.findLock(tenantId, resourceKey);
        if (lock.isPresent()) {
            redisStatisticsRepository.decrementActiveLocks(tenantId, lock.get().getLockType());
        }

        return lockRepository.forceUnlock(tenantId, resourceKey);
    }

    /**
     * Clean up expired locks.
     *
     * @param tenantId the tenant ID (null for all tenants)
     * @return number of locks cleaned up
     */
    public long cleanupExpiredLocks(String tenantId) {
        log.debug("Starting cleanup of expired locks for tenant: {}", tenantId);

        List<Lock> expiredLocks = lockRepository.findExpiredLocks(tenantId);

        long cleanedUp = lockRepository.cleanupExpiredLocks(tenantId);

        // Update statistics
        if (tenantId != null) {
            for (long i = 0; i < cleanedUp; i++) {
                redisStatisticsRepository.incrementExpiredLocks(tenantId);
            }
        }

        log.debug("Cleaned up {} expired locks for tenant: {}", cleanedUp, tenantId);
        return cleanedUp;
    }

    /**
     * Get lock statistics.
     *
     * @param tenantId the tenant ID
     * @return lock statistics
     */
    public LockStatistics getStatistics(String tenantId) {
        return statisticsRepository.getStatistics(tenantId);
    }

    /**
     * Get global lock statistics.
     *
     * @return global lock statistics
     */
    public LockStatistics getGlobalStatistics() {
        return statisticsRepository.getGlobalStatistics();
    }

    /**
     * Get active lock counts by tenant.
     *
     * @return map of tenant ID to active lock count
     */
    public Map<String, Long> getActiveLockCountsByTenant() {
        return statisticsRepository.getActiveLockCountByTenant();
    }
}
