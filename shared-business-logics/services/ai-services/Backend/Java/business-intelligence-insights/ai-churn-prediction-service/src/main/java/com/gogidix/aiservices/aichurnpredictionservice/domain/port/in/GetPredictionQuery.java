package com.gogidix.aiservices.aichurnpredictionservice.domain.port.in;

/**
 * Input port for querying churn predictions.
 */
public interface GetPredictionQuery {

    /**
     * Get the segment ID to query.
     *
     * @return the segment ID
     */
    String getPredictionId();

    /**
     * Get the tenant ID from context.
     *
     * @return the tenant ID
     */
    String getTenantId();
}
