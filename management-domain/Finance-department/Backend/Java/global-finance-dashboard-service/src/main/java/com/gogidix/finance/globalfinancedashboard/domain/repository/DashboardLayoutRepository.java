package com.gogidix.finance.globalfinancedashboard.domain.repository;

import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardLayout;

import java.util.List;
import java.util.Optional;

/**
 * Dashboard Layout Repository Interface (Port)
 * Defines the contract for dashboard layout persistence operations
 */
public interface DashboardLayoutRepository {

    /**
     * Saves a dashboard layout
     */
    DashboardLayout save(DashboardLayout layout);

    /**
     * Saves all dashboard layouts
     */
    List<DashboardLayout> saveAll(List<DashboardLayout> layouts);

    /**
     * Finds a layout by ID
     */
    Optional<DashboardLayout> findById(String id);

    /**
     * Finds a layout by tenant ID and layout ID
     */
    Optional<DashboardLayout> findByTenantIdAndLayoutId(String tenantId, String layoutId);

    /**
     * Finds a layout by dashboard ID
     */
    Optional<DashboardLayout> findByDashboardId(String tenantId, String dashboardId);

    /**
     * Finds all layouts for a tenant
     */
    List<DashboardLayout> findByTenantId(String tenantId);

    /**
     * Finds layouts by type
     */
    List<DashboardLayout> findByTenantIdAndLayoutType(String tenantId,
                                                       DashboardLayout.LayoutType layoutType);

    /**
     * Finds default layout for dashboard
     */
    Optional<DashboardLayout> findDefaultByDashboardId(String tenantId, String dashboardId);

    /**
     * Checks if layout exists
     */
    boolean existsByTenantIdAndLayoutId(String tenantId, String layoutId);

    /**
     * Deletes a layout
     */
    void deleteById(String id);

    /**
     * Deletes a layout by tenant ID and layout ID
     */
    void deleteByTenantIdAndLayoutId(String tenantId, String layoutId);

    /**
     * Deletes all layouts for a dashboard
     */
    void deleteAllByDashboardId(String tenantId, String dashboardId);

    /**
     * Counts layouts for a tenant
     */
    long countByTenantId(String tenantId);
}
