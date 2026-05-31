package com.gogidix.finance.globalfinancedashboard.domain.port.out;

import com.gogidix.finance.globalfinancedashboard.domain.model.Dashboard;

import java.util.List;
import java.util.Optional;

/**
 * Output port for dashboard persistence
 */
public interface DashboardRepository {

    /**
     * Saves a dashboard
     */
    Dashboard save(Dashboard dashboard);

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
     * Finds default dashboard
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
     * Deletes a dashboard
     */
    void delete(Dashboard dashboard);

    /**
     * Checks if dashboard exists
     */
    boolean existsById(String tenantId, String dashboardId);
}
