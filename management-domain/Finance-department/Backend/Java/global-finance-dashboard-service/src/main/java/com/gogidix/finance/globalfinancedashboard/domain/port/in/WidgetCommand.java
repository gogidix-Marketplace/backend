package com.gogidix.finance.globalfinancedashboard.domain.port.in;

import com.gogidix.finance.globalfinancedashboard.domain.model.Widget;

/**
 * Input port for widget commands
 * Defines operations for creating and updating widgets
 */
public interface WidgetCommand {

    /**
     * Creates a new widget
     */
    Widget createWidget(String tenantId, String dashboardId, String name,
                       Widget.WidgetType type, Widget.DataSource dataSource);

    /**
     * Updates widget information
     */
    Widget updateWidgetInfo(String tenantId, String widgetId, String name, String description);

    /**
     * Updates widget size
     */
    Widget updateWidgetSize(String tenantId, String widgetId, Widget.WidgetSize size);

    /**
     * Updates widget location
     */
    Widget updateWidgetLocation(String tenantId, String widgetId, int row, int column);

    /**
     * Updates widget data source
     */
    Widget updateDataSource(String tenantId, String widgetId, Widget.DataSource dataSource);

    /**
     * Updates metric configuration
     */
    Widget updateMetricConfig(String tenantId, String widgetId, Widget.MetricConfig config);

    /**
     * Updates visualization configuration
     */
    Widget updateVisualizationConfig(String tenantId, String widgetId,
                                    Widget.VisualizationConfig config);

    /**
     * Sets widget property
     */
    Widget setProperty(String tenantId, String widgetId, String key, Object value);

    /**
     * Updates refresh configuration
     */
    Widget updateRefreshConfig(String tenantId, String widgetId, Widget.RefreshConfig config);

    /**
     * Enables drilldown
     */
    Widget enableDrilldown(String tenantId, String widgetId, Widget.DrilldownConfig config);

    /**
     * Disables drilldown
     */
    Widget disableDrilldown(String tenantId, String widgetId);

    /**
     * Activates widget
     */
    Widget activateWidget(String tenantId, String widgetId);

    /**
     * Hides widget
     */
    Widget hideWidget(String tenantId, String widgetId);

    /**
     * Deletes widget
     */
    void deleteWidget(String tenantId, String widgetId);
}
