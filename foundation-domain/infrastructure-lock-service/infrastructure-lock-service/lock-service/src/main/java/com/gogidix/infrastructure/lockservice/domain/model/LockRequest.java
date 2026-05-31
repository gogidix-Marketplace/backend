package com.gogidix.infrastructure.lockservice.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

/**
 * Domain model representing a request to acquire a lock.
 * Contains all parameters needed to request a distributed lock.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockRequest {

    /**
     * Tenant identifier for multi-tenant isolation.
     */
    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    /**
     * Resource being locked (e.g., "order:123", "user:456:profile").
     */
    @NotBlank(message = "Resource key is required")
    private String resourceKey;

    /**
     * Identifier of the entity requesting the lock.
     */
    @NotBlank(message = "Holder ID is required")
    private String holderId;

    /**
     * Optional holder name for easier identification.
     */
    private String holderName;

    /**
     * Type of lock to acquire.
     */
    @NotNull(message = "Lock type is required")
    private LockType lockType;

    /**
     * Time to live for the lock in seconds.
     * If null, uses default TTL from configuration.
     */
    @Positive(message = "TTL must be positive")
    private Long ttlSeconds;

    /**
     * Maximum time to wait for lock acquisition in seconds.
     */
    @Positive(message = "Wait time must be positive")
    private Long waitTimeSeconds;

    /**
     * Maximum number of retry attempts.
     */
    @Positive(message = "Max retries must be positive")
    private Integer maxRetries;

    /**
     * Retry interval in milliseconds.
     */
    @Positive(message = "Retry interval must be positive")
    private Long retryIntervalMs;

    /**
     * Metadata associated with the lock request.
     */
    private String metadata;

    /**
     * Whether to auto-extend the lock before expiration.
     */
    private Boolean autoExtend;

    /**
     * Create a builder with required fields.
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @param holderId    the holder ID
     * @return LockRequestBuilder with required fields set
     */
    public static LockRequestBuilder essential(String tenantId, String resourceKey, String holderId) {
        return new LockRequestBuilder()
                .tenantId(tenantId)
                .resourceKey(resourceKey)
                .holderId(holderId)
                .lockType(LockType.EXCLUSIVE);
    }

    /**
     * Get TTL as Duration.
     *
     * @return Duration representing the TTL
     */
    public Duration getTtlDuration() {
        return ttlSeconds != null ? Duration.ofSeconds(ttlSeconds) : null;
    }

    /**
     * Get wait time as Duration.
     *
     * @return Duration representing the wait time
     */
    public Duration getWaitDuration() {
        return waitTimeSeconds != null ? Duration.ofSeconds(waitTimeSeconds) : null;
    }
}
