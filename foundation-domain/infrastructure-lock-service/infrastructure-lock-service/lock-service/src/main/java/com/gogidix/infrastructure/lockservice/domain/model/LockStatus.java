package com.gogidix.infrastructure.lockservice.domain.model;

import lombok.Getter;

/**
 * Enumeration representing the current status of a distributed lock.
 */
@Getter
public enum LockStatus {

    /**
     * Lock is currently held and active.
     */
    LOCKED("LOCKED", "Resource is currently locked"),

    /**
     * Lock is available to be acquired.
     */
    AVAILABLE("AVAILABLE", "Resource is available for locking"),

    /**
     * Lock has expired due to timeout.
     */
    EXPIRED("EXPIRED", "Lock has expired due to timeout"),

    /**
     * Lock was released intentionally.
     */
    RELEASED("RELEASED", "Lock was intentionally released"),

    /**
     * Lock acquisition failed (e.g., contention timeout).
     */
    FAILED("FAILED", "Lock acquisition failed"),

    /**
     * Lock is in a waiting state for retry.
     */
    WAITING("WAITING", "Lock request is waiting for retry");

    private final String code;
    private final String description;

    LockStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Check if this status represents an active lock.
     *
     * @return true if the lock is active
     */
    public boolean isActive() {
        return this == LOCKED;
    }

    /**
     * Check if this status represents an available lock.
     *
     * @return true if the lock is available
     */
    public boolean isAvailable() {
        return this == AVAILABLE;
    }

    /**
     * Check if this status represents a terminated lock (no longer active).
     *
     * @return true if the lock is terminated
     */
    public boolean isTerminated() {
        return this == EXPIRED || this == RELEASED || this == FAILED;
    }

    /**
     * Find LockStatus by code.
     *
     * @param code the status code
     * @return matching LockStatus or null
     */
    public static LockStatus fromCode(String code) {
        for (LockStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
