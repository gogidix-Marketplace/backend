package com.gogidix.customersupport.globalsupportdashboard.domain.repository;

import com.gogidix.customersupport.globalsupportdashboard.domain.model.RegionalMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for RegionalMetrics entity
 */
@Repository
public interface RegionalMetricsRepository extends MongoRepository<RegionalMetrics, String> {

    /**
     * Find all regional metrics for a tenant
     */
    List<RegionalMetrics> findByTenantIdOrderByRegionNameAsc(String tenantId);

    /**
     * Find specific region metrics
     */
    Optional<RegionalMetrics> findByTenantIdAndRegionCode(String tenantId, String regionCode);

    /**
     * Find most recent metrics for a specific region
     */
    Optional<RegionalMetrics> findFirstByTenantIdAndRegionCodeOrderByMetricDateDesc(
            String tenantId, String regionCode);

    /**
     * Find metrics within a date range for a region
     */
    List<RegionalMetrics> findByTenantIdAndRegionCodeAndMetricDateBetweenOrderByMetricDateDesc(
            String tenantId, String regionCode, Instant startDate, Instant endDate);

    /**
     * Find all regions sorted by ticket volume
     */
    @Query("{ 'tenantId': ?0 }")
    List<RegionalMetrics> findByTenantIdOrderByTotalTicketsDesc(String tenantId);

    /**
     * Find regions needing attention (high ticket volume, low SLA)
     */
    @Query("{ 'tenantId': ?0, 'slaCompliancePercentage': { $lt: ?1 } }")
    List<RegionalMetrics> findRegionsWithLowSla(String tenantId, double slaThreshold);

    /**
     * Get regional trends
     */
    List<RegionalMetrics> findByTenantIdAndAggregationTypeOrderByMetricDateDesc(
            String tenantId, String aggregationType);
}
