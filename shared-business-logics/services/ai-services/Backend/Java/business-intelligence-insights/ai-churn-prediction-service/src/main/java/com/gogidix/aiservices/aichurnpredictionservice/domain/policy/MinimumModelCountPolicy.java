package com.gogidix.aiservices.aichurnpredictionservice.domain.policy;

import com.gogidix.aiservices.aichurnpredictionservice.shared.exception.BusinessException;
import com.gogidix.aiservices.aichurnpredictionservice.shared.exception.ValidationException;

/**
 * Business rule policy: Minimum customer count required for segment activation.
 * Prevents activation of segments with too few customers for meaningful analysis.
 */
public class MinimumModelCountPolicy {

    private static final int DEFAULT_MINIMUM_COUNT = 10;

    private final int minimumCustomerCount;

    public MinimumModelCountPolicy() {
        this(DEFAULT_MINIMUM_COUNT);
    }

    public MinimumModelCountPolicy(int minimumCustomerCount) {
        if (minimumCustomerCount < 0) {
            throw new IllegalArgumentException("minimumCustomerCount cannot be negative");
        }
        this.minimumCustomerCount = minimumCustomerCount;
    }

    /**
     * Validate that the segment has enough customers for activation.
     *
     * @param customerCount the current customer count
     * @param segmentName  the segment name
     * @throws BusinessException if below minimum threshold
     */
    public void validateForActivation(long customerCount, String segmentName) {
        if (customerCount < minimumCustomerCount) {
            throw new BusinessException(
                    String.format("Prediction '%s' has only %d customers. Minimum %d required for activation.",
                            segmentName, customerCount, minimumCustomerCount),
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
        int analysisMinimum = Math.max(1, minimumCustomerCount / 2);
        if (customerCount < analysisMinimum) {
            throw new ValidationException(
                    String.format("Prediction '%s' has only %d customers. Minimum %d required for analysis.",
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
        return customerCount >= minimumCustomerCount;
    }

    /**
     * Get the minimum customer count for activation.
     *
     * @return the minimum count
     */
    public int getMinimumModelCount() {
        return minimumCustomerCount;
    }

    /**
     * Calculate how many more customers are needed for activation.
     *
     * @param customerCount the current customer count
     * @return number of additional customers needed (0 if already sufficient)
     */
    public long getAdditionalCustomersNeeded(long customerCount) {
        if (customerCount >= minimumCustomerCount) {
            return 0;
        }
        return minimumCustomerCount - customerCount;
    }
}
