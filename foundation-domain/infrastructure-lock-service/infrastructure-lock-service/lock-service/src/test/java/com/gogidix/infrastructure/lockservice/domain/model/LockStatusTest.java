package com.gogidix.infrastructure.lockservice.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for LockStatus enum.
 */
@DisplayName("LockStatus Enum Tests")
class LockStatusTest {

    @Test
    @DisplayName("Should identify active status")
    void shouldIdentifyActiveStatus() {
        assertThat(LockStatus.LOCKED.isActive()).isTrue();
        assertThat(LockStatus.AVAILABLE.isActive()).isFalse();
        assertThat(LockStatus.EXPIRED.isActive()).isFalse();
        assertThat(LockStatus.RELEASED.isActive()).isFalse();
        assertThat(LockStatus.FAILED.isActive()).isFalse();
        assertThat(LockStatus.WAITING.isActive()).isFalse();
    }

    @Test
    @DisplayName("Should identify available status")
    void shouldIdentifyAvailableStatus() {
        assertThat(LockStatus.AVAILABLE.isAvailable()).isTrue();
        assertThat(LockStatus.LOCKED.isAvailable()).isFalse();
        assertThat(LockStatus.EXPIRED.isAvailable()).isFalse();
        assertThat(LockStatus.RELEASED.isAvailable()).isFalse();
        assertThat(LockStatus.FAILED.isAvailable()).isFalse();
        assertThat(LockStatus.WAITING.isAvailable()).isFalse();
    }

    @Test
    @DisplayName("Should identify terminated status")
    void shouldIdentifyTerminatedStatus() {
        assertThat(LockStatus.EXPIRED.isTerminated()).isTrue();
        assertThat(LockStatus.RELEASED.isTerminated()).isTrue();
        assertThat(LockStatus.FAILED.isTerminated()).isTrue();
        assertThat(LockStatus.LOCKED.isTerminated()).isFalse();
        assertThat(LockStatus.AVAILABLE.isTerminated()).isFalse();
        assertThat(LockStatus.WAITING.isTerminated()).isFalse();
    }

    @Test
    @DisplayName("Should find status by code")
    void shouldFindStatusByCode() {
        assertThat(LockStatus.fromCode("LOCKED")).isEqualTo(LockStatus.LOCKED);
        assertThat(LockStatus.fromCode("AVAILABLE")).isEqualTo(LockStatus.AVAILABLE);
        assertThat(LockStatus.fromCode("EXPIRED")).isEqualTo(LockStatus.EXPIRED);
        assertThat(LockStatus.fromCode("RELEASED")).isEqualTo(LockStatus.RELEASED);
        assertThat(LockStatus.fromCode("FAILED")).isEqualTo(LockStatus.FAILED);
        assertThat(LockStatus.fromCode("WAITING")).isEqualTo(LockStatus.WAITING);
        assertThat(LockStatus.fromCode("INVALID")).isNull();
    }
}
