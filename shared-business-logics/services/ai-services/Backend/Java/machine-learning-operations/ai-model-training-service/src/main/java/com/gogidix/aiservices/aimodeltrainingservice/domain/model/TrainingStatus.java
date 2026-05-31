package com.gogidix.aiservices.aimodeltrainingservice.domain.model;

/**
 * Enumeration of training job status.
 */
public enum TrainingStatus {
    QUEUED,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED,
    TIMEOUT
}
