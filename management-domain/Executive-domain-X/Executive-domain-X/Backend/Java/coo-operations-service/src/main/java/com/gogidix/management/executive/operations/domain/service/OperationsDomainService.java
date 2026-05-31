package com.gogidix.management.executive.operations.domain.service;

import com.gogidix.management.executive.operations.domain.model.Operations;

/**
 * Domain service for Operations business logic
 */
public interface OperationsDomainService {

    /**
     * Record layout class
     */
    static record OperationsLayout(int columns, int rows, int itemCount) {}

    /**
     * Record health metrics class
     */
    static record OperationsHealthMetrics(int score, boolean healthy, String recommendation) {}

    /**
     * Validate strategy for creation
     */
    void validateStrategyForCreation(Operations strategy);

    /**
     * Validate strategy for update
     */
    void validateStrategyForUpdate(Operations strategy);

    /**
     * Check if strategy can be published
     */
    boolean canPublish(Operations strategy);

    /**
     * Check if strategy can be deleted
     */
    boolean canDelete(Operations strategy);

    /**
     * Calculate strategy layout
     */
    OperationsLayout calculateLayout(Operations strategy);

    /**
     * Validate strategy layout
     */
    void validateLayout(Operations strategy);

    /**
     * Get strategy health metrics
     */
    OperationsHealthMetrics getHealthMetrics(Operations strategy);
}
