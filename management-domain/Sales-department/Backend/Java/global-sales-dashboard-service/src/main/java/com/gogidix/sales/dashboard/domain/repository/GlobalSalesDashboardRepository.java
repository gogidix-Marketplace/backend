package com.gogidix.sales.dashboard.domain.repository;

import com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard;

import java.util.List;
import java.util.Optional;

/**
 * Global Sales Dashboard Repository Interface (Port)
 * Defines the contract for dashboard persistence operations
 */
public interface GlobalSalesDashboardRepository {

    GlobalSalesDashboard save(GlobalSalesDashboard dashboard);

    List<GlobalSalesDashboard> saveAll(List<GlobalSalesDashboard> dashboards);

    Optional<GlobalSalesDashboard> findById(String id);

    Optional<GlobalSalesDashboard> findByDashboardIdAndTenantId(String dashboardId, String tenantId);

    List<GlobalSalesDashboard> findByTenantId(String tenantId);

    List<GlobalSalesDashboard> findByTenantIdAndStatus(String tenantId,
                                                         GlobalSalesDashboard.DashboardStatus status);

    List<GlobalSalesDashboard> findByTenantIdAndType(String tenantId,
                                                       GlobalSalesDashboard.DashboardType type);

    List<GlobalSalesDashboard> findByTenantIdAndTypeAndStatus(String tenantId,
                                                                GlobalSalesDashboard.DashboardType type,
                                                                GlobalSalesDashboard.DashboardStatus status);

    List<GlobalSalesDashboard> findByTenantIdAndNameContaining(String tenantId, String name);

    List<GlobalSalesDashboard> findByTenantIdAndCreatedBy(String tenantId, String userId);

    boolean existsByDashboardIdAndTenantId(String dashboardId, String tenantId);

    void deleteById(String id);

    void deleteByDashboardIdAndTenantId(String dashboardId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, GlobalSalesDashboard.DashboardStatus status);

    List<GlobalSalesDashboard> findActiveDashboardsForTenant(String tenantId);
}
