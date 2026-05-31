package com.gogidix.aiservices.aichurnpredictionservice.domain.model;

/**
 * Enum representing the status of a ChurnPrediction.
 */
public enum PredictionStatus {
    /**
     * Prediction is being created and not yet active.
     */
    DRAFT,

    /**
     * Prediction is active and used for targeting.
     */
    ACTIVE,

    /**
     * Prediction is temporarily disabled.
     */
    INACTIVE,

    /**
     * Prediction is archived and no longer in use.
     */
    ARCHIVED
}
