package com.gogidix.finance.globalfinancedashboard.domain.port.in;

import com.gogidix.finance.globalfinancedashboard.domain.model.Dashboard;
import com.gogidix.finance.globalfinancedashboard.domain.model.Widget;

import java.util.Set;

/**
 * Input port for dashboard commands
 * Defines operations for creating and updating dashboards
 */
public interface DashboardCommand {

    /**
     * Creates a new dashboard
     */
    Dashboard createDashboard(String tenantId, String name, String description,
                             String owner, String ownerEmail, Dashboard.DashboardType type);

    /**
     * Updates dashboard information
     */
    Dashboard updateDashboard(String tenantId, String dashboardId, String name,
                             String description, Dashboard.DashboardType type);

    /**
     * Activates a dashboard
     */
    Dashboard activateDashboard(String tenantId, String dashboardId);

    /**
     * Archives a dashboard
     */
    Dashboard archiveDashboard(String tenantId, String dashboardId);

    /**
     * Deletes a dashboard
     */
    void deleteDashboard(String tenantId, String dashboardId);

    /**
     * Sets dashboard as default
     */
    Dashboard setAsDefault(String tenantId, String dashboardId);

    /**
     * Makes dashboard public
     */
    Dashboard makePublic(String tenantId, String dashboardId);

    /**
     * Makes dashboard private
     */
    Dashboard makePrivate(String tenantId, String dashboardId);

    /**
     * Generates share token
     */
    String generateShareToken(String tenantId, String dashboardId, int expiryHours);

    /**
     * Shares dashboard with user
     */
    void shareWithUser(String tenantId, String dashboardId, String userId);

    /**
     * Shares dashboard with group
     */
    void shareWithGroup(String tenantId, String dashboardId, String groupId);

    /**
     * Removes share from user
     */
    void unshareFromUser(String tenantId, String dashboardId, String userId);

    /**
     * Removes share from group
     */
    void unshareFromGroup(String tenantId, String dashboardId, String groupId);

    /**
     * Adds widget to dashboard
     */
    Dashboard addWidget(String tenantId, String dashboardId, Widget widget);

    /**
     * Removes widget from dashboard
     */
    Dashboard removeWidget(String tenantId, String dashboardId, String widgetId);

    /**
     * Updates widget position
     */
    Dashboard updateWidgetPosition(String tenantId, String dashboardId,
                                  String widgetId, int newPosition);

    /**
     * Updates dashboard layout
     */
    Dashboard updateLayout(String tenantId, String dashboardId,
                          Dashboard.LayoutConfig layout);

    /**
     * Updates dashboard theme
     */
    Dashboard updateTheme(String tenantId, String dashboardId,
                         Dashboard.ThemeConfig theme);

    /**
     * Updates refresh interval
     */
    Dashboard updateRefreshInterval(String tenantId, String dashboardId,
                                   Dashboard.RefreshInterval interval);
}
