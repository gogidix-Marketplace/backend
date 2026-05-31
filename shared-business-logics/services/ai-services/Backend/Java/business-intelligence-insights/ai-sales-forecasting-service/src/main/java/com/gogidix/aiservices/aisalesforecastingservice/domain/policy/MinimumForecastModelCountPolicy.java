package com.gogidix.aiservices.aisalesforecastingservice.domain.policy;

import com.gogidix.aiservices.aisalesforecastingservice.shared.exception.BusinessException;
import com.gogidix.aiservices.aisalesforecastingservice.shared.exception.ValidationException;

/**
 * Business rule policy: Minimum customer count required for segment activation.
 * Prevents activation of segments with too few customers for meaningful analysis.
 */
public class MinimumForecastModelCountPolicy {

    private static final int DEFAULT_MINIMUM_COUNT = 10;

    private final int minimumForecastModelCount;

    public MinimumForecastModelCountPolicy() {
        this(DEFAULT_MINIMUM_COUNT);
    }

    public MinimumForecastModelCountPolicy(int minimumForecastModelCount) {
        if (minimumForecastModelCount < 0) {
            throw new IllegalArgumentException("minimumForecastModelCount cannot be negative");
        }
        this.minimumForecastModelCount = minimumForecastModelCount;
    }

    /**
     * Validate that the segment has enough customers for activation.
     *
     * @param customerCount the current customer count
     * @param segmentName  the segment name
     * @throws BusinessException if below minimum threshold
     */
    public void validateForActivation(long customerCount, String segmentName) {
        if (customerCount < minimumForecastModelCount) {
            throw new BusinessException(
                    String.format("Forecast '%s' has only %d customers. Minimum %d required for activation.",
                            segmentName, customerCount, minimumForecastModelCount),
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
        int analysisMinimum = Math.max(1, minimumForecastModelCount / 2);
        if (customerCount < analysisMinimum) {
            throw new ValidationException(
                    String.format("Forecast '%s' has only %d customers. Minimum %d required for analysis.",
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
        return customerCount >= minimumForecastModelCount;
    }

    /**
     * Get the minimum customer count for activation.
     *
     * @return the minimum count
     */
    public int getMinimumForecastModelCount() {
        return minimumForecastModelCount;
    }

    /**
     * Calculate how many more customers are needed for activation.
     *
     * @param customerCount the current customer count
     * @return number of additional customers needed (0 if already sufficient)
     */
    public long getAdditionalForecastModelsNeeded(long customerCount) {
        if (customerCount >= minimumForecastModelCount) {
            return 0;
        }
        return minimumForecastModelCount - customerCount;
    }
}
