package com.gogidix.aiservices.aichurnpredictionservice.domain.port.in;

import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionCriteria;

/**
 * Input port for creating a new churn prediction.
 */
public interface CreatePredictionCommand {

    /**
     * Get the segment name.
     *
     * @return the name
     */
    String getName();

    /**
     * Get the segment description.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Get the segment type.
     *
     * @return the segment type
     */
    String getPredictionType();

    /**
     * Get the segment criteria.
     *
     * @return the criteria
     */
    PredictionCriteria getCriteria();

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
