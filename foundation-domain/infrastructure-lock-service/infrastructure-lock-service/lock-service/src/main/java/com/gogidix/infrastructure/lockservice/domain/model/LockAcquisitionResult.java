package com.gogidix.infrastructure.lockservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain model representing the result of a lock acquisition attempt.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockAcquisitionResult {

    /**
     * Whether the lock was successfully acquired.
     */
    private boolean acquired;

    /**
     * The acquired lock (if successful).
     */
    private Lock lock;

    /**
     * Error message if acquisition failed.
     */
    private String errorMessage;

    /**
     * When the acquisition attempt was made.
     */
    private LocalDateTime attemptedAt;

    /**
     * When the lock was acquired (if successful).
     */
    private LocalDateTime acquiredAt;

    /**
     * Number of retries made during acquisition.
     */
    private Integer retryCount;

    /**
     * Whether the acquisition failed due to timeout.
     */
    private boolean timedOut;

    /**
     * Create a successful acquisition result.
     *
     * @param lock       the acquired lock
     * @param retryCount number of retries made
     * @return successful LockAcquisitionResult
     */
    public static LockAcquisitionResult success(Lock lock, Integer retryCount) {
        return LockAcquisitionResult.builder()
                .acquired(true)
                .lock(lock)
                .attemptedAt(lock.getAcquiredAt())
                .acquiredAt(LocalDateTime.now())
                .retryCount(retryCount)
                .timedOut(false)
                .build();
    }

    /**
     * Create a failed acquisition result.
     *
     * @param errorMessage the error message
     * @return failed LockAcquisitionResult
     */
    public static LockAcquisitionResult failure(String errorMessage) {
        LocalDateTime now = LocalDateTime.now();
        return LockAcquisitionResult.builder()
                .acquired(false)
                .errorMessage(errorMessage)
                .attemptedAt(now)
                .retryCount(0)
                .timedOut(false)
                .build();
    }

    /**
     * Create a timeout acquisition result.
     *
     * @param retryCount number of retries made
     * @return timed out LockAcquisitionResult
     */
    public static LockAcquisitionResult timeout(Integer retryCount) {
        LocalDateTime now = LocalDateTime.now();
        return LockAcquisitionResult.builder()
                .acquired(false)
                .errorMessage("Lock acquisition timed out after " + retryCount + " retries")
                .attemptedAt(now)
                .retryCount(retryCount)
                .timedOut(true)
                .build();
    }
}
