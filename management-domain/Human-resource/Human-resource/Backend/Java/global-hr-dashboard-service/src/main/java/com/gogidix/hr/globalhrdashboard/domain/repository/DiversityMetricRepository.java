package com.gogidix.hr.globalhrdashboard.domain.repository;

import com.gogidix.hr.globalhrdashboard.domain.model.DiversityMetric;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository Interface for DiversityMetric aggregate
 * Following Hexagonal Architecture - this is a PORT (out)
 */
public interface DiversityMetricRepository {

    /**
     * Save a diversity metric
     */
    DiversityMetric save(DiversityMetric metric);

    /**
     * Find metric by ID and tenant
     */
    Optional<DiversityMetric> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find all metrics for a tenant
     */
    List<DiversityMetric> findByTenantId(String tenantId);

    /**
     * Find metrics by tenant and country code
     */
    List<DiversityMetric> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    /**
     * Find metric by tenant, country code and period
     */
    Optional<DiversityMetric> findByTenantIdAndCountryCodeAndPeriod(String tenantId, String countryCode, String period);

    /**
     * Find metrics by tenant and period
     */
    List<DiversityMetric> findByTenantIdAndPeriod(String tenantId, String period);

    /**
     * Find metrics by tenant and region
     */
    List<DiversityMetric> findByTenantIdAndRegionCode(String tenantId, String regionCode);

    /**
     * Find active metrics for a tenant
     */
    List<DiversityMetric> findActiveByTenantId(String tenantId);

    /**
     * Find metrics updated since a specific time
     */
    List<DiversityMetric> findByTenantIdAndLastUpdatedAfter(String tenantId, Instant lastUpdated);

    /**
     * Find metrics with high gender diversity score
     */
    List<DiversityMetric> findWithHighGenderDiversity(String tenantId, Double minScore);

    /**
     * Find metrics with high national diversity score
     */
    List<DiversityMetric> findWithHighNationalDiversity(String tenantId, Double minScore);

    /**
     * Find metrics with high overall diversity score
     */
    List<DiversityMetric> findWithHighOverallDiversity(String tenantId, Double minScore);

    /**
     * Get average gender diversity score for a tenant and period
     */
    Double getAverageGenderDiversityScore(String tenantId, String period);

    /**
     * Get average national diversity score for a tenant and period
     */
    Double getAverageNationalDiversityScore(String tenantId, String period);

    /**
     * Get average overall diversity score for a tenant and period
     */
    Double getAverageOverallDiversityScore(String tenantId, String period);

    /**
     * Find metrics needing update
     */
    List<DiversityMetric> findMetricsNeedingUpdate(String tenantId, Instant before);

    /**
     * Delete metric by ID and tenant
     */
    void deleteByIdAndTenantId(String id, String tenantId);

    /**
     * Delete all metrics for a tenant and period
     */
    void deleteByTenantIdAndPeriod(String tenantId, String period);

    /**
     * Check if metric exists for tenant
     */
    boolean existsByIdAndTenantId(String id, String tenantId);

    /**
     * Batch save metrics
     */
    List<DiversityMetric> saveAll(List<DiversityMetric> metrics);

    /**
     * Count metrics by tenant and country
     */
    long countByTenantIdAndCountryCode(String tenantId, String countryCode);

    /**
     * Find diversity trend over time for a country
     */
    List<DiversityMetric> findTrendDataByCountry(String tenantId, String countryCode,
                                                  String startPeriod, String endPeriod);

    /**
     * Find diversity trend over time for a region
     */
    List<DiversityMetric> findTrendDataByRegion(String tenantId, String regionCode,
                                                 String startPeriod, String endPeriod);

    /**
     * Find latest diversity metric for each country for a given period
     */
    List<DiversityMetric> findLatestByPeriod(String tenantId, String period);

    /**
     * Find global diversity aggregate for a tenant and period
     */
    Optional<DiversityMetric> findGlobalAggregateByTenantIdAndPeriod(String tenantId, String period);

    /**
     * Get countries with highest diversity score for a tenant and period
     */
    List<DiversityMetric> findTopCountriesByDiversityScore(String tenantId, String period, int limit);
}
