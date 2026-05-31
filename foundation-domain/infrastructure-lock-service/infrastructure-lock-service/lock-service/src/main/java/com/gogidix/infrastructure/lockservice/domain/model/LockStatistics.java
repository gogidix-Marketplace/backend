package com.gogidix.infrastructure.lockservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Domain model representing lock statistics for monitoring and reporting.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockStatistics {

    /**
     * Tenant ID for these statistics.
     */
    private String tenantId;

    /**
     * Total number of active locks.
     */
    private Long activeLocks;

    /**
     * Total number of expired locks.
     */
    private Long expiredLocks;

    /**
     * Total number of released locks.
     */
    private Long releasedLocks;

    /**
     * Total number of failed lock attempts.
     */
    private Long failedAttempts;

    /**
     * Average lock acquisition time in milliseconds.
     */
    private Double averageAcquisitionTimeMs;

    /**
     * Average lock hold time in milliseconds.
     */
    private Double averageHoldTimeMs;

    /**
     * Lock count by type.
     */
    private Map<LockType, Long> locksByType;

    /**
     * When these statistics were calculated.
     */
    private LocalDateTime calculatedAt;

    /**
     * Total number of lock operations (acquire + release).
     */
    private Long totalOperations;

    /**
     * Peak concurrent locks for this tenant.
     */
    private Long peakConcurrentLocks;

    /**
     * Current waiting lock requests.
     */
    private Long waitingRequests;
}
