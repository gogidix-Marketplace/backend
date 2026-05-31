package com.gogidix.aiservices.aisalesforecastingservice.domain.port.in;

/**
 * Input port for listing sales forecasts.
 */
public interface ListForecastsQuery {

    /**
     * Get the tenant ID from context.
     *
     * @return the tenant ID
     */
    String getTenantId();

    /**
     * Get the page number.
     *
     * @return the page number (0-indexed)
     */
    int getPage();

    /**
     * Get the page size.
     *
     * @return the page size
     */
    int getSize();

    /**
     * Get the sort field.
     *
     * @return the sort field
     */
    String getSort();

    /**
     * Get the filter by status.
     *
     * @return the status filter
     */
    String getStatus();

    /**
     * Get the filter by segment type.
     *
     * @return the segment type filter
     */
    String getForecastType();
}
