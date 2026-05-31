package com.gogidix.aiservices.aitrainingservice.domain.model;

/**
 * Enumeration of fine-tuning job status.
 */
public enum FineTuningStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED
}
