package com.gogidix.aiservices.aisalesforecastingservice.domain.policy;

import com.gogidix.aiservices.aisalesforecastingservice.shared.exception.BusinessException;

/**
 * Business rule policy: Maximum number of segments per tenant.
 * Enforces the limit to prevent resource exhaustion.
 */
public class MaxForecastsPerTenantPolicy {

    private static final int DEFAULT_MAX_SEGMENTS = 100;

    private final int maxForecasts;

    public MaxForecastsPerTenantPolicy() {
        this(DEFAULT_MAX_SEGMENTS);
    }

    public MaxForecastsPerTenantPolicy(int maxForecasts) {
        if (maxForecasts <= 0) {
            throw new IllegalArgumentException("maxForecasts must be positive");
        }
        this.maxForecasts = maxForecasts;
    }

    /**
     * Validate that the tenant can create more segments.
     *
     * @param currentForecastCount the current number of segments
     * @param tenantId            the tenant ID
     * @throws BusinessException if limit is exceeded
     */
    public void validate(int currentForecastCount, String tenantId) {
        if (currentForecastCount >= maxForecasts) {
            throw new BusinessException(
                    String.format("Tenant '%s' has reached the maximum number of segments (%d)",
                            tenantId, maxForecasts),
                    "MAX_SEGMENTS_EXCEEDED"
            );
        }
    }

    /**
     * Check if the tenant can create more segments.
     *
     * @param currentForecastCount the current number of segments
     * @return true if more segments can be created, false otherwise
     */
    public boolean canCreateForecast(int currentForecastCount) {
        return currentForecastCount < maxForecasts;
    }

    /**
     * Get the maximum allowed segments.
     *
     * @return the maximum segments
     */
    public int getMaxForecasts() {
        return maxForecasts;
    }

    /**
     * Calculate remaining segments allowed.
     *
     * @param currentForecastCount the current number of segments
     * @return remaining segments allowed
     */
    public int getRemainingForecasts(int currentForecastCount) {
        return Math.max(0, maxForecasts - currentForecastCount);
    }
}
