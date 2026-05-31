package com.gogidix.dashboard.core.domain.port.out;

import com.gogidix.dashboard.core.domain.model.DashboardKPI;
import com.gogidix.dashboard.core.domain.model.SourceDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for DashboardKPI aggregate.
 * Output port in hexagonal architecture.
 */
public interface DashboardKPIRepository {

    /**
     * Save a KPI (create or update)
     */
    DashboardKPI save(DashboardKPI kpi);

    /**
     * Find KPI by ID
     */
    Optional<DashboardKPI> findById(UUID id);

    /**
     * Find KPI by code
     */
    Optional<DashboardKPI> findByCode(String code);

    /**
     * Find KPIs by tenant ID
     */
    List<DashboardKPI> findByTenantId(String tenantId);

    /**
     * Find KPIs by tenant ID and category
     */
    List<DashboardKPI> findByTenantIdAndCategory(String tenantId, String category);

    /**
     * Find KPIs by tenant ID and source domain
     */
    List<DashboardKPI> findByTenantIdAndSourceDomain(String tenantId, SourceDomain sourceDomain);

    /**
     * Find active KPIs by tenant ID
     */
    List<DashboardKPI> findByTenantIdAndIsActive(String tenantId, Boolean isActive);

    /**
     * Find real-time KPIs by tenant ID
     */
    List<DashboardKPI> findByTenantIdAndIsRealTime(String tenantId, Boolean isRealTime);

    /**
     * Find KPIs by tenant ID and codes
     */
    List<DashboardKPI> findByTenantIdAndCodeIn(String tenantId, List<String> codes);

    /**
     * Search KPIs by name or description
     */
    List<DashboardKPI> searchByTenantIdAndSearchQuery(String tenantId, String searchQuery);

    /**
     * Delete a KPI
     */
    void delete(DashboardKPI kpi);

    /**
     * Delete KPIs by tenant ID
     */
    void deleteByTenantId(String tenantId);

    /**
     * Check if KPI exists with code
     */
    boolean existsByCode(String code);

    /**
     * Count KPIs by tenant ID
     */
    long countByTenantId(String tenantId);

    /**
     * Find KPIs needing refresh (based on last calculated time)
     */
    List<DashboardKPI> findKPIsNeedingRefresh(int minutesThreshold);
}
