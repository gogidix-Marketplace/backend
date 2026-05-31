package com.gogidix.aiservices.aicustomersegmentationservice.domain.policy;

import com.gogidix.aiservices.aicustomersegmentationservice.shared.exception.BusinessException;

/**
 * Business rule policy: Maximum number of segments per tenant.
 * Enforces the limit to prevent resource exhaustion.
 */
public class MaxSegmentsPerTenantPolicy {

    private static final int DEFAULT_MAX_SEGMENTS = 100;

    private final int maxSegments;

    public MaxSegmentsPerTenantPolicy() {
        this(DEFAULT_MAX_SEGMENTS);
    }

    public MaxSegmentsPerTenantPolicy(int maxSegments) {
        if (maxSegments <= 0) {
            throw new IllegalArgumentException("maxSegments must be positive");
        }
        this.maxSegments = maxSegments;
    }

    /**
     * Validate that the tenant can create more segments.
     *
     * @param currentSegmentCount the current number of segments
     * @param tenantId            the tenant ID
     * @throws BusinessException if limit is exceeded
     */
    public void validate(int currentSegmentCount, String tenantId) {
        if (currentSegmentCount >= maxSegments) {
            throw new BusinessException(
                    String.format("Tenant '%s' has reached the maximum number of segments (%d)",
                            tenantId, maxSegments),
                    "MAX_SEGMENTS_EXCEEDED"
            );
        }
    }

    /**
     * Check if the tenant can create more segments.
     *
     * @param currentSegmentCount the current number of segments
     * @return true if more segments can be created, false otherwise
     */
    public boolean canCreateSegment(int currentSegmentCount) {
        return currentSegmentCount < maxSegments;
    }

    /**
     * Get the maximum allowed segments.
     *
     * @return the maximum segments
     */
    public int getMaxSegments() {
        return maxSegments;
    }

    /**
     * Calculate remaining segments allowed.
     *
     * @param currentSegmentCount the current number of segments
     * @return remaining segments allowed
     */
    public int getRemainingSegments(int currentSegmentCount) {
        return Math.max(0, maxSegments - currentSegmentCount);
    }
}
