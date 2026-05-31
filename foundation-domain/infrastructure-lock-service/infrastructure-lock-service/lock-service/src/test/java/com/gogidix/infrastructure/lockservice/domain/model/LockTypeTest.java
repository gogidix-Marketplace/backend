package com.gogidix.infrastructure.lockservice.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for LockType enum.
 */
@DisplayName("LockType Enum Tests")
class LockTypeTest {

    @Test
    @DisplayName("Should identify concurrent lock types")
    void shouldIdentifyConcurrentLockTypes() {
        assertThat(LockType.SHARED.allowsConcurrency()).isTrue();
        assertThat(LockType.READ.allowsConcurrency()).isTrue();
        assertThat(LockType.EXCLUSIVE.allowsConcurrency()).isFalse();
        assertThat(LockType.WRITE.allowsConcurrency()).isFalse();
    }

    @Test
    @DisplayName("Should find type by code")
    void shouldFindTypeByCode() {
        assertThat(LockType.fromCode("EXCLUSIVE")).isEqualTo(LockType.EXCLUSIVE);
        assertThat(LockType.fromCode("SHARED")).isEqualTo(LockType.SHARED);
        assertThat(LockType.fromCode("WRITE")).isEqualTo(LockType.WRITE);
        assertThat(LockType.fromCode("READ")).isEqualTo(LockType.READ);
        assertThat(LockType.fromCode("INVALID")).isNull();
    }
}
