package com.gogidix.infrastructure.database.domain.repository;

import com.gogidix.infrastructure.database.domain.model.QueryPerformanceMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for managing query performance metrics.
 */
@Repository
public interface QueryPerformanceMetricRepository extends MongoRepository<QueryPerformanceMetric, String> {

    /**
     * Find by tenant ID.
     */
    List<QueryPerformanceMetric> findAllByTenantId(String tenantId);

    /**
     * Find by tenant ID paginated.
     */
    Page<QueryPerformanceMetric> findAllByTenantId(String tenantId, Pageable pageable);

    /**
     * Find by tenant ID and database name.
     */
    List<QueryPerformanceMetric> findAllByTenantIdAndDatabaseName(String tenantId, String databaseName);

    /**
     * Find by tenant ID and query hash.
     */
    List<QueryPerformanceMetric> findAllByTenantIdAndQueryHashOrderByExecutedAtDesc(
            String tenantId, String queryHash);

    /**
     * Find by tenant ID and performance tier.
     */
    List<QueryPerformanceMetric> findAllByTenantIdAndPerformanceTier(
            String tenantId,
            QueryPerformanceMetric.PerformanceTier performanceTier);

    /**
     * Find slow queries.
     */
    @Query("{'executionDurationMs': {$gt: ?0}, 'tenantId': ?1}")
    List<QueryPerformanceMetric> findSlowQueries(Long thresholdMs, String tenantId);

    /**
     * Find very slow queries.
     */
    @Query("{'executionDurationMs': {$gt: 5000}, 'tenantId': ?0}")
    List<QueryPerformanceMetric> findVerySlowQueries(String tenantId);

    /**
     * Find failed queries.
     */
    List<QueryPerformanceMetric> findAllByTenantIdAndStatus(
            String tenantId,
            QueryPerformanceMetric.QueryStatus status);

    /**
     * Find by query type.
     */
    List<QueryPerformanceMetric> findAllByTenantIdAndQueryType(
            String tenantId,
            QueryPerformanceMetric.QueryType queryType);

    /**
     * Find queries within time range.
     */
    List<QueryPerformanceMetric> findAllByTenantIdAndExecutedAtBetween(
            String tenantId,
            LocalDateTime startTime,
            LocalDateTime endTime);

    /**
     * Find by application name.
     */
    List<QueryPerformanceMetric> findAllByTenantIdAndApplicationName(
            String tenantId,
            String applicationName);

    /**
     * Find by username.
     */
    List<QueryPerformanceMetric> findAllByTenantIdAndUsername(
            String tenantId,
            String username);

    /**
     * Find queries with alerts.
     */
    @Query("{'alertsTriggered': {$ne: null}, 'tenantId': ?0}")
    List<QueryPerformanceMetric> findProductQueriesWithAlerts(String tenantId);

    /**
     * Find queries that had full table scans.
     */
    @Query("{'sequentialScan': true, 'tenantId': ?0, 'indexesUsed': {$size: 0}}")
    List<QueryPerformanceMetric> findQueriesWithFullTableScan(String tenantId);

    /**
     * Count by tenant ID.
     */
    Long countByTenantId(String tenantId);

    /**
     * Count by tenant ID and status.
     */
    Long countByTenantIdAndStatus(String tenantId, QueryPerformanceMetric.QueryStatus status);

    /**
     * Count by tenant ID and performance tier.
     */
    Long countByTenantIdAndPerformanceTier(
            String tenantId,
            QueryPerformanceMetric.PerformanceTier performanceTier);

    /**
     * Delete metrics older than given date.
     */
    void deleteByExecutedAtBefore(LocalDateTime date);

    /**
     * Delete by tenant ID.
     */
    void deleteByTenantId(String tenantId);

    /**
     * Get top N slowest queries.
     */
    List<QueryPerformanceMetric> findTop10ByTenantIdOrderByExecutionDurationMsDesc(String tenantId);

    /**
     * Get aggregated statistics by query type.
     */
    @Aggregation(pipeline = {
            "{$match: {'tenantId': ?0}}",
            "{$group: {_id: '$queryType', count: {$sum: 1}, avgDuration: {$avg: '$executionDurationMs'}}}"
    })
    List<QueryStatistics> getStatisticsByQueryType(String tenantId);

    /**
     * Get aggregated statistics by database.
     */
    @Aggregation(pipeline = {
            "{$match: {'tenantId': ?0}}",
            "{$group: {_id: '$databaseName', count: {$sum: 1}, avgDuration: {$avg: '$executionDurationMs'}, totalDuration: {$sum: '$executionDurationMs'}}}"
    })
    List<QueryStatistics> getStatisticsByDatabase(String tenantId);

    /**
     * Find queries referencing a specific table.
     */
    @Query("{'referencedTables': {$in: [?0]}, 'tenantId': ?1}")
    List<QueryPerformanceMetric> findByReferencedTable(String tableName, String tenantId);

    /**
     * Count by query hash.
     */
    Long countByTenantIdAndQueryHash(String tenantId, String queryHash);

    /**
     * Find paginated with filters.
     */
    Page<QueryPerformanceMetric> findAllByTenantIdAndDatabaseNameAndExecutedAtBetween(
            String tenantId,
            String databaseName,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Pageable pageable);

    /**
     * Query statistics result.
     */
    interface QueryStatistics {
        String get_id();
        Long getCount();
        Double getAvgDuration();
        Double getTotalDuration();
    }
}
