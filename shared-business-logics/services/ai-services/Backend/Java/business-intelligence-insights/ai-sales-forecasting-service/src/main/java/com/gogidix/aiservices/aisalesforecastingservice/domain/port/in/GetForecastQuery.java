package com.gogidix.aiservices.aisalesforecastingservice.domain.port.in;

/**
 * Input port for querying sales forecasts.
 */
public interface GetForecastQuery {

    /**
     * Get the segment ID to query.
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
}
