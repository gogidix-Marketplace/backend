package com.gogidix.sales.analytics.domain.repository;

import com.gogidix.sales.analytics.domain.model.DashboardWidget;

import java.util.List;
import java.util.Optional;

/**
 * Dashboard Widget Repository Interface (Port)
 * Defines the contract for dashboard widget persistence operations
 */
public interface DashboardWidgetRepository {

    DashboardWidget save(DashboardWidget widget);

    List<DashboardWidget> saveAll(List<DashboardWidget> widgets);

    Optional<DashboardWidget> findById(String id);

    Optional<DashboardWidget> findByWidgetIdAndTenantId(String widgetId, String tenantId);

    List<DashboardWidget> findByTenantId(String tenantId);

    List<DashboardWidget> findByTenantIdAndDashboardId(String tenantId, String dashboardId);

    List<DashboardWidget> findByTenantIdAndDashboardIdOrderByDisplayOrderAsc(String tenantId, String dashboardId);

    List<DashboardWidget> findByTenantIdAndWidgetType(String tenantId, DashboardWidget.WidgetType widgetType);

    List<DashboardWidget> findByTenantIdAndIsVisible(String tenantId, Boolean isVisible);

    List<DashboardWidget> findByTenantIdAndCreatedBy(String tenantId, String createdBy);

    boolean existsByWidgetIdAndTenantId(String widgetId, String tenantId);

    void deleteById(String id);

    void deleteByWidgetIdAndTenantId(String widgetId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    void deleteAllByDashboardId(String dashboardId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndDashboardId(String tenantId, String dashboardId);
}
