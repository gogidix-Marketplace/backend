package com.gogidix.aiservices.aichurnpredictionservice.domain.port.in;

/**
 * Input port for deleting a churn prediction.
 */
public interface DeletePredictionCommand {

    /**
     * Get the segment ID to delete.
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

    /**
     * Get the user ID from context.
     *
     * @return the user ID
     */
    String getUserId();
}
