package com.gogidix.management.executive.domain.service;

import com.gogidix.management.executive.domain.model.Dashboard;
import com.gogidix.management.executive.domain.model.KpiWidget;
import java.util.List;

/**
 * Domain service for Dashboard business logic
 */
public interface DashboardDomainService {

    /**
     * Record layout class
     */
    static record DashboardLayout(int columns, int rows, int widgetCount) {}

    /**
     * Record health metrics class
     */
    static record DashboardHealthMetrics(int score, boolean healthy, String recommendation) {}

    /**
     * Validate dashboard for creation
     */
    void validateDashboardForCreation(Dashboard dashboard);

    /**
     * Validate dashboard for update
     */
    void validateDashboardForUpdate(Dashboard dashboard);

    /**
     * Validate KPI widget for creation
     */
    void validateWidgetForCreation(KpiWidget widget, Dashboard dashboard);

    /**
     * Validate KPI widget for update
     */
    void validateWidgetForUpdate(KpiWidget widget);

    /**
     * Check if widget can be added to dashboard
     */
    boolean canAddWidget(KpiWidget widget, Dashboard dashboard);

    /**
     * Check if dashboard can be published
     */
    boolean canPublish(Dashboard dashboard);

    /**
     * Check if dashboard can be deleted
     */
    boolean canDelete(Dashboard dashboard);

    /**
     * Calculate dashboard layout
     */
    DashboardLayout calculateLayout(Dashboard dashboard);

    /**
     * Validate dashboard layout
     */
    void validateLayout(Dashboard dashboard);

    /**
     * Get dashboard health metrics
     */
    DashboardHealthMetrics getHealthMetrics(Dashboard dashboard);
}
