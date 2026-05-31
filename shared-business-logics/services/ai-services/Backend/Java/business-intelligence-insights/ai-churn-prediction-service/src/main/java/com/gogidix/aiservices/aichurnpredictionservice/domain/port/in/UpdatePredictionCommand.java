package com.gogidix.aiservices.aichurnpredictionservice.domain.port.in;

/**
 * Input port for updating an existing churn prediction.
 */
public interface UpdatePredictionCommand {

    /**
     * Get the segment ID to update.
     *
     * @return the segment ID
     */
    String getPredictionId();

    /**
     * Get the new segment name.
     *
     * @return the name
     */
    String getName();

    /**
     * Get the new segment description.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Get the updated segment criteria.
     *
     * @return the criteria
     */
    com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionCriteria getCriteria();

    /**
     * Get the new status.
     *
     * @return the status
     */
    String getStatus();

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
