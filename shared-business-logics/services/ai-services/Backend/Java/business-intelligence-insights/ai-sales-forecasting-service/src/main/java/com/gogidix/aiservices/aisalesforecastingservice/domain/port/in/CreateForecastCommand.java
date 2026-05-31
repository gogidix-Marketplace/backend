package com.gogidix.aiservices.aisalesforecastingservice.domain.port.in;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria;

/**
 * Input port for creating a new sales forecast.
 */
public interface CreateForecastCommand {

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
    String getForecastType();

    /**
     * Get the segment criteria.
     *
     * @return the criteria
     */
    ForecastCriteria getCriteria();

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
