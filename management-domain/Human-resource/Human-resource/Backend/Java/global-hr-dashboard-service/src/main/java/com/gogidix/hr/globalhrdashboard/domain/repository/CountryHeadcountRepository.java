package com.gogidix.hr.globalhrdashboard.domain.repository;

import com.gogidix.hr.globalhrdashboard.domain.model.CountryHeadcount;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository Interface for CountryHeadcount aggregate
 * Following Hexagonal Architecture - this is a PORT (out)
 */
public interface CountryHeadcountRepository {

    /**
     * Save a country headcount
     */
    CountryHeadcount save(CountryHeadcount headcount);

    /**
     * Find headcount by ID and tenant
     */
    Optional<CountryHeadcount> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find all headcounts for a tenant
     */
    List<CountryHeadcount> findByTenantId(String tenantId);

    /**
     * Find headcounts by tenant and country code
     */
    List<CountryHeadcount> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    /**
     * Find headcount by tenant, country code, department and period
     */
    Optional<CountryHeadcount> findByTenantIdAndCountryCodeAndDepartmentAndPeriod(
            String tenantId, String countryCode, String department, String period);

    /**
     * Find headcounts by tenant and period
     */
    List<CountryHeadcount> findByTenantIdAndPeriod(String tenantId, String period);

    /**
     * Find headcounts by tenant and department
     */
    List<CountryHeadcount> findByTenantIdAndDepartment(String tenantId, String department);

    /**
     * Find headcounts by tenant and region
     */
    List<CountryHeadcount> findByTenantIdAndRegionCode(String tenantId, String regionCode);

    /**
     * Find headcounts by tenant, country and period
     */
    List<CountryHeadcount> findByTenantIdAndCountryCodeAndPeriod(String tenantId, String countryCode, String period);

    /**
     * Find active headcounts for a tenant
     */
    List<CountryHeadcount> findActiveByTenantId(String tenantId);

    /**
     * Find all countries for a tenant
     */
    List<String> findDistinctCountriesByTenantId(String tenantId);

    /**
     * Find all departments for a tenant
     */
    List<String> findDistinctDepartmentsByTenantId(String tenantId);

    /**
     * Find headcounts updated since a specific time
     */
    List<CountryHeadcount> findByTenantIdAndLastUpdatedAfter(String tenantId, Instant lastUpdated);

    /**
     * Get global headcount total for a tenant and period
     */
    Optional<CountryHeadcount> findGlobalTotalByTenantIdAndPeriod(String tenantId, String period);

    /**
     * Get headcount trend over time for a country
     */
    List<CountryHeadcount> findTrendDataByCountry(String tenantId, String countryCode,
                                                    String startPeriod, String endPeriod);

    /**
     * Get headcount trend over time for a department
     */
    List<CountryHeadcount> findTrendDataByDepartment(String tenantId, String department,
                                                      String startPeriod, String endPeriod);

    /**
     * Delete headcount by ID and tenant
     */
    void deleteByIdAndTenantId(String id, String tenantId);

    /**
     * Delete all headcounts for a tenant and period
     */
    void deleteByTenantIdAndPeriod(String tenantId, String period);

    /**
     * Check if headcount exists for tenant
     */
    boolean existsByIdAndTenantId(String id, String tenantId);

    /**
     * Batch save headcounts
     */
    List<CountryHeadcount> saveAll(List<CountryHeadcount> headcounts);

    /**
     * Count headcounts by tenant and country
     */
    long countByTenantIdAndCountryCode(String tenantId, String countryCode);

    /**
     * Sum total headcount for a tenant and period
     */
    Integer sumTotalHeadcountByTenantIdAndPeriod(String tenantId, String period);

    /**
     * Find headcounts by tenant, region and period
     */
    List<CountryHeadcount> findByTenantIdAndRegionCodeAndPeriod(String tenantId, String regionCode, String period);

    /**
     * Find latest headcount for each country for a given period
     */
    List<CountryHeadcount> findLatestByPeriod(String tenantId, String period);
}
