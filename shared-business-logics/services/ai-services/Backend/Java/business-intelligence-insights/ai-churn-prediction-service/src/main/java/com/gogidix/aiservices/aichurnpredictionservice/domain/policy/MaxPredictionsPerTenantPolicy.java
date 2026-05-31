package com.gogidix.aiservices.aichurnpredictionservice.domain.policy;

import com.gogidix.aiservices.aichurnpredictionservice.shared.exception.BusinessException;

/**
 * Business rule policy: Maximum number of segments per tenant.
 * Enforces the limit to prevent resource exhaustion.
 */
public class MaxPredictionsPerTenantPolicy {

    private static final int DEFAULT_MAX_SEGMENTS = 100;

    private final int maxPredictions;

    public MaxPredictionsPerTenantPolicy() {
        this(DEFAULT_MAX_SEGMENTS);
    }

    public MaxPredictionsPerTenantPolicy(int maxPredictions) {
        if (maxPredictions <= 0) {
            throw new IllegalArgumentException("maxPredictions must be positive");
        }
        this.maxPredictions = maxPredictions;
    }

    /**
     * Validate that the tenant can create more segments.
     *
     * @param currentPredictionCount the current number of segments
     * @param tenantId            the tenant ID
     * @throws BusinessException if limit is exceeded
     */
    public void validate(int currentPredictionCount, String tenantId) {
        if (currentPredictionCount >= maxPredictions) {
            throw new BusinessException(
                    String.format("Tenant '%s' has reached the maximum number of segments (%d)",
                            tenantId, maxPredictions),
                    "MAX_SEGMENTS_EXCEEDED"
            );
        }
    }

    /**
     * Check if the tenant can create more segments.
     *
     * @param currentPredictionCount the current number of segments
     * @return true if more segments can be created, false otherwise
     */
    public boolean canCreatePrediction(int currentPredictionCount) {
        return currentPredictionCount < maxPredictions;
    }

    /**
     * Get the maximum allowed segments.
     *
     * @return the maximum segments
     */
    public int getMaxPredictions() {
        return maxPredictions;
    }

    /**
     * Calculate remaining segments allowed.
     *
     * @param currentPredictionCount the current number of segments
     * @return remaining segments allowed
     */
    public int getRemainingPredictions(int currentPredictionCount) {
        return Math.max(0, maxPredictions - currentPredictionCount);
    }
}
