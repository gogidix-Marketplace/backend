package com.gogidix.management.executive.workflow.domain.service;

import com.gogidix.management.executive.workflow.domain.model.Workflow;

/**
 * Domain service for Workflow business logic
 */
public interface WorkflowDomainService {

    /**
     * Record layout class
     */
    static record WorkflowLayout(int columns, int rows, int itemCount) {}

    /**
     * Record health metrics class
     */
    static record WorkflowHealthMetrics(int score, boolean healthy, String recommendation) {}

    /**
     * Validate strategy for creation
     */
    void validateStrategyForCreation(Workflow strategy);

    /**
     * Validate strategy for update
     */
    void validateStrategyForUpdate(Workflow strategy);

    /**
     * Check if strategy can be published
     */
    boolean canPublish(Workflow strategy);

    /**
     * Check if strategy can be deleted
     */
    boolean canDelete(Workflow strategy);

    /**
     * Calculate strategy layout
     */
    WorkflowLayout calculateLayout(Workflow strategy);

    /**
     * Validate strategy layout
     */
    void validateLayout(Workflow strategy);

    /**
     * Get strategy health metrics
     */
    WorkflowHealthMetrics getHealthMetrics(Workflow strategy);
}
