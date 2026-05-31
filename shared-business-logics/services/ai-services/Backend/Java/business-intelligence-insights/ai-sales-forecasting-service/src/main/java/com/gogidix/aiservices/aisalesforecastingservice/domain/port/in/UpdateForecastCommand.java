package com.gogidix.aiservices.aisalesforecastingservice.domain.port.in;

/**
 * Input port for updating an existing sales forecast.
 */
public interface UpdateForecastCommand {

    /**
     * Get the segment ID to update.
     *
     * @return the segment ID
     */
    String getForecastId();

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
    com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria getCriteria();

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
