package com.gogidix.infrastructure.database.application.service;

import com.gogidix.infrastructure.database.domain.model.QueryPerformanceMetric;
import com.gogidix.infrastructure.database.domain.repository.QueryPerformanceMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for monitoring query performance.
 *
 * <p>Tracks query execution metrics, identifies slow queries,
 * and provides optimization insights.</p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QueryMonitoringService {

    private final QueryPerformanceMetricRepository repository;

    @Value("${infrastructure-database.monitoring.slow-query-threshold:1000}")
    private Long slowQueryThreshold;

    @Value("${infrastructure-database.monitoring.query-logging-enabled:true}")
    private Boolean queryLoggingEnabled;

    @Value("${infrastructure-database.monitoring.metrics-collection-enabled:true}")
    private Boolean metricsCollectionEnabled;

    /**
     * Record a query metric.
     */
    @Transactional
    @CacheEvict(value = "queryMetrics", allEntries = true)
    public QueryPerformanceMetric recordQuery(@Valid QueryPerformanceMetric metric) {
        if (!metricsCollectionEnabled) {
            log.debug("Metrics collection disabled, skipping query metric recording");
            return metric;
        }

        // Calculate performance tier
        metric.calculatePerformanceTier(slowQueryThreshold);

        // Check for slow queries and log
        if (metric.isSlow(slowQueryThreshold)) {
            log.warn("Slow query detected: {}ms - {}", metric.getExecutionDurationMs(), metric.getQueryText());
            if (metric.getAlertsTriggered() == null) {
                metric.setAlertsTriggered(new HashSet<>());
            }
            metric.getAlertsTriggered().add("SLOW_QUERY");
        }

        // Check for very slow queries
        if (metric.isVerySlow()) {
            if (metric.getAlertsTriggered() == null) {
                metric.setAlertsTriggered(new HashSet<>());
            }
            metric.getAlertsTriggered().add("VERY_SLOW_QUERY");
        }

        // Check for full table scans
        if (metric.hadFullTableScan()) {
            if (metric.getAlertsTriggered() == null) {
                metric.setAlertsTriggered(new HashSet<>());
            }
            metric.getAlertsTriggered().add("FULL_TABLE_SCAN");

            // Add optimization suggestion
            if (metric.getOptimizationSuggestions() == null) {
                metric.setOptimizationSuggestions(new HashSet<>());
            }
            metric.getOptimizationSuggestions().add("Consider adding indexes on queried columns");
        }

        QueryPerformanceMetric saved = repository.save(metric);

        if (queryLoggingEnabled) {
            logQuery(saved);
        }

        return saved;
    }

    /**
     * Log query metric.
     */
    private void logQuery(QueryPerformanceMetric metric) {
        if (log.isDebugEnabled()) {
            log.debug("Query: {} | Duration: {}ms | Type: {} | DB: {}",
                    metric.getQueryType(),
                    metric.getExecutionDurationMs(),
                    metric.getPerformanceTier(),
                    metric.getDatabaseName());
        }
    }

    /**
     * Get query metrics for a tenant.
     */
    @Cacheable(value = "queryMetrics", key = "#tenantId")
    public List<QueryPerformanceMetric> getQueryMetrics(String tenantId) {
        return repository.findAllByTenantId(tenantId);
    }

    /**
     * Get query metrics paginated.
     */
    public Page<QueryPerformanceMetric> getQueryMetrics(String tenantId, Pageable pageable) {
        return repository.findAllByTenantId(tenantId, pageable);
    }

    /**
     * Get slow queries.
     */
    public List<QueryPerformanceMetric> getSlowQueries(String tenantId) {
        return repository.findSlowQueries(slowQueryThreshold, tenantId);
    }

    /**
     * Get very slow queries.
     */
    public List<QueryPerformanceMetric> getVerySlowQueries(String tenantId) {
        return repository.findVerySlowQueries(tenantId);
    }

    /**
     * Get queries with full table scans.
     */
    public List<QueryPerformanceMetric> getQueriesWithFullTableScans(String tenantId) {
        return repository.findQueriesWithFullTableScan(tenantId);
    }

    /**
     * Get failed queries.
     */
    public List<QueryPerformanceMetric> getFailedQueries(String tenantId) {
        return repository.findAllByTenantIdAndStatus(tenantId, QueryPerformanceMetric.QueryStatus.FAILED);
    }

    /**
     * Get top N slowest queries.
     */
    public List<QueryPerformanceMetric> getTopSlowestQueries(String tenantId, int limit) {
        return repository.findTop10ByTenantIdOrderByExecutionDurationMsDesc(tenantId)
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get query statistics by database.
     */
    public Map<String, Object> getQueryStatisticsByDatabase(String tenantId, String databaseName) {
        List<QueryPerformanceMetric> metrics = repository.findAllByTenantIdAndDatabaseName(
                tenantId, databaseName);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalQueries", metrics.size());
        stats.put("databaseName", databaseName);

        if (metrics.isEmpty()) {
            return stats;
        }

        stats.put("avgDuration", metrics.stream()
                .mapToLong(QueryPerformanceMetric::getExecutionDurationMs)
                .average()
                .orElse(0));

        stats.put("maxDuration", metrics.stream()
                .mapToLong(QueryPerformanceMetric::getExecutionDurationMs)
                .max()
                .orElse(0));

        stats.put("minDuration", metrics.stream()
                .mapToLong(QueryPerformanceMetric::getExecutionDurationMs)
                .min()
                .orElse(0));

        stats.put("totalDuration", metrics.stream()
                .mapToLong(QueryPerformanceMetric::getExecutionDurationMs)
                .sum());

        long slowCount = metrics.stream()
                .filter(m -> m.isSlow(slowQueryThreshold))
                .count();
        stats.put("slowQueries", slowCount);
        stats.put("slowQueryPercentage", (slowCount * 100.0) / metrics.size());

        Map<String, Long> countsByType = new HashMap<>();
        for (QueryPerformanceMetric.QueryType type : QueryPerformanceMetric.QueryType.values()) {
            long count = metrics.stream()
                    .filter(m -> m.getQueryType() == type)
                    .count();
            if (count > 0) {
                countsByType.put(type.name(), count);
            }
        }
        stats.put("byType", countsByType);

        Map<String, Long> countsByTier = new HashMap<>();
        for (QueryPerformanceMetric.PerformanceTier tier : QueryPerformanceMetric.PerformanceTier.values()) {
            long count = metrics.stream()
                    .filter(m -> m.getPerformanceTier() == tier)
                    .count();
            countsByTier.put(tier.name(), count);
        }
        stats.put("byPerformanceTier", countsByTier);

        return stats;
    }

    /**
     * Get overall query statistics for tenant.
     */
    public Map<String, Object> getOverallQueryStatistics(String tenantId) {
        Map<String, Object> stats = new HashMap<>();

        List<QueryPerformanceMetric> allMetrics = repository.findAllByTenantId(tenantId);

        stats.put("totalQueries", allMetrics.size());
        stats.put("tenantId", tenantId);

        if (allMetrics.isEmpty()) {
            return stats;
        }

        stats.put("avgDuration", allMetrics.stream()
                .mapToLong(QueryPerformanceMetric::getExecutionDurationMs)
                .average()
                .orElse(0));

        stats.put("totalDuration", allMetrics.stream()
                .mapToLong(QueryPerformanceMetric::getExecutionDurationMs)
                .sum());

        long failedCount = repository.countByTenantIdAndStatus(
                tenantId, QueryPerformanceMetric.QueryStatus.FAILED);
        stats.put("failedQueries", failedCount);
        stats.put("successRate", 100.0 - (failedCount * 100.0 / allMetrics.size()));

        // Database breakdown
        Map<String, Long> byDatabase = allMetrics.stream()
                .collect(Collectors.groupingBy(
                        QueryPerformanceMetric::getDatabaseName,
                        Collectors.counting()
                ));
        stats.put("byDatabase", byDatabase);

        return stats;
    }

    /**
     * Get queries by hash (same query pattern).
     */
    public List<QueryPerformanceMetric> getQueriesByHash(String tenantId, String queryHash) {
        return repository.findAllByTenantIdAndQueryHashOrderByExecutedAtDesc(tenantId, queryHash);
    }

    /**
     * Get queries within time range.
     */
    public List<QueryPerformanceMetric> getQueriesInTimeRange(
            String tenantId,
            LocalDateTime startTime,
            LocalDateTime endTime) {
        return repository.findAllByTenantIdAndExecutedAtBetween(tenantId, startTime, endTime);
    }

    /**
     * Delete old metrics.
     */
    @Transactional
    @Scheduled(cron = "0 0 2 * * ?") // Run at 2 AM daily
    public void deleteOldMetrics() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        log.info("Deleting query metrics older than {}", cutoff);

        try {
            repository.deleteByExecutedAtBefore(cutoff);
            log.info("Old query metrics deleted successfully");
        } catch (Exception e) {
            log.error("Failed to delete old metrics: {}", e.getMessage());
        }
    }

    /**
     * Get optimization suggestions.
     */
    public List<String> getOptimizationSuggestions(String tenantId) {
        List<QueryPerformanceMetric> problematicQueries = new ArrayList<>();
        problematicQueries.addAll(getSlowQueries(tenantId));
        problematicQueries.addAll(getQueriesWithFullTableScans(tenantId));

        Set<String> suggestions = new HashSet<>();

        for (QueryPerformanceMetric metric : problematicQueries) {
            if (metric.getOptimizationSuggestions() != null) {
                suggestions.addAll(metric.getOptimizationSuggestions());
            }

            if (metric.hadFullTableScan()) {
                suggestions.add("Query had full table scan - add indexes on: " +
                        (metric.getReferencedTables() != null ? metric.getReferencedTables() : "unknown tables"));
            }
        }

        return new ArrayList<>(suggestions);
    }

    /**
     * Compare queries before and after optimization.
     */
    public Map<String, Object> compareQueryPerformance(String tenantId, String queryHash) {
        List<QueryPerformanceMetric> metrics = repository.findAllByTenantIdAndQueryHashOrderByExecutedAtDesc(
                tenantId, queryHash);

        if (metrics.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Object> comparison = new HashMap<>();
        comparison.put("queryHash", queryHash);
        comparison.put("executionCount", metrics.size());
        comparison.put("normalizedQuery", metrics.get(0).getNormalizedQuery());

        comparison.put("avgDuration", metrics.stream()
                .mapToLong(QueryPerformanceMetric::getExecutionDurationMs)
                .average()
                .orElse(0));

        comparison.put("minDuration", metrics.stream()
                .mapToLong(QueryPerformanceMetric::getExecutionDurationMs)
                .min()
                .orElse(0));

        comparison.put("maxDuration", metrics.stream()
                .mapToLong(QueryPerformanceMetric::getExecutionDurationMs)
                .max()
                .orElse(0));

        comparison.put("lastExecuted", metrics.get(0).getExecutedAt());
        comparison.put("firstExecuted", metrics.get(metrics.size() - 1).getExecutedAt());

        return comparison;
    }
}
