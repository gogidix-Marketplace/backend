package com.gogidix.aiservices.aiproductrecommendationservice.domain.policy;

import com.gogidix.aiservices.aiproductrecommendationservice.shared.exception.BusinessException;
import com.gogidix.aiservices.aiproductrecommendationservice.shared.exception.ValidationException;

/**
 * Business rule policy: Minimum customer count required for segment activation.
 * Prevents activation of segments with too few customers for meaningful analysis.
 */
public class MinimumProductCountPolicy {

    private static final int DEFAULT_MINIMUM_COUNT = 10;

    private final int minimumProductCount;

    public MinimumProductCountPolicy() {
        this(DEFAULT_MINIMUM_COUNT);
    }

    public MinimumProductCountPolicy(int minimumProductCount) {
        if (minimumProductCount < 0) {
            throw new IllegalArgumentException("minimumProductCount cannot be negative");
        }
        this.minimumProductCount = minimumProductCount;
    }

    /**
     * Validate that the segment has enough customers for activation.
     *
     * @param customerCount the current customer count
     * @param segmentName  the segment name
     * @throws BusinessException if below minimum threshold
     */
    public void validateForActivation(long customerCount, String segmentName) {
        if (customerCount < minimumProductCount) {
            throw new BusinessException(
                    String.format("Recommendation '%s' has only %d customers. Minimum %d required for activation.",
                            segmentName, customerCount, minimumProductCount),
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
        int analysisMinimum = Math.max(1, minimumProductCount / 2);
        if (customerCount < analysisMinimum) {
            throw new ValidationException(
                    String.format("Recommendation '%s' has only %d customers. Minimum %d required for analysis.",
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
        return customerCount >= minimumProductCount;
    }

    /**
     * Get the minimum customer count for activation.
     *
     * @return the minimum count
     */
    public int getMinimumProductCount() {
        return minimumProductCount;
    }

    /**
     * Calculate how many more customers are needed for activation.
     *
     * @param customerCount the current customer count
     * @return number of additional customers needed (0 if already sufficient)
     */
    public long getAdditionalProductsNeeded(long customerCount) {
        if (customerCount >= minimumProductCount) {
            return 0;
        }
        return minimumProductCount - customerCount;
    }
}
