package com.gogidix.aiservices.intelligenceanalysisservice.domain.policy;

import com.gogidix.aiservices.intelligenceanalysisservice.shared.exception.BusinessException;

/**
 * Business rule policy: Maximum number of segments per tenant.
 * Enforces the limit to prevent resource exhaustion.
 */
public class MaxAnalysissPerTenantPolicy {

    private static final int DEFAULT_MAX_SEGMENTS = 100;

    private final int maxAnalysiss;

    public MaxAnalysissPerTenantPolicy() {
        this(DEFAULT_MAX_SEGMENTS);
    }

    public MaxAnalysissPerTenantPolicy(int maxAnalysiss) {
        if (maxAnalysiss <= 0) {
            throw new IllegalArgumentException("maxAnalysiss must be positive");
        }
        this.maxAnalysiss = maxAnalysiss;
    }

    /**
     * Validate that the tenant can create more segments.
     *
     * @param currentAnalysisCount the current number of segments
     * @param tenantId            the tenant ID
     * @throws BusinessException if limit is exceeded
     */
    public void validate(int currentAnalysisCount, String tenantId) {
        if (currentAnalysisCount >= maxAnalysiss) {
            throw new BusinessException(
                    String.format("Tenant '%s' has reached the maximum number of segments (%d)",
                            tenantId, maxAnalysiss),
                    "MAX_SEGMENTS_EXCEEDED"
            );
        }
    }

    /**
     * Check if the tenant can create more segments.
     *
     * @param currentAnalysisCount the current number of segments
     * @return true if more segments can be created, false otherwise
     */
    public boolean canCreateAnalysis(int currentAnalysisCount) {
        return currentAnalysisCount < maxAnalysiss;
    }

    /**
     * Get the maximum allowed segments.
     *
     * @return the maximum segments
     */
    public int getMaxAnalysiss() {
        return maxAnalysiss;
    }

    /**
     * Calculate remaining segments allowed.
     *
     * @param currentAnalysisCount the current number of segments
     * @return remaining segments allowed
     */
    public int getRemainingAnalysiss(int currentAnalysisCount) {
        return Math.max(0, maxAnalysiss - currentAnalysisCount);
    }
}
