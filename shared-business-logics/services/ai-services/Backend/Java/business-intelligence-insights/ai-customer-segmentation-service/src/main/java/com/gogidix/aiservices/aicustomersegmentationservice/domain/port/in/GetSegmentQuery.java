package com.gogidix.aiservices.aicustomersegmentationservice.domain.port.in;

/**
 * Input port for querying customer segments.
 */
public interface GetSegmentQuery {

    /**
     * Get the segment ID to query.
     *
     * @return the segment ID
     */
    String getSegmentId();

    /**
     * Get the tenant ID from context.
     *
     * @return the tenant ID
     */
    String getTenantId();
}
