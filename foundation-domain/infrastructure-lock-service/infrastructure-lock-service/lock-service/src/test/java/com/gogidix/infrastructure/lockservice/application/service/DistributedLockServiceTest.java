package com.gogidix.infrastructure.lockservice.application.service;

import com.gogidix.infrastructure.lockservice.domain.model.*;
import com.gogidix.infrastructure.lockservice.domain.repository.LockRepository;
import com.gogidix.infrastructure.lockservice.domain.repository.LockStatisticsRepository;
import com.gogidix.infrastructure.lockservice.infrastructure.redis.RedisLockStatisticsRepository;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DistributedLockService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DistributedLockService Tests")
class DistributedLockServiceTest {

    @Mock
    private LockRepository lockRepository;

    @Mock
    private LockStatisticsRepository statisticsRepository;

    @Mock
    private RedisLockStatisticsRepository redisStatisticsRepository;

    @Mock
    private RetryRegistry retryRegistry;

    private DistributedLockService lockService;

    @BeforeEach
    void setUp() {
        Retry retry = Retry.of("lockAcquisition", RetryConfig.custom()
                .maxAttempts(1)
                .waitDuration(Duration.ofMillis(100))
                .build());
        lenient().when(retryRegistry.retry("lockAcquisition")).thenReturn(retry);

        lockService = new DistributedLockService(
            lockRepository,
            statisticsRepository,
            redisStatisticsRepository,
            retryRegistry
        );

        // Set default values
        ReflectionTestUtils.setField(lockService, "defaultTtlSeconds", 300L);
        ReflectionTestUtils.setField(lockService, "defaultWaitTimeSeconds", 30L);
        ReflectionTestUtils.setField(lockService, "defaultMaxRetries", 3);
        ReflectionTestUtils.setField(lockService, "defaultRetryIntervalMs", 100L);
    }

    @Test
    @DisplayName("Should acquire lock successfully")
    void shouldAcquireLockSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        String resourceKey = "resource-123";
        String holderId = "holder-1";

        Lock expectedLock = Lock.builder(tenantId, resourceKey, holderId)
            .lockId(UUID.randomUUID())
            .status(LockStatus.LOCKED)
            .lockType(LockType.EXCLUSIVE)
            .acquiredAt(LocalDateTime.now())
            .build();

        LockAcquisitionResult acquisitionResult = LockAcquisitionResult.success(expectedLock, 0);

        when(lockRepository.acquireLock(any(LockRequest.class))).thenReturn(acquisitionResult);
        when(lockRepository.getActiveLockCount(tenantId)).thenReturn(1L);

        // When
        LockAcquisitionResult result = lockService.acquireLock(tenantId, resourceKey, holderId);

        // Then
        assertThat(result.isAcquired()).isTrue();
        assertThat(result.getLock()).isNotNull();
        assertThat(result.getLock().getResourceKey()).isEqualTo(resourceKey);

        verify(statisticsRepository).incrementTotalOperations(tenantId);
        verify(redisStatisticsRepository).incrementActiveLocks(tenantId, LockType.EXCLUSIVE);
        verify(statisticsRepository).updatePeakConcurrentLocks(eq(tenantId), anyLong());
    }

    @Test
    @DisplayName("Should handle failed lock acquisition")
    void shouldHandleFailedLockAcquisition() {
        // Given
        String tenantId = "tenant-1";
        String resourceKey = "resource-123";
        String holderId = "holder-1";

        LockAcquisitionResult failedResult = LockAcquisitionResult.failure("Resource already locked");

        when(lockRepository.acquireLock(any(LockRequest.class))).thenReturn(failedResult);

        // When
        LockAcquisitionResult result = lockService.acquireLock(tenantId, resourceKey, holderId);

        // Then
        assertThat(result.isAcquired()).isFalse();
        assertThat(result.getErrorMessage()).isNotNull();

        verify(statisticsRepository).incrementFailedAttempts(tenantId);
        verify(redisStatisticsRepository, never()).incrementActiveLocks(anyString(), any());
    }

    @Test
    @DisplayName("Should release lock successfully")
    void shouldReleaseLockSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        String resourceKey = "resource-123";
        String holderId = "holder-1";
        UUID lockId = UUID.randomUUID();

        Lock lock = Lock.builder(tenantId, resourceKey, holderId)
            .lockId(lockId)
            .status(LockStatus.LOCKED)
            .lockType(LockType.EXCLUSIVE)
            .acquiredAt(LocalDateTime.now())
            .build();

        LockReleaseResult releaseResult = LockReleaseResult.success(lock);

        when(lockRepository.findLock(tenantId, resourceKey)).thenReturn(Optional.of(lock));
        when(lockRepository.releaseLock(tenantId, resourceKey, holderId, lockId.toString()))
            .thenReturn(releaseResult);

        // When
        LockReleaseResult result = lockService.releaseLock(tenantId, resourceKey, holderId, lockId.toString());

        // Then
        assertThat(result.isSuccess()).isTrue();

        verify(redisStatisticsRepository).decrementActiveLocks(tenantId, LockType.EXCLUSIVE);
        verify(statisticsRepository).incrementTotalOperations(tenantId);
    }

    @Test
    @DisplayName("Should extend lock successfully")
    void shouldExtendLockSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        String resourceKey = "resource-123";
        String holderId = "holder-1";
        UUID lockId = UUID.randomUUID();
        long additionalTtl = 300L;

        when(lockRepository.extendLock(tenantId, resourceKey, holderId, lockId.toString(), additionalTtl))
            .thenReturn(true);

        // When
        boolean result = lockService.extendLock(tenantId, resourceKey, holderId, lockId.toString(), additionalTtl);

        // Then
        assertThat(result).isTrue();
        verify(statisticsRepository).incrementTotalOperations(tenantId);
    }

    @Test
    @DisplayName("Should check if resource is locked")
    void shouldCheckIfResourceIsLocked() {
        // Given
        String tenantId = "tenant-1";
        String resourceKey = "resource-123";

        when(lockRepository.isLocked(tenantId, resourceKey)).thenReturn(true);

        // When
        boolean result = lockService.isLocked(tenantId, resourceKey);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should get lock by resource key")
    void shouldGetLockByResourceKey() {
        // Given
        String tenantId = "tenant-1";
        String resourceKey = "resource-123";

        Lock lock = Lock.builder(tenantId, resourceKey, "holder-1")
            .lockId(UUID.randomUUID())
            .status(LockStatus.LOCKED)
            .build();

        when(lockRepository.findLock(tenantId, resourceKey)).thenReturn(Optional.of(lock));

        // When
        Optional<Lock> result = lockService.getLock(tenantId, resourceKey);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getResourceKey()).isEqualTo(resourceKey);
    }

    @Test
    @DisplayName("Should get active locks for tenant")
    void shouldGetActiveLocksForTenant() {
        // Given
        String tenantId = "tenant-1";

        Lock lock1 = Lock.builder(tenantId, "resource-1", "holder-1")
            .lockId(UUID.randomUUID())
            .status(LockStatus.LOCKED)
            .build();

        Lock lock2 = Lock.builder(tenantId, "resource-2", "holder-2")
            .lockId(UUID.randomUUID())
            .status(LockStatus.LOCKED)
            .build();

        when(lockRepository.findActiveLocksByTenant(tenantId)).thenReturn(List.of(lock1, lock2));

        // When
        List<Lock> result = lockService.getActiveLocks(tenantId);

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should force unlock resource")
    void shouldForceUnlockResource() {
        // Given
        String tenantId = "tenant-1";
        String resourceKey = "resource-123";

        Lock lock = Lock.builder(tenantId, resourceKey, "holder-1")
            .lockId(UUID.randomUUID())
            .status(LockStatus.LOCKED)
            .lockType(LockType.EXCLUSIVE)
            .build();

        when(lockRepository.findLock(tenantId, resourceKey)).thenReturn(Optional.of(lock));
        when(lockRepository.forceUnlock(tenantId, resourceKey)).thenReturn(true);

        // When
        boolean result = lockService.forceUnlock(tenantId, resourceKey);

        // Then
        assertThat(result).isTrue();
        verify(redisStatisticsRepository).decrementActiveLocks(tenantId, LockType.EXCLUSIVE);
    }

    @Test
    @DisplayName("Should get statistics")
    void shouldGetStatistics() {
        // Given
        String tenantId = "tenant-1";

        LockStatistics stats = LockStatistics.builder()
            .tenantId(tenantId)
            .activeLocks(5L)
            .expiredLocks(2L)
            .releasedLocks(10L)
            .failedAttempts(1L)
            .build();

        when(statisticsRepository.getStatistics(tenantId)).thenReturn(stats);

        // When
        LockStatistics result = lockService.getStatistics(tenantId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTenantId()).isEqualTo(tenantId);
        assertThat(result.getActiveLocks()).isEqualTo(5L);
    }

    @Test
    @DisplayName("Should cleanup expired locks")
    void shouldCleanupExpiredLocks() {
        // Given
        String tenantId = "tenant-1";
        when(lockRepository.cleanupExpiredLocks(tenantId)).thenReturn(3L);

        // When
        long result = lockService.cleanupExpiredLocks(tenantId);

        // Then
        assertThat(result).isEqualTo(3L);
    }

    @Test
    @DisplayName("Should get active lock counts by tenant")
    void shouldGetActiveLockCountsByTenant() {
        // Given
        when(statisticsRepository.getActiveLockCountByTenant())
            .thenReturn(java.util.Map.of("tenant-1", 5L, "tenant-2", 3L));

        // When
        java.util.Map<String, Long> result = lockService.getActiveLockCountsByTenant();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get("tenant-1")).isEqualTo(5L);
    }
}
