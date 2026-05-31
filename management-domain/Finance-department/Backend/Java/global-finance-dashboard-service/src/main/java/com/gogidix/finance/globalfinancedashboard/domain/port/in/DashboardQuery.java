package com.gogidix.finance.globalfinancedashboard.domain.port.in;

import com.gogidix.finance.globalfinancedashboard.domain.model.Dashboard;

import java.util.List;
import java.util.Optional;

/**
 * Input port for dashboard queries
 * Defines operations for retrieving dashboards
 */
public interface DashboardQuery {

    /**
     * Finds a dashboard by ID
     */
    Optional<Dashboard> findById(String tenantId, String dashboardId);

    /**
     * Finds a dashboard by share token
     */
    Optional<Dashboard> findByShareToken(String shareToken);

    /**
     * Finds all dashboards for a tenant
     */
    List<Dashboard> findAll(String tenantId);

    /**
     * Finds all active dashboards for a tenant
     */
    List<Dashboard> findActive(String tenantId);

    /**
     * Finds dashboards by owner
     */
    List<Dashboard> findByOwner(String tenantId, String owner);

    /**
     * Finds dashboards shared with user
     */
    List<Dashboard> findSharedWith(String tenantId, String userId);

    /**
     * Finds dashboards shared with group
     */
    List<Dashboard> findSharedWithGroup(String tenantId, String groupId);

    /**
     * Finds default dashboard for tenant
     */
    Optional<Dashboard> findDefault(String tenantId);

    /**
     * Finds dashboards by type
     */
    List<Dashboard> findByType(String tenantId, Dashboard.DashboardType type);

    /**
     * Searches dashboards by name
     */
    List<Dashboard> searchByName(String tenantId, String namePattern);

    /**
     * Checks if user has access to dashboard
     */
    boolean hasAccess(String tenantId, String dashboardId, String userId, List<String> userGroupIds);
}
