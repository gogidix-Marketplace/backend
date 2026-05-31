package com.gogidix.aiservices.aichurnpredictionservice.domain.port.in;

/**
 * Input port for analyzing churn predictions.
 */
public interface PredictionAnalysisQuery {

    /**
     * Get the segment ID to analyze.
     *
     * @return the segment ID
     */
    String getPredictionId();

    /**
     * Get the analysis type.
     *
     * @return the analysis type (FULL, INCREMENTAL, QUICK)
     */
    String getAnalysisType();

    /**
     * Get the tenant ID from context.
     *
     * @return the tenant ID
     */
    String getTenantId();

    /**
     * Get whether to include customer profiles.
     *
     * @return true to include profiles
     */
    boolean isIncludeChurnModels();

    /**
     * Get whether to include trends.
     *
     * @return true to include trends
     */
    boolean isIncludeTrends();

    /**
     * Get the time range in days.
     *
     * @return time range in days
     */
    Integer getTimeRangeDays();
}
