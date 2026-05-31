package com.gogidix.infrastructure.lockservice.application.service;

import com.gogidix.infrastructure.lockservice.domain.model.Lock;
import com.gogidix.infrastructure.lockservice.domain.repository.LockRepository;
import com.gogidix.infrastructure.lockservice.infrastructure.redis.RedisLockStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Scheduled service for cleaning up expired locks.
 * Runs periodically to remove locks that have exceeded their TTL.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LockCleanupService {

    private final LockRepository lockRepository;
    private final RedisLockStatisticsRepository statisticsRepository;

    @Value("${lock.cleanup.enabled:true}")
    private boolean cleanupEnabled;

    @Value("${lock.cleanup.batch-size:100}")
    private int cleanupBatchSize;

    /**
     * Scheduled cleanup task that runs every hour.
     * Cleans up expired locks for all tenants.
     */
    @Scheduled(cron = "${lock.cleanup.cron:0 0 * * * ?}")
    public void scheduledCleanup() {
        if (!cleanupEnabled) {
            log.debug("Lock cleanup is disabled");
            return;
        }

        log.info("Starting scheduled lock cleanup");
        long startTime = System.currentTimeMillis();

        try {
            long cleanedUp = lockRepository.cleanupExpiredLocks(null);
            long duration = System.currentTimeMillis() - startTime;

            log.info("Completed scheduled lock cleanup: {} locks cleaned up in {} ms",
                cleanedUp, duration);
        } catch (Exception e) {
            log.error("Error during scheduled lock cleanup", e);
        }
    }

    /**
     * Cleanup expired locks for a specific tenant.
     *
     * @param tenantId the tenant ID
     * @return number of locks cleaned up
     */
    public long cleanupTenantLocks(String tenantId) {
        log.info("Starting lock cleanup for tenant: {}", tenantId);
        long startTime = System.currentTimeMillis();

        try {
            long cleanedUp = lockRepository.cleanupExpiredLocks(tenantId);

            // Update statistics
            List<Lock> expiredLocks = lockRepository.findExpiredLocks(tenantId);
            for (Lock lock : expiredLocks) {
                statisticsRepository.incrementExpiredLocks(tenantId);
            }

            long duration = System.currentTimeMillis() - startTime;
            log.info("Completed lock cleanup for tenant {}: {} locks cleaned up in {} ms",
                tenantId, cleanedUp, duration);

            return cleanedUp;
        } catch (Exception e) {
            log.error("Error during lock cleanup for tenant: {}", tenantId, e);
            return 0;
        }
    }

    /**
     * Get cleanup status information.
     *
     * @return cleanup status
     */
    public CleanupStatus getCleanupStatus() {
        List<Lock> allExpiredLocks = lockRepository.findExpiredLocks(null);

        return CleanupStatus.builder()
            .cleanupEnabled(cleanupEnabled)
            .expiredLocksCount(allExpiredLocks.size())
            .batchSize(cleanupBatchSize)
            .build();
    }

    /**
     * Status information for the cleanup service.
     */
    @lombok.Builder
    @lombok.Data
    public static class CleanupStatus {
        private boolean cleanupEnabled;
        private long expiredLocksCount;
        private int batchSize;
    }
}
