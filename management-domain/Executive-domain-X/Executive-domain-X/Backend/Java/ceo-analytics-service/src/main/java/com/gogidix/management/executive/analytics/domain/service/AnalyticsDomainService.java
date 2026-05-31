package com.gogidix.management.executive.analytics.domain.service;

import com.gogidix.management.executive.analytics.domain.model.Analytics;
import com.gogidix.management.executive.analytics.domain.model.KpiWidget;
import java.util.List;

/**
 * Domain service for Analytics business logic
 */
public interface AnalyticsDomainService {

    /**
     * Record layout class
     */
    static record AnalyticsLayout(int columns, int rows, int widgetCount) {}

    /**
     * Record health metrics class
     */
    static record AnalyticsHealthMetrics(int score, boolean healthy, String recommendation) {}

    /**
     * Validate analytics for creation
     */
    void validateAnalyticsForCreation(Analytics analytics);

    /**
     * Validate analytics for update
     */
    void validateAnalyticsForUpdate(Analytics analytics);

    /**
     * Validate KPI widget for creation
     */
    void validateWidgetForCreation(KpiWidget widget, Analytics analytics);

    /**
     * Validate KPI widget for update
     */
    void validateWidgetForUpdate(KpiWidget widget);

    /**
     * Check if widget can be added to analytics
     */
    boolean canAddWidget(KpiWidget widget, Analytics analytics);

    /**
     * Check if analytics can be published
     */
    boolean canPublish(Analytics analytics);

    /**
     * Check if analytics can be deleted
     */
    boolean canDelete(Analytics analytics);

    /**
     * Calculate analytics layout
     */
    AnalyticsLayout calculateLayout(Analytics analytics);

    /**
     * Validate analytics layout
     */
    void validateLayout(Analytics analytics);

    /**
     * Get analytics health metrics
     */
    AnalyticsHealthMetrics getHealthMetrics(Analytics analytics);
}
