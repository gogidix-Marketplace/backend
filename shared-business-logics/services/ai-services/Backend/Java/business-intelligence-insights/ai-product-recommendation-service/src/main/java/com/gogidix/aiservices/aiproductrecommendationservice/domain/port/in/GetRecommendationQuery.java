package com.gogidix.aiservices.aiproductrecommendationservice.domain.port.in;

/**
 * Input port for querying product recommendations.
 */
public interface GetRecommendationQuery {

    /**
     * Get the segment ID to query.
     *
     * @return the segment ID
     */
    String getRecommendationId();

    /**
     * Get the tenant ID from context.
     *
     * @return the tenant ID
     */
    String getTenantId();
}
