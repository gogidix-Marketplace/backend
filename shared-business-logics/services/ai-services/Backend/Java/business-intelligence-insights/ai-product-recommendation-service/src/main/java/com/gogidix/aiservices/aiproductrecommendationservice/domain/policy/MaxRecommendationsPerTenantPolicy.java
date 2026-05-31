package com.gogidix.aiservices.aiproductrecommendationservice.domain.policy;

import com.gogidix.aiservices.aiproductrecommendationservice.shared.exception.BusinessException;

/**
 * Business rule policy: Maximum number of segments per tenant.
 * Enforces the limit to prevent resource exhaustion.
 */
public class MaxRecommendationsPerTenantPolicy {

    private static final int DEFAULT_MAX_SEGMENTS = 100;

    private final int maxRecommendations;

    public MaxRecommendationsPerTenantPolicy() {
        this(DEFAULT_MAX_SEGMENTS);
    }

    public MaxRecommendationsPerTenantPolicy(int maxRecommendations) {
        if (maxRecommendations <= 0) {
            throw new IllegalArgumentException("maxRecommendations must be positive");
        }
        this.maxRecommendations = maxRecommendations;
    }

    /**
     * Validate that the tenant can create more segments.
     *
     * @param currentRecommendationCount the current number of segments
     * @param tenantId            the tenant ID
     * @throws BusinessException if limit is exceeded
     */
    public void validate(int currentRecommendationCount, String tenantId) {
        if (currentRecommendationCount >= maxRecommendations) {
            throw new BusinessException(
                    String.format("Tenant '%s' has reached the maximum number of segments (%d)",
                            tenantId, maxRecommendations),
                    "MAX_SEGMENTS_EXCEEDED"
            );
        }
    }

    /**
     * Check if the tenant can create more segments.
     *
     * @param currentRecommendationCount the current number of segments
     * @return true if more segments can be created, false otherwise
     */
    public boolean canCreateRecommendation(int currentRecommendationCount) {
        return currentRecommendationCount < maxRecommendations;
    }

    /**
     * Get the maximum allowed segments.
     *
     * @return the maximum segments
     */
    public int getMaxRecommendations() {
        return maxRecommendations;
    }

    /**
     * Calculate remaining segments allowed.
     *
     * @param currentRecommendationCount the current number of segments
     * @return remaining segments allowed
     */
    public int getRemainingRecommendations(int currentRecommendationCount) {
        return Math.max(0, maxRecommendations - currentRecommendationCount);
    }
}
