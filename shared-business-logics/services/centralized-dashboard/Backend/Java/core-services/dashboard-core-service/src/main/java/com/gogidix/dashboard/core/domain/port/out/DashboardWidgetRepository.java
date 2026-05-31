package com.gogidix.dashboard.core.domain.port.out;

import com.gogidix.dashboard.core.domain.model.DashboardWidget;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for DashboardWidget entity.
 * Output port in hexagonal architecture.
 */
public interface DashboardWidgetRepository {

    /**
     * Save a widget
     */
    DashboardWidget save(DashboardWidget widget);

    /**
     * Find widget by ID
     */
    Optional<DashboardWidget> findById(UUID id);

    /**
     * Find widgets by dashboard ID
     */
    List<DashboardWidget> findByDashboardId(UUID dashboardId);

    /**
     * Find widgets by tenant ID
     */
    List<DashboardWidget> findByTenantId(String tenantId);

    /**
     * Find visible widgets by dashboard ID
     */
    List<DashboardWidget> findByDashboardIdAndIsVisible(UUID dashboardId, Boolean isVisible);

    /**
     * Delete a widget
     */
    void delete(DashboardWidget widget);

    /**
     * Delete widgets by dashboard ID
     */
    void deleteByDashboardId(UUID dashboardId);

    /**
     * Count widgets by dashboard ID
     */
    long countByDashboardId(UUID dashboardId);
}
