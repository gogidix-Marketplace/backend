package com.gogidix.aiservices.intelligenceanalysisservice.domain.port.in;

/**
 * Input port for querying customer segments.
 */
public interface GetAnalysisQuery {

    /**
     * Get the segment ID to query.
     *
     * @return the segment ID
     */
    String getAnalysisId();

    /**
     * Get the tenant ID from context.
     *
     * @return the tenant ID
     */
    String getTenantId();
}
