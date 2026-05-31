package com.gogidix.customersupport.globalsupportdashboard.domain.repository;

import com.gogidix.customersupport.globalsupportdashboard.domain.model.SupportMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for SupportMetrics entity
 */
@Repository
public interface SupportMetricsRepository extends MongoRepository<SupportMetrics, String> {

    /**
     * Find the most recent metrics for a tenant
     */
    Optional<SupportMetrics> findFirstByTenantIdOrderByUpdatedAtDesc(String tenantId);

    /**
     * Find metrics within a date range
     */
    List<SupportMetrics> findByTenantIdAndMetricEndDateBetweenOrderByUpdatedAtDesc(
            String tenantId, Instant startDate, Instant endDate);

    /**
     * Find metrics by aggregation type
     */
    List<SupportMetrics> findByTenantIdAndAggregationTypeOrderByUpdatedAtDesc(
            String tenantId, String aggregationType);

    /**
     * Find the most recent real-time metrics
     */
    Optional<SupportMetrics> findFirstByTenantIdAndIsRealTimeTrueOrderByUpdatedAtDesc(String tenantId);

    /**
     * Find metrics needing refresh (stale data)
     */
    @Query("{ 'tenantId': ?0, 'lastRefreshedAt': { $lt: ?1 } }")
    List<SupportMetrics> findStaleMetrics(String tenantId, Instant staleThreshold);

    /**
     * Delete metrics older than specified date
     */
    void deleteByTenantIdAndUpdatedAtBefore(String tenantId, Instant cutoffDate);

    /**
     * Count metrics by tenant
     */
    long countByTenantId(String tenantId);
}
