package com.gogidix.management.executive.financial.domain.service;

import com.gogidix.management.executive.financial.domain.model.FinancialData;

/**
 * Domain service for FinancialData business logic
 */
public interface FinancialDataDomainService {

    /**
     * Record layout class
     */
    static record FinancialDataLayout(int columns, int rows, int itemCount) {}

    /**
     * Record health metrics class
     */
    static record FinancialDataHealthMetrics(int score, boolean healthy, String recommendation) {}

    /**
     * Validate strategy for creation
     */
    void validateStrategyForCreation(FinancialData strategy);

    /**
     * Validate strategy for update
     */
    void validateStrategyForUpdate(FinancialData strategy);

    /**
     * Check if strategy can be published
     */
    boolean canPublish(FinancialData strategy);

    /**
     * Check if strategy can be deleted
     */
    boolean canDelete(FinancialData strategy);

    /**
     * Calculate strategy layout
     */
    FinancialDataLayout calculateLayout(FinancialData strategy);

    /**
     * Validate strategy layout
     */
    void validateLayout(FinancialData strategy);

    /**
     * Get strategy health metrics
     */
    FinancialDataHealthMetrics getHealthMetrics(FinancialData strategy);
}
