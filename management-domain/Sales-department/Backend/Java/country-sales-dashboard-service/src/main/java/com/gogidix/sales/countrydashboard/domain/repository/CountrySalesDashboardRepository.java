package com.gogidix.sales.countrydashboard.domain.repository;

import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Country Sales Dashboard Repository Interface
 * Defines persistence operations for country sales dashboards
 */
public interface CountrySalesDashboardRepository {

    CountrySalesDashboard save(CountrySalesDashboard dashboard);

    List<CountrySalesDashboard> saveAll(List<CountrySalesDashboard> dashboards);

    Optional<CountrySalesDashboard> findById(String id);

    Optional<CountrySalesDashboard> findByDashboardIdAndTenantId(String dashboardId, String tenantId);

    Optional<CountrySalesDashboard> findByCountryCodeAndTenantId(String countryCode, String tenantId);

    List<CountrySalesDashboard> findByTenantId(String tenantId);

    Page<CountrySalesDashboard> findByTenantId(String tenantId, Pageable pageable);

    List<CountrySalesDashboard> findByTenantIdAndStatus(String tenantId, CountrySalesDashboard.DashboardStatus status);

    List<CountrySalesDashboard> findByTenantIdAndRegion(String tenantId, String region);

    List<CountrySalesDashboard> findByMultipleCountryCodes(String tenantId, List<String> countryCodes);

    boolean existsByDashboardIdAndTenantId(String dashboardId, String tenantId);

    boolean existsByCountryCodeAndTenantId(String countryCode, String tenantId);

    void deleteById(String id);

    void deleteByDashboardIdAndTenantId(String dashboardId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, CountrySalesDashboard.DashboardStatus status);

    List<CountrySalesDashboard> findActiveDashboardsForTenant(String tenantId);

    List<CountrySalesDashboard> findByRegionAndTenantId(String region, String tenantId);
}
