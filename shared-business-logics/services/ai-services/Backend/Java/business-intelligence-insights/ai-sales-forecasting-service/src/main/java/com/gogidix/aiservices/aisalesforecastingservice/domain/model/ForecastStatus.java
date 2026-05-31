package com.gogidix.aiservices.aisalesforecastingservice.domain.model;

/**
 * Enum representing the status of a SalesForecast.
 */
public enum ForecastStatus {
    /**
     * Forecast is being created and not yet active.
     */
    DRAFT,

    /**
     * Forecast is active and used for targeting.
     */
    ACTIVE,

    /**
     * Forecast is temporarily disabled.
     */
    INACTIVE,

    /**
     * Forecast is archived and no longer in use.
     */
    ARCHIVED
}
