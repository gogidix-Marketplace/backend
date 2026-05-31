package com.gogidix.aiservices.aisalesforecastingservice.domain.port.in;

/**
 * Input port for deleting a sales forecast.
 */
public interface DeleteForecastCommand {

    /**
     * Get the segment ID to delete.
     *
     * @return the segment ID
     */
    String getForecastId();

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
