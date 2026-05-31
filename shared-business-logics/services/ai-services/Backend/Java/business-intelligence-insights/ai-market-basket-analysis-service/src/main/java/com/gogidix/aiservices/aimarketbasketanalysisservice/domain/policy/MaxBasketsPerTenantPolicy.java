package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.policy;

import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.exception.BusinessException;

/**
 * Business rule policy: Maximum number of segments per tenant.
 * Enforces the limit to prevent resource exhaustion.
 */
public class MaxBasketsPerTenantPolicy {

    private static final int DEFAULT_MAX_SEGMENTS = 100;

    private final int maxBaskets;

    public MaxBasketsPerTenantPolicy() {
        this(DEFAULT_MAX_SEGMENTS);
    }

    public MaxBasketsPerTenantPolicy(int maxBaskets) {
        if (maxBaskets <= 0) {
            throw new IllegalArgumentException("maxBaskets must be positive");
        }
        this.maxBaskets = maxBaskets;
    }

    /**
     * Validate that the tenant can create more segments.
     *
     * @param currentBasketCount the current number of segments
     * @param tenantId            the tenant ID
     * @throws BusinessException if limit is exceeded
     */
    public void validate(int currentBasketCount, String tenantId) {
        if (currentBasketCount >= maxBaskets) {
            throw new BusinessException(
                    String.format("Tenant '%s' has reached the maximum number of segments (%d)",
                            tenantId, maxBaskets),
                    "MAX_SEGMENTS_EXCEEDED"
            );
        }
    }

    /**
     * Check if the tenant can create more segments.
     *
     * @param currentBasketCount the current number of segments
     * @return true if more segments can be created, false otherwise
     */
    public boolean canCreateBasket(int currentBasketCount) {
        return currentBasketCount < maxBaskets;
    }

    /**
     * Get the maximum allowed segments.
     *
     * @return the maximum segments
     */
    public int getMaxBaskets() {
        return maxBaskets;
    }

    /**
     * Calculate remaining segments allowed.
     *
     * @param currentBasketCount the current number of segments
     * @return remaining segments allowed
     */
    public int getRemainingBaskets(int currentBasketCount) {
        return Math.max(0, maxBaskets - currentBasketCount);
    }
}
