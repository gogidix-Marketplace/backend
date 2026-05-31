package com.gogidix.aiservices.aiuserprofilingservice.domain.policy;

import com.gogidix.aiservices.aiuserprofilingservice.shared.exception.BusinessException;
import com.gogidix.aiservices.aiuserprofilingservice.shared.exception.ValidationException;

/**
 * Business rule policy: Minimum customer count required for segment activation.
 * Prevents activation of segments with too few customers for meaningful analysis.
 */
public class MinimumUserCountPolicy {

    private static final int DEFAULT_MINIMUM_COUNT = 10;

    private final int minimumUserCount;

    public MinimumUserCountPolicy() {
        this(DEFAULT_MINIMUM_COUNT);
    }

    public MinimumUserCountPolicy(int minimumUserCount) {
        if (minimumUserCount < 0) {
            throw new IllegalArgumentException("minimumUserCount cannot be negative");
        }
        this.minimumUserCount = minimumUserCount;
    }

    /**
     * Validate that the segment has enough customers for activation.
     *
     * @param customerCount the current customer count
     * @param segmentName  the segment name
     * @throws BusinessException if below minimum threshold
     */
    public void validateForActivation(long customerCount, String segmentName) {
        if (customerCount < minimumUserCount) {
            throw new BusinessException(
                    String.format("Profile '%s' has only %d customers. Minimum %d required for activation.",
                            segmentName, customerCount, minimumUserCount),
                    "INSUFFICIENT_CUSTOMERS"
            );
        }
    }

    /**
     * Validate that the segment has enough customers for analysis.
     *
     * @param customerCount the current customer count
     * @param segmentName  the segment name
     * @throws ValidationException if below minimum threshold
     */
    public void validateForAnalysis(long customerCount, String segmentName) {
        int analysisMinimum = Math.max(1, minimumUserCount / 2);
        if (customerCount < analysisMinimum) {
            throw new ValidationException(
                    String.format("Profile '%s' has only %d customers. Minimum %d required for analysis.",
                            segmentName, customerCount, analysisMinimum)
            );
        }
    }

    /**
     * Check if the segment has enough customers for activation.
     *
     * @param customerCount the current customer count
     * @return true if can be activated, false otherwise
     */
    public boolean canActivate(long customerCount) {
        return customerCount >= minimumUserCount;
    }

    /**
     * Get the minimum customer count for activation.
     *
     * @return the minimum count
     */
    public int getMinimumUserCount() {
        return minimumUserCount;
    }

    /**
     * Calculate how many more customers are needed for activation.
     *
     * @param customerCount the current customer count
     * @return number of additional customers needed (0 if already sufficient)
     */
    public long getAdditionalUsersNeeded(long customerCount) {
        if (customerCount >= minimumUserCount) {
            return 0;
        }
        return minimumUserCount - customerCount;
    }
}
