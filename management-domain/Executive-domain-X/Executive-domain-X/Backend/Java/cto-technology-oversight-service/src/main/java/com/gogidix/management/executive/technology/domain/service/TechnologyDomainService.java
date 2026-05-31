package com.gogidix.management.executive.technology.domain.service;

import com.gogidix.management.executive.technology.domain.model.Technology;

/**
 * Domain service for Technology business logic
 */
public interface TechnologyDomainService {

    /**
     * Record layout class
     */
    static record TechnologyLayout(int columns, int rows, int itemCount) {}

    /**
     * Record health metrics class
     */
    static record TechnologyHealthMetrics(int score, boolean healthy, String recommendation) {}

    /**
     * Validate strategy for creation
     */
    void validateStrategyForCreation(Technology strategy);

    /**
     * Validate strategy for update
     */
    void validateStrategyForUpdate(Technology strategy);

    /**
     * Check if strategy can be published
     */
    boolean canPublish(Technology strategy);

    /**
     * Check if strategy can be deleted
     */
    boolean canDelete(Technology strategy);

    /**
     * Calculate strategy layout
     */
    TechnologyLayout calculateLayout(Technology strategy);

    /**
     * Validate strategy layout
     */
    void validateLayout(Technology strategy);

    /**
     * Get strategy health metrics
     */
    TechnologyHealthMetrics getHealthMetrics(Technology strategy);
}
