package com.gogidix.aiservices.aichurnpredictionservice.domain.model;

/**
 * Enum representing the type of ChurnPrediction.
 */
public enum PredictionType {
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
