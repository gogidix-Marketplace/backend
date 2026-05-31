package com.gogidix.sales.analytics.domain.repository;

import com.gogidix.sales.analytics.domain.model.PipelineMetric;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Pipeline Metric Repository Interface (Port)
 * Defines the contract for pipeline metric persistence operations
 */
public interface PipelineMetricRepository {

    PipelineMetric save(PipelineMetric metric);

    List<PipelineMetric> saveAll(List<PipelineMetric> metrics);

    Optional<PipelineMetric> findById(String id);

    Optional<PipelineMetric> findByPipelineMetricIdAndTenantId(String metricId, String tenantId);

    List<PipelineMetric> findByTenantId(String tenantId);

    List<PipelineMetric> findByTenantIdAndEntityType(String tenantId, String entityType);

    List<PipelineMetric> findByTenantIdAndEntityTypeAndEntityId(
            String tenantId, String entityType, String entityId);

    List<PipelineMetric> findByTenantIdAndPeriodBetween(
            String tenantId, LocalDate startDate, LocalDate endDate);

    List<PipelineMetric> findByTenantIdAndPeriod(String tenantId, PipelineMetric.MetricPeriod period);

    List<PipelineMetric> findByTenantIdAndHealth(String tenantId, PipelineMetric.PipelineHealth health);

    List<PipelineMetric> findByTenantIdAndHealthScoreLessThan(String tenantId, Integer score);

    Optional<PipelineMetric> findLatestByTenantIdAndEntity(
            String tenantId, String entityType, String entityId);

    List<PipelineMetric> findAtRiskPipelinesByTenantId(String tenantId);

    List<PipelineMetric> findByTenantIdOrderByHealthScoreAsc(String tenantId);

    boolean existsByPipelineMetricIdAndTenantId(String metricId, String tenantId);

    void deleteById(String id);

    void deleteByPipelineMetricIdAndTenantId(String metricId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    void deleteByTenantIdAndPeriod(String tenantId, PipelineMetric.MetricPeriod period);

    long countByTenantId(String tenantId);

    long countByTenantIdAndHealth(String tenantId, PipelineMetric.PipelineHealth health);
}
