package com.gogidix.aiservices.aicustomersegmentationservice.domain.port.in;

/**
 * Input port for analyzing customer segments.
 */
public interface SegmentAnalysisQuery {

    /**
     * Get the segment ID to analyze.
     *
     * @return the segment ID
     */
    String getSegmentId();

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
    boolean isIncludeCustomerProfiles();

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
