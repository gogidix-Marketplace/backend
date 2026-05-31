package com.gogidix.infrastructure.lockservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain model representing the result of a lock release operation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockReleaseResult {

    /**
     * Whether the release was successful.
     */
    private boolean success;

    /**
     * The lock that was released.
     */
    private Lock releasedLock;

    /**
     * Error message if release failed.
     */
    private String errorMessage;

    /**
     * When the release occurred.
     */
    private LocalDateTime releasedAt;

    /**
     * Whether the lock was already expired.
     */
    private boolean wasExpired;

    /**
     * Create a successful release result.
     *
     * @param lock the lock that was released
     * @return successful LockReleaseResult
     */
    public static LockReleaseResult success(Lock lock) {
        return LockReleaseResult.builder()
                .success(true)
                .releasedLock(lock)
                .releasedAt(LocalDateTime.now())
                .wasExpired(false)
                .build();
    }

    /**
     * Create a failed release result.
     *
     * @param errorMessage the error message
     * @return failed LockReleaseResult
     */
    public static LockReleaseResult failure(String errorMessage) {
        return LockReleaseResult.builder()
                .success(false)
                .errorMessage(errorMessage)
                .releasedAt(LocalDateTime.now())
                .wasExpired(false)
                .build();
    }

    /**
     * Create an expired release result.
     *
     * @param lock the expired lock
     * @return LockReleaseResult indicating the lock was expired
     */
    public static LockReleaseResult expired(Lock lock) {
        return LockReleaseResult.builder()
                .success(true)
                .releasedLock(lock)
                .releasedAt(LocalDateTime.now())
                .wasExpired(true)
                .build();
    }
}
