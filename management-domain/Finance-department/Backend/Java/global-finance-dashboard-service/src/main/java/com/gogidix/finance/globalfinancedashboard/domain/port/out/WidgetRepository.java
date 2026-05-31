package com.gogidix.finance.globalfinancedashboard.domain.port.out;

import com.gogidix.finance.globalfinancedashboard.domain.model.Widget;

import java.util.List;
import java.util.Optional;

/**
 * Output port for widget persistence
 */
public interface WidgetRepository {

    /**
     * Saves a widget
     */
    Widget save(Widget widget);

    /**
     * Finds a widget by ID
     */
    Optional<Widget> findById(String tenantId, String widgetId);

    /**
     * Finds all widgets for a dashboard
     */
    List<Widget> findByDashboardId(String tenantId, String dashboardId);

    /**
     * Finds widgets by type
     */
    List<Widget> findByType(String tenantId, Widget.WidgetType type);

    /**
     * Finds widgets by status
     */
    List<Widget> findByStatus(String tenantId, Widget.WidgetStatus status);

    /**
     * Deletes a widget
     */
    void delete(Widget widget);

    /**
     * Deletes all widgets for a dashboard
     */
    void deleteByDashboardId(String tenantId, String dashboardId);

    /**
     * Batch saves widgets
     */
    List<Widget> saveAll(List<Widget> widgets);
}
