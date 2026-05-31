package com.gogidix.infrastructure.lockservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain model representing a distributed lock.
 * Encapsulates all information about a lock including its holder, type, and lifecycle.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lock {

    /**
     * Unique identifier for this lock instance.
     */
    private UUID lockId;

    /**
     * Tenant identifier for multi-tenant isolation.
     */
    private String tenantId;

    /**
     * Resource being locked (e.g., "order:123", "user:456:profile").
     */
    private String resourceKey;

    /**
     * Type of lock (EXCLUSIVE, SHARED, WRITE, READ).
     */
    private LockType lockType;

    /**
     * Current status of the lock.
     */
    private LockStatus status;

    /**
     * Identifier of the entity holding this lock.
     */
    private String holderId;

    /**
     * Optional holder name for easier identification.
     */
    private String holderName;

    /**
     * When the lock was acquired.
     */
    private LocalDateTime acquiredAt;

    /**
     * When the lock will expire (null for non-expiring locks).
     */
    private LocalDateTime expiresAt;

    /**
     * How long the lock was requested for (in seconds).
     */
    private Long ttlSeconds;

    /**
     * Number of retry attempts made to acquire this lock.
     */
    private Integer retryCount;

    /**
     * Metadata associated with the lock.
     */
    private String metadata;

    /**
     * Version for optimistic locking.
     */
    private Long version;

    /**
     * Check if this lock has expired.
     *
     * @return true if the lock has expired
     */
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Check if the lock is held by a specific holder.
     *
     * @param holder the holder ID to check
     * @return true if the lock is held by the specified holder
     */
    public boolean isHeldBy(String holder) {
        return holderId != null && holderId.equals(holder);
    }

    /**
     * Check if the lock belongs to a specific tenant.
     *
     * @param tenant the tenant ID to check
     * @return true if the lock belongs to the specified tenant
     */
    public boolean belongsToTenant(String tenant) {
        return tenantId != null && tenantId.equals(tenant);
    }

    /**
     * Create a new lock request builder.
     *
     * @param tenantId   the tenant ID
     * @param resourceKey the resource key
     * @param holderId    the holder ID
     * @return a LockBuilder pre-configured with basic information
     */
    public static LockBuilder builder(String tenantId, String resourceKey, String holderId) {
        return new LockBuilder()
                .lockId(UUID.randomUUID())
                .tenantId(tenantId)
                .resourceKey(resourceKey)
                .holderId(holderId)
                .status(LockStatus.AVAILABLE)
                .acquiredAt(LocalDateTime.now())
                .retryCount(0);
    }

    /**
     * Generate the Redis key for storing this lock.
     *
     * @return formatted Redis key
     */
    public String getRedisKey() {
        return String.format("lock:%s:%s", tenantId, resourceKey);
    }

    /**
     * Get remaining time to live in seconds.
     *
     * @return remaining TTL or -1 if no expiration set
     */
    public long getRemainingTtlSeconds() {
        if (expiresAt == null) {
            return -1;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expiresAt)) {
            return 0;
        }
        return java.time.Duration.between(now, expiresAt).getSeconds();
    }
}
