package com.gogidix.management.executive.strategy.domain.service;

import com.gogidix.management.executive.strategy.domain.model.Strategy;

/**
 * Domain service for Strategy business logic
 */
public interface StrategyDomainService {

    /**
     * Record layout class
     */
    static record StrategyLayout(int columns, int rows, int itemCount) {}

    /**
     * Record health metrics class
     */
    static record StrategyHealthMetrics(int score, boolean healthy, String recommendation) {}

    /**
     * Validate strategy for creation
     */
    void validateStrategyForCreation(Strategy strategy);

    /**
     * Validate strategy for update
     */
    void validateStrategyForUpdate(Strategy strategy);

    /**
     * Check if strategy can be published
     */
    boolean canPublish(Strategy strategy);

    /**
     * Check if strategy can be deleted
     */
    boolean canDelete(Strategy strategy);

    /**
     * Calculate strategy layout
     */
    StrategyLayout calculateLayout(Strategy strategy);

    /**
     * Validate strategy layout
     */
    void validateLayout(Strategy strategy);

    /**
     * Get strategy health metrics
     */
    StrategyHealthMetrics getHealthMetrics(Strategy strategy);
}
