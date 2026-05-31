package com.gogidix.aiservices.aisalesforecastingservice.domain.model;

/**
 * Enum representing the type of SalesForecast.
 */
public enum ForecastType {
    /**
     * Based on customer behavior patterns.
     */
    BEHAVIORAL,

    /**
     * Based on demographic characteristics.
     */
    DEMOGRAPHIC,

    /**
     * Based on transaction history.
     */
    TRANSACTIONAL,

    /**
     * Custom defined segment.
     */
    CUSTOM
}
