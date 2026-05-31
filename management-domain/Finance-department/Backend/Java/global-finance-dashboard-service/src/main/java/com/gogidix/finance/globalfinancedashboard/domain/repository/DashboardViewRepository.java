package com.gogidix.finance.globalfinancedashboard.domain.repository;

import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardView;

import java.util.List;
import java.util.Optional;

/**
 * Dashboard View Repository Interface (Port)
 * Defines the contract for dashboard view persistence operations
 */
public interface DashboardViewRepository {

    /**
     * Saves a dashboard view
     */
    DashboardView save(DashboardView dashboardView);

    /**
     * Saves all dashboard views
     */
    List<DashboardView> saveAll(List<DashboardView> dashboardViews);

    /**
     * Finds a dashboard view by ID
     */
    Optional<DashboardView> findById(String id);

    /**
     * Finds a dashboard view by tenant ID and view ID
     */
    Optional<DashboardView> findByTenantIdAndViewId(String tenantId, String viewId);

    /**
     * Finds all dashboard views for a tenant
     */
    List<DashboardView> findByTenantId(String tenantId);

    /**
     * Finds dashboard views by owner
     */
    List<DashboardView> findByTenantIdAndOwnerId(String tenantId, String ownerId);

    /**
     * Finds public dashboard views for a tenant
     */
    List<DashboardView> findByTenantIdAndIsPublic(String tenantId, boolean isPublic);

    /**
     * Finds dashboard views by name pattern
     */
    List<DashboardView> findByTenantIdAndNameContaining(String tenantId, String namePattern);

    /**
     * Finds default dashboard view for a tenant
     */
    Optional<DashboardView> findDefaultByTenantId(String tenantId);

    /**
     * Finds dashboard views shared with a user
     */
    List<DashboardView> findSharedWithUser(String tenantId, String userId);

    /**
     * Checks if a dashboard view exists
     */
    boolean existsByTenantIdAndViewId(String tenantId, String viewId);

    /**
     * Deletes a dashboard view
     */
    void deleteById(String id);

    /**
     * Deletes a dashboard view by tenant ID and view ID
     */
    void deleteByTenantIdAndViewId(String tenantId, String viewId);

    /**
     * Deletes all dashboard views for a tenant
     */
    void deleteAllByTenantId(String tenantId);

    /**
     * Counts dashboard views for a tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Counts dashboard views by owner
     */
    long countByTenantIdAndOwnerId(String tenantId, String ownerId);
}
