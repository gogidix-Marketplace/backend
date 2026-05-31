package com.gogidix.aiservices.aiuserprofilingservice.domain.policy;

import com.gogidix.aiservices.aiuserprofilingservice.shared.exception.BusinessException;

/**
 * Business rule policy: Maximum number of segments per tenant.
 * Enforces the limit to prevent resource exhaustion.
 */
public class MaxProfilesPerTenantPolicy {

    private static final int DEFAULT_MAX_SEGMENTS = 100;

    private final int maxProfiles;

    public MaxProfilesPerTenantPolicy() {
        this(DEFAULT_MAX_SEGMENTS);
    }

    public MaxProfilesPerTenantPolicy(int maxProfiles) {
        if (maxProfiles <= 0) {
            throw new IllegalArgumentException("maxProfiles must be positive");
        }
        this.maxProfiles = maxProfiles;
    }

    /**
     * Validate that the tenant can create more segments.
     *
     * @param currentProfileCount the current number of segments
     * @param tenantId            the tenant ID
     * @throws BusinessException if limit is exceeded
     */
    public void validate(int currentProfileCount, String tenantId) {
        if (currentProfileCount >= maxProfiles) {
            throw new BusinessException(
                    String.format("Tenant '%s' has reached the maximum number of segments (%d)",
                            tenantId, maxProfiles),
                    "MAX_SEGMENTS_EXCEEDED"
            );
        }
    }

    /**
     * Check if the tenant can create more segments.
     *
     * @param currentProfileCount the current number of segments
     * @return true if more segments can be created, false otherwise
     */
    public boolean canCreateProfile(int currentProfileCount) {
        return currentProfileCount < maxProfiles;
    }

    /**
     * Get the maximum allowed segments.
     *
     * @return the maximum segments
     */
    public int getMaxProfiles() {
        return maxProfiles;
    }

    /**
     * Calculate remaining segments allowed.
     *
     * @param currentProfileCount the current number of segments
     * @return remaining segments allowed
     */
    public int getRemainingProfiles(int currentProfileCount) {
        return Math.max(0, maxProfiles - currentProfileCount);
    }
}
