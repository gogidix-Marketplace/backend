package com.gogidix.aiservices.intelligenceanalysisservice.domain.policy;

import com.gogidix.aiservices.intelligenceanalysisservice.shared.exception.BusinessException;
import com.gogidix.aiservices.intelligenceanalysisservice.shared.exception.ValidationException;

/**
 * Business rule policy: Minimum customer count required for segment activation.
 * Prevents activation of segments with too few customers for meaningful analysis.
 */
public class MinimumIntelligenceReportCountPolicy {

    private static final int DEFAULT_MINIMUM_COUNT = 10;

    private final int minimumIntelligenceReportCount;

    public MinimumIntelligenceReportCountPolicy() {
        this(DEFAULT_MINIMUM_COUNT);
    }

    public MinimumIntelligenceReportCountPolicy(int minimumIntelligenceReportCount) {
        if (minimumIntelligenceReportCount < 0) {
            throw new IllegalArgumentException("minimumIntelligenceReportCount cannot be negative");
        }
        this.minimumIntelligenceReportCount = minimumIntelligenceReportCount;
    }

    /**
     * Validate that the segment has enough customers for activation.
     *
     * @param customerCount the current customer count
     * @param segmentName  the segment name
     * @throws BusinessException if below minimum threshold
     */
    public void validateForActivation(long customerCount, String segmentName) {
        if (customerCount < minimumIntelligenceReportCount) {
            throw new BusinessException(
                    String.format("Analysis '%s' has only %d customers. Minimum %d required for activation.",
                            segmentName, customerCount, minimumIntelligenceReportCount),
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
        int analysisMinimum = Math.max(1, minimumIntelligenceReportCount / 2);
        if (customerCount < analysisMinimum) {
            throw new ValidationException(
                    String.format("Analysis '%s' has only %d customers. Minimum %d required for analysis.",
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
        return customerCount >= minimumIntelligenceReportCount;
    }

    /**
     * Get the minimum customer count for activation.
     *
     * @return the minimum count
     */
    public int getMinimumIntelligenceReportCount() {
        return minimumIntelligenceReportCount;
    }

    /**
     * Calculate how many more customers are needed for activation.
     *
     * @param customerCount the current customer count
     * @return number of additional customers needed (0 if already sufficient)
     */
    public long getAdditionalIntelligenceReportsNeeded(long customerCount) {
        if (customerCount >= minimumIntelligenceReportCount) {
            return 0;
        }
        return minimumIntelligenceReportCount - customerCount;
    }
}
