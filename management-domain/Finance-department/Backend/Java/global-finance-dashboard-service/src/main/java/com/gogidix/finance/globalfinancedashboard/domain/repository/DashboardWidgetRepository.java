package com.gogidix.finance.globalfinancedashboard.domain.repository;

import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardWidget;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Dashboard Widget Repository Interface (Port)
 * Defines the contract for dashboard widget persistence operations
 */
public interface DashboardWidgetRepository {

    /**
     * Saves a dashboard widget
     */
    DashboardWidget save(DashboardWidget widget);

    /**
     * Saves all dashboard widgets
     */
    List<DashboardWidget> saveAll(List<DashboardWidget> widgets);

    /**
     * Finds a widget by ID
     */
    Optional<DashboardWidget> findById(String id);

    /**
     * Finds a widget by tenant ID and widget ID
     */
    Optional<DashboardWidget> findByTenantIdAndWidgetId(String tenantId, String widgetId);

    /**
     * Finds all widgets for a dashboard
     */
    List<DashboardWidget> findByDashboardId(String tenantId, String dashboardId);

    /**
     * Finds all widgets for a tenant
     */
    List<DashboardWidget> findByTenantId(String tenantId);

    /**
     * Finds widgets by type
     */
    List<DashboardWidget> findByTenantIdAndType(String tenantId,
                                                DashboardWidget.WidgetType type);

    /**
     * Finds widgets by status
     */
    List<DashboardWidget> findByTenantIdAndStatus(String tenantId,
                                                   DashboardWidget.WidgetStatus status);

    /**
     * Finds widgets by dashboard and status
     */
    List<DashboardWidget> findByDashboardIdAndStatus(String tenantId, String dashboardId,
                                                      DashboardWidget.WidgetStatus status);

    /**
     * Finds active widgets for a dashboard
     */
    List<DashboardWidget> findActiveByDashboardId(String tenantId, String dashboardId);

    /**
     * Finds widgets by position range
     */
    List<DashboardWidget> findByDashboardIdAndPositionBetween(String tenantId,
                                                               String dashboardId,
                                                               int minPosition,
                                                               int maxPosition);

    /**
     * Finds widgets created by a user
     */
    List<DashboardWidget> findByTenantIdAndCreatedBy(String tenantId, String createdBy);

    /**
     * Checks if a widget exists
     */
    boolean existsByTenantIdAndWidgetId(String tenantId, String widgetId);

    /**
     * Checks if a widget exists for a dashboard
     */
    boolean existsByDashboardIdAndWidgetId(String tenantId, String dashboardId,
                                           String widgetId);

    /**
     * Deletes a widget
     */
    void deleteById(String id);

    /**
     * Deletes a widget by tenant ID and widget ID
     */
    void deleteByTenantIdAndWidgetId(String tenantId, String widgetId);

    /**
     * Deletes all widgets for a dashboard
     */
    void deleteAllByDashboardId(String tenantId, String dashboardId);

    /**
     * Deletes all widgets for a tenant
     */
    void deleteAllByTenantId(String tenantId);

    /**
     * Counts widgets for a tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Counts widgets for a dashboard
     */
    long countByDashboardId(String tenantId, String dashboardId);

    /**
     * Counts widgets by status
     */
    long countByTenantIdAndStatus(String tenantId, DashboardWidget.WidgetStatus status);

    /**
     * Finds widgets needing refresh
     */
    List<DashboardWidget> findWidgetsNeedingRefresh(String tenantId,
                                                     Instant beforeTimestamp);

    /**
     * Updates widget status
     */
    void updateStatus(String tenantId, String widgetId,
                     DashboardWidget.WidgetStatus status);
}
