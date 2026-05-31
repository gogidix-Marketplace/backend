package com.gogidix.infrastructure.lockservice.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for LockRequest domain model.
 */
@DisplayName("LockRequest Domain Model Tests")
class LockRequestTest {

    @Test
    @DisplayName("Should create lock request with builder")
    void shouldCreateLockRequestWithBuilder() {
        LockRequest request = LockRequest.builder()
            .tenantId("tenant-1")
            .resourceKey("resource-123")
            .holderId("holder-1")
            .lockType(LockType.EXCLUSIVE)
            .ttlSeconds(300L)
            .build();

        assertThat(request.getTenantId()).isEqualTo("tenant-1");
        assertThat(request.getResourceKey()).isEqualTo("resource-123");
        assertThat(request.getHolderId()).isEqualTo("holder-1");
        assertThat(request.getLockType()).isEqualTo(LockType.EXCLUSIVE);
        assertThat(request.getTtlSeconds()).isEqualTo(300L);
    }

    @Test
    @DisplayName("Should create request with essential builder")
    void shouldCreateRequestWithEssentialBuilder() {
        LockRequest request = LockRequest.essential("tenant-1", "resource-123", "holder-1")
            .lockType(LockType.SHARED)
            .build();

        assertThat(request.getTenantId()).isEqualTo("tenant-1");
        assertThat(request.getResourceKey()).isEqualTo("resource-123");
        assertThat(request.getHolderId()).isEqualTo("holder-1");
        assertThat(request.getLockType()).isEqualTo(LockType.SHARED);
    }

    @Test
    @DisplayName("Should convert TTL to duration")
    void shouldConvertTtlToDuration() {
        LockRequest request = LockRequest.builder()
            .tenantId("tenant-1")
            .resourceKey("resource-123")
            .holderId("holder-1")
            .ttlSeconds(300L)
            .build();

        Duration duration = request.getTtlDuration();
        assertThat(duration).isNotNull();
        assertThat(duration.getSeconds()).isEqualTo(300);
    }

    @Test
    @DisplayName("Should return null duration when TTL is null")
    void shouldReturnNullDurationWhenTtlIsNull() {
        LockRequest request = LockRequest.builder()
            .tenantId("tenant-1")
            .resourceKey("resource-123")
            .holderId("holder-1")
            .ttlSeconds(null)
            .build();

        assertThat(request.getTtlDuration()).isNull();
    }

    @Test
    @DisplayName("Should convert wait time to duration")
    void shouldConvertWaitTimeToDuration() {
        LockRequest request = LockRequest.builder()
            .tenantId("tenant-1")
            .resourceKey("resource-123")
            .holderId("holder-1")
            .waitTimeSeconds(30L)
            .build();

        Duration duration = request.getWaitDuration();
        assertThat(duration).isNotNull();
        assertThat(duration.getSeconds()).isEqualTo(30);
    }
}
