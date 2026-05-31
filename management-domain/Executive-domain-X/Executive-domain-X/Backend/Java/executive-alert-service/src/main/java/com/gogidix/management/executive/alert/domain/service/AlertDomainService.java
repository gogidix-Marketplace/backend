package com.gogidix.management.executive.alert.domain.service;

import com.gogidix.management.executive.alert.domain.model.Alert;

/**
 * Domain service for Alert business logic
 */
public interface AlertDomainService {

    /**
     * Record layout class
     */
    static record AlertLayout(int columns, int rows, int itemCount) {}

    /**
     * Record health metrics class
     */
    static record AlertHealthMetrics(int score, boolean healthy, String recommendation) {}

    /**
     * Validate strategy for creation
     */
    void validateStrategyForCreation(Alert strategy);

    /**
     * Validate strategy for update
     */
    void validateStrategyForUpdate(Alert strategy);

    /**
     * Check if strategy can be published
     */
    boolean canPublish(Alert strategy);

    /**
     * Check if strategy can be deleted
     */
    boolean canDelete(Alert strategy);

    /**
     * Calculate strategy layout
     */
    AlertLayout calculateLayout(Alert strategy);

    /**
     * Validate strategy layout
     */
    void validateLayout(Alert strategy);

    /**
     * Get strategy health metrics
     */
    AlertHealthMetrics getHealthMetrics(Alert strategy);
}
