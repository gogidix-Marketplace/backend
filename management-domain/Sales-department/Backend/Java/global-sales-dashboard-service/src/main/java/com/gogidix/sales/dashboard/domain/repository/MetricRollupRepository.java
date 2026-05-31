package com.gogidix.sales.dashboard.domain.repository;

import com.gogidix.sales.dashboard.domain.model.MetricRollup;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Metric Rollup Repository Interface (Port)
 * Defines the contract for rollup persistence operations
 */
public interface MetricRollupRepository {

    MetricRollup save(MetricRollup rollup);

    List<MetricRollup> saveAll(List<MetricRollup> rollups);

    Optional<MetricRollup> findById(String id);

    Optional<MetricRollup> findByRollupIdAndTenantId(String rollupId, String tenantId);

    List<MetricRollup> findByTenantId(String tenantId);

    List<MetricRollup> findByTenantIdAndRollupType(String tenantId,
                                                      MetricRollup.RollupType type);

    List<MetricRollup> findByTenantIdAndRollupKey(String tenantId, String rollupKey);

    List<MetricRollup> findByTenantIdAndParentRollupId(String tenantId, String parentRollupId);

    List<MetricRollup> findByTenantIdAndTimePeriod(String tenantId,
                                                     MetricRollup.TimePeriod timePeriod);

    List<MetricRollup> findGlobalRollupByTenantId(String tenantId);

    List<MetricRollup> findRegionalRollupsByTenantId(String tenantId);

    List<MetricRollup> findByTenantIdAndRollupTypeAndTimePeriod(
            String tenantId,
            MetricRollup.RollupType type,
            MetricRollup.TimePeriod timePeriod);

    List<MetricRollup> findChildRollups(String parentRollupId, String tenantId);

    Optional<MetricRollup> findLatestByTypeAndKey(String tenantId,
                                                   MetricRollup.RollupType type,
                                                   String rollupKey);

    List<MetricRollup> findByTenantIdAndDataVersion(String tenantId, Integer dataVersion);

    boolean existsByRollupIdAndTenantId(String rollupId, String tenantId);

    void deleteById(String id);

    void deleteByRollupIdAndTenantId(String rollupId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    void deleteOldRollups(String tenantId, LocalDate beforeDate);

    long countByTenantId(String tenantId);

    long countByTenantIdAndRollupType(String tenantId, MetricRollup.RollupType type);

    List<MetricRollup> findRealtimeRollupsByTenantId(String tenantId);
}
