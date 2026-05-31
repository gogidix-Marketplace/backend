package com.gogidix.sales.dashboard.domain.repository;

import com.gogidix.sales.dashboard.domain.model.KPIWidget;

import java.util.List;
import java.util.Optional;

/**
 * KPI Widget Repository Interface (Port)
 * Defines the contract for widget persistence operations
 */
public interface KPIWidgetRepository {

    KPIWidget save(KPIWidget widget);

    List<KPIWidget> saveAll(List<KPIWidget> widgets);

    Optional<KPIWidget> findById(String id);

    Optional<KPIWidget> findByWidgetIdAndTenantId(String widgetId, String tenantId);

    List<KPIWidget> findByTenantId(String tenantId);

    List<KPIWidget> findByDashboardIdAndTenantId(String dashboardId, String tenantId);

    List<KPIWidget> findByTenantIdAndWidgetType(String tenantId,
                                                  KPIWidget.WidgetType widgetType);

    List<KPIWidget> findByTenantIdAndCategory(String tenantId,
                                                KPIWidget.WidgetCategory category);

    List<KPIWidget> findByTenantIdAndIsActive(String tenantId, Boolean isActive);

    List<KPIWidget> findByTenantIdAndOwner(String tenantId, String owner);

    List<KPIWidget> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<KPIWidget> findByDashboardIdAndTenantIdAndIsActive(String dashboardId,
                                                               String tenantId,
                                                               Boolean isActive);

    List<KPIWidget> findByTenantIdAndWidgetTypeAndCategory(String tenantId,
                                                             KPIWidget.WidgetType widgetType,
                                                             KPIWidget.WidgetCategory category);

    List<KPIWidget> searchByTitle(String tenantId, String searchTerm);

    boolean existsByWidgetIdAndTenantId(String widgetId, String tenantId);

    void deleteById(String id);

    void deleteByWidgetIdAndTenantId(String widgetId, String tenantId);

    void deleteByDashboardIdAndTenantId(String dashboardId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByDashboardIdAndTenantId(String dashboardId, String tenantId);

    long countByTenantIdAndWidgetType(String tenantId, KPIWidget.WidgetType widgetType);

    List<KPIWidget> findWidgetsNeedingRefresh(String tenantId, int minutesThreshold);
}
