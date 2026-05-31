package com.gogidix.infrastructure.lockservice.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for Lock domain model.
 */
@DisplayName("Lock Domain Model Tests")
class LockTest {

    @Test
    @DisplayName("Should create lock with builder")
    void shouldCreateLockWithBuilder() {
        Lock lock = Lock.builder("tenant-1", "resource-123", "holder-1")
            .lockId(UUID.randomUUID())
            .lockType(LockType.EXCLUSIVE)
            .status(LockStatus.LOCKED)
            .holderName("Test Holder")
            .ttlSeconds(300L)
            .acquiredAt(LocalDateTime.now())
            .build();

        assertThat(lock).isNotNull();
        assertThat(lock.getTenantId()).isEqualTo("tenant-1");
        assertThat(lock.getResourceKey()).isEqualTo("resource-123");
        assertThat(lock.getHolderId()).isEqualTo("holder-1");
        assertThat(lock.getLockType()).isEqualTo(LockType.EXCLUSIVE);
        assertThat(lock.getStatus()).isEqualTo(LockStatus.LOCKED);
        assertThat(lock.getHolderName()).isEqualTo("Test Holder");
        assertThat(lock.getTtlSeconds()).isEqualTo(300L);
    }

    @Test
    @DisplayName("Should identify expired lock")
    void shouldIdentifyExpiredLock() {
        Lock lock = Lock.builder("tenant-1", "resource-123", "holder-1")
            .lockId(UUID.randomUUID())
            .expiresAt(LocalDateTime.now().minusSeconds(10))
            .build();

        assertThat(lock.isExpired()).isTrue();
    }

    @Test
    @DisplayName("Should identify non-expired lock")
    void shouldIdentifyNonExpiredLock() {
        Lock lock = Lock.builder("tenant-1", "resource-123", "holder-1")
            .lockId(UUID.randomUUID())
            .expiresAt(LocalDateTime.now().plusSeconds(100))
            .build();

        assertThat(lock.isExpired()).isFalse();
    }

    @Test
    @DisplayName("Should identify lock without expiration")
    void shouldIdentifyLockWithoutExpiration() {
        Lock lock = Lock.builder("tenant-1", "resource-123", "holder-1")
            .lockId(UUID.randomUUID())
            .expiresAt(null)
            .build();

        assertThat(lock.isExpired()).isFalse();
    }

    @Test
    @DisplayName("Should check if lock is held by specific holder")
    void shouldCheckIfLockIsHeldBySpecificHolder() {
        Lock lock = Lock.builder("tenant-1", "resource-123", "holder-1")
            .lockId(UUID.randomUUID())
            .holderId("holder-1")
            .build();

        assertThat(lock.isHeldBy("holder-1")).isTrue();
        assertThat(lock.isHeldBy("holder-2")).isFalse();
    }

    @Test
    @DisplayName("Should check if lock belongs to tenant")
    void shouldCheckIfLockBelongsToTenant() {
        Lock lock = Lock.builder("tenant-1", "resource-123", "holder-1")
            .lockId(UUID.randomUUID())
            .tenantId("tenant-1")
            .build();

        assertThat(lock.belongsToTenant("tenant-1")).isTrue();
        assertThat(lock.belongsToTenant("tenant-2")).isFalse();
    }

    @Test
    @DisplayName("Should generate correct Redis key")
    void shouldGenerateCorrectRedisKey() {
        Lock lock = Lock.builder("tenant-1", "resource-123", "holder-1")
            .lockId(UUID.randomUUID())
            .build();

        assertThat(lock.getRedisKey()).isEqualTo("lock:tenant-1:resource-123");
    }

    @Test
    @DisplayName("Should calculate remaining TTL correctly")
    void shouldCalculateRemainingTtlCorrectly() {
        int ttlSeconds = 300;
        Lock lock = Lock.builder("tenant-1", "resource-123", "holder-1")
            .lockId(UUID.randomUUID())
            .expiresAt(LocalDateTime.now().plusSeconds(ttlSeconds))
            .build();

        long remainingTtl = lock.getRemainingTtlSeconds();
        assertThat(remainingTtl).isGreaterThan(0);
        assertThat(remainingTtl).isLessThanOrEqualTo(ttlSeconds);
    }

    @Test
    @DisplayName("Should return -1 for lock without expiration")
    void shouldReturnMinusOneForLockWithoutExpiration() {
        Lock lock = Lock.builder("tenant-1", "resource-123", "holder-1")
            .lockId(UUID.randomUUID())
            .expiresAt(null)
            .build();

        assertThat(lock.getRemainingTtlSeconds()).isEqualTo(-1);
    }
}
