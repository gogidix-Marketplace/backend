package com.gogidix.hr.globalhrdashboard.domain.repository;

import com.gogidix.hr.globalhrdashboard.domain.model.ComplianceMetric;
import com.gogidix.hr.globalhrdashboard.domain.model.ComplianceStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository Interface for ComplianceMetric aggregate
 * Following Hexagonal Architecture - this is a PORT (out)
 */
public interface ComplianceMetricRepository {

    /**
     * Save a compliance metric
     */
    ComplianceMetric save(ComplianceMetric metric);

    /**
     * Find metric by ID and tenant
     */
    Optional<ComplianceMetric> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find all metrics for a tenant
     */
    List<ComplianceMetric> findByTenantId(String tenantId);

    /**
     * Find metrics by tenant and country code
     */
    List<ComplianceMetric> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    /**
     * Find metric by tenant, country code and period
     */
    Optional<ComplianceMetric> findByTenantIdAndCountryCodeAndPeriod(String tenantId, String countryCode, String period);

    /**
     * Find metrics by tenant and period
     */
    List<ComplianceMetric> findByTenantIdAndPeriod(String tenantId, String period);

    /**
     * Find metrics by tenant and region
     */
    List<ComplianceMetric> findByTenantIdAndRegionCode(String tenantId, String regionCode);

    /**
     * Find metrics by tenant and status
     */
    List<ComplianceMetric> findByTenantIdAndStatus(String tenantId, ComplianceStatus status);

    /**
     * Find metrics at risk for a tenant
     */
    List<ComplianceMetric> findAtRiskByTenantId(String tenantId);

    /**
     * Find non-compliant metrics for a tenant
     */
    List<ComplianceMetric> findNonCompliantByTenantId(String tenantId);

    /**
     * Find metrics with critical issues for a tenant
     */
    List<ComplianceMetric> findWithCriticalIssuesByTenantId(String tenantId);

    /**
     * Find active metrics for a tenant
     */
    List<ComplianceMetric> findActiveByTenantId(String tenantId);

    /**
     * Find metrics assessed since a specific time
     */
    List<ComplianceMetric> findByTenantIdAndLastAssessedAfter(String tenantId, Instant lastAssessed);

    /**
     * Find metrics needing assessment
     */
    List<ComplianceMetric> findMetricsNeedingAssessment(String tenantId, Instant before);

    /**
     * Find metrics with compliance score below threshold
     */
    List<ComplianceMetric> findByTenantIdAndComplianceScoreLessThan(String tenantId, Double score);

    /**
     * Find metrics with compliance score above threshold
     */
    List<ComplianceMetric> findByTenantIdAndComplianceScoreGreaterThanEqual(String tenantId, Double score);

    /**
     * Get average compliance score for a tenant and period
     */
    Double getAverageComplianceScore(String tenantId, String period);

    /**
     * Get global compliance status for a tenant and period
     */
    Optional<ComplianceStatus> getGlobalComplianceStatus(String tenantId, String period);

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
    List<ComplianceMetric> saveAll(List<ComplianceMetric> metrics);

    /**
     * Count metrics by tenant and status
     */
    long countByTenantIdAndStatus(String tenantId, ComplianceStatus status);

    /**
     * Count metrics with critical issues by tenant
     */
    long countWithCriticalIssuesByTenantId(String tenantId);

    /**
     * Find compliance trend over time for a country
     */
    List<ComplianceMetric> findTrendDataByCountry(String tenantId, String countryCode,
                                                    String startPeriod, String endPeriod);

    /**
     * Find latest compliance metric for each country for a given period
     */
    List<ComplianceMetric> findLatestByPeriod(String tenantId, String period);
}
