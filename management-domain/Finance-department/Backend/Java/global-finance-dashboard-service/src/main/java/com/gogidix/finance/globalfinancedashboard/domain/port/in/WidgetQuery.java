package com.gogidix.finance.globalfinancedashboard.domain.port.in;

import com.gogidix.finance.globalfinancedashboard.domain.model.Widget;

import java.util.List;
import java.util.Optional;

/**
 * Input port for widget queries
 * Defines operations for retrieving widgets
 */
public interface WidgetQuery {

    /**
     * Finds a widget by ID
     */
    Optional<Widget> findById(String tenantId, String widgetId);

    /**
     * Finds all widgets for a dashboard
     */
    List<Widget> findByDashboardId(String tenantId, String dashboardId);

    /**
     * Finds active widgets for a dashboard
     */
    List<Widget> findActiveByDashboardId(String tenantId, String dashboardId);

    /**
     * Finds widgets by type
     */
    List<Widget> findByType(String tenantId, Widget.WidgetType type);

    /**
     * Finds widgets by status
     */
    List<Widget> findByStatus(String tenantId, Widget.WidgetStatus status);
}
