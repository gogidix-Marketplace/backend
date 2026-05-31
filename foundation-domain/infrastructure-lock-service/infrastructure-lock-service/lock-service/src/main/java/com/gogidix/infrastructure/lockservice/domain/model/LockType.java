package com.gogidix.infrastructure.lockservice.domain.model;

import lombok.Getter;

/**
 * Enumeration representing different types of distributed locks.
 */
@Getter
public enum LockType {

    /**
     * Exclusive lock - only one holder can acquire.
     */
    EXCLUSIVE("EXCLUSIVE", "Only one holder allowed"),

    /**
     * Shared lock - multiple readers allowed.
     */
    SHARED("SHARED", "Multiple readers allowed"),

    /**
     * Write lock - for write operations, conflicts with other write and shared locks.
     */
    WRITE("WRITE", "Single writer, no concurrent readers or writers"),

    /**
     * Read lock - for read operations, multiple concurrent reads allowed.
     */
    READ("READ", "Multiple concurrent reads allowed");

    private final String code;
    private final String description;

    LockType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Check if this lock type allows concurrency.
     *
     * @return true if concurrent access is allowed
     */
    public boolean allowsConcurrency() {
        return this == SHARED || this == READ;
    }

    /**
     * Find LockType by code.
     *
     * @param code the type code
     * @return matching LockType or null
     */
    public static LockType fromCode(String code) {
        for (LockType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
