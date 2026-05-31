package com.gogidix.infrastructure.database.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Domain model representing query performance metrics.
 *
 * <p>Tracks and analyzes database query performance including:</p>
 * <ul>
 *   <li>Execution time</li>
 *   <li>Rows affected</li>
 *   <li>Query patterns</li>
 *   <li>Slow queries</li>
 *   <li>Query frequency</li>
 * </ul>
 *
 * <p>Provides insights for optimization and performance tuning.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "query_performance_metrics")
@CompoundIndex(def = "{'tenantId': 1, 'queryHash': 1, 'executedAt': -1}")
@CompoundIndex(def = "{'tenantId': 1, 'databaseName': 1, 'executedAt': -1}")
@CompoundIndex(def = "{'executedAt': -1}")
public class QueryPerformanceMetric {

    /**
     * Unique identifier for the metric record.
     */
    @Id
    private String id;

    /**
     * Tenant identifier for multi-tenancy support.
     */
    @Indexed
    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    /**
     * Target database name.
     */
    @Indexed
    private String databaseName;

    /**
     * Connection pool name used.
     */
    private String connectionPoolName;

    /**
     * Query type (SELECT, INSERT, UPDATE, DELETE, etc.).
     */
    @NotNull(message = "Query type is required")
    private QueryType queryType;

    /**
     * Normalized query hash for grouping similar queries.
     */
    @Indexed
    @NotBlank(message = "Query hash is required")
    private String queryHash;

    /**
     * Original SQL query.
     */
    @NotBlank(message = "Query text is required")
    private String queryText;

    /**
     * Normalized query text (with parameter placeholders).
     */
    private String normalizedQuery;

    /**
     * Query fingerprint for pattern matching.
     */
    private String queryFingerprint;

    /**
     * Tables referenced in the query.
     */
    private java.util.Set<String> referencedTables;

    /**
     * Execution start time.
     */
    private LocalDateTime executionStartTime;

    /**
     * Execution end time.
     */
    private LocalDateTime executionEndTime;

    /**
     * Query execution timestamp.
     */
    @Indexed
    @NotNull(message = "Execution timestamp is required")
    private LocalDateTime executedAt;

    /**
     * Execution duration in milliseconds.
     */
    @Indexed
    @NotNull(message = "Execution duration is required")
    private Long executionDurationMs;

    /**
     * CPU time used.
     */
    private Long cpuTimeMs;

    /**
     * Wait time.
     */
    private Long waitTimeMs;

    /**
     * Rows read.
     */
    private Long rowsRead;

    /**
     * Rows written.
     */
    private Long rowsWritten;

    /**
     * Rows affected/returned.
     */
    private Long rowsAffected;

    /**
     * Bytes read from disk.
     */
    private Long bytesRead;

    /**
     * Bytes written to disk.
     */
    private Long bytesWritten;

    /**
     * Network bytes sent.
     */
    private Long networkBytesSent;

    /**
     * Network bytes received.
     */
    private Long networkBytesReceived;

    /**
     * Number of index scans.
     */
    private Integer indexScans;

    /**
     * Indexes used.
     */
    private java.util.Set<String> indexesUsed;

    /**
     * Sequential scan flag.
     */
    private Boolean sequentialScan;

    /**
     * Sort operations.
     */
    private Boolean hadSort;

    /**
     * Hash operations.
     */
    private Boolean hadHashJoin;

    /**
     * Nested loop joins.
     */
    private Boolean hadNestedLoop;

    /**
     * Transaction ID if part of transaction.
     */
    private String transactionId;

    /**
     * Whether query was prepared statement.
     */
    @Builder.Default
    private Boolean isPrepared = false;

    /**
     * Statement name if prepared.
     */
    private String statementName;

    /**
     * Number of parameters.
     */
    private Integer parameterCount;

    /**
     * Application/service name.
     */
    private String applicationName;

    /**
     * User who executed the query.
     */
    private String username;

    /**
     * Client IP address.
     */
    private String clientIp;

    /**
     * Session ID.
     */
    private String sessionId;

    /**
     * Query execution status.
     */
    @NotNull(message = "Status is required")
    @Builder.Default
    private QueryStatus status = QueryStatus.SUCCESS;

    /**
     * Error code if failed.
     */
    private String errorCode;

    /**
     * Error message if failed.
     */
    private String errorMessage;

    /**
     * Stack trace if error.
     */
    private String errorStackTrace;

    /**
     * Performance tier (FAST, NORMAL, SLOW, VERY_SLOW).
     */
    @Indexed
    private PerformanceTier performanceTier;

    /**
     * Alert threshold exceeded.
     */
    private java.util.Set<String> alertsTriggered;

    /**
     * Query optimization suggestions.
     */
    private java.util.Set<String> optimizationSuggestions;

    /**
     * Explain plan JSON.
     */
    private String explainPlan;

    /**
     * Plan cost estimate.
     */
    private Double planCost;

    /**
     * Plan actual cost.
     */
    private Double actualCost;

    /**
     * Metadata associated with this metric.
     */
    private Map<String, Object> metadata;

    /**
     * Timestamp when this metric was created.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * Query type enumeration.
     */
    public enum QueryType {
        SELECT,
        INSERT,
        UPDATE,
        DELETE,
        CREATE,
        ALTER,
        DROP,
        TRUNCATE,
        MERGE,
        CALL,
        EXPLAIN,
        SHOW,
        DESCRIBE,
        BEGIN,
        COMMIT,
        ROLLBACK,
        OTHER
    }

    /**
     * Query status enumeration.
     */
    public enum QueryStatus {
        SUCCESS,
        FAILED,
        TIMEOUT,
        CANCELLED,
        RETRIED
    }

    /**
     * Performance tier enumeration.
     */
    public enum PerformanceTier {
        FAST(0L, 50L),
        NORMAL(50L, 500L),
        SLOW(500L, 2000L),
        VERY_SLOW(2000L, null),
        CRITICAL(null, null);

        private final Long minMs;
        private final Long maxMs;

        PerformanceTier(Long minMs, Long maxMs) {
            this.minMs = minMs;
            this.maxMs = maxMs;
        }

        public static PerformanceTier fromDuration(Long durationMs) {
            if (durationMs == null) {
                return NORMAL;
            }
            if (durationMs < FAST.maxMs) {
                return FAST;
            } else if (durationMs < NORMAL.maxMs) {
                return NORMAL;
            } else if (durationMs < SLOW.maxMs) {
                return SLOW;
            } else if (durationMs < VERY_SLOW.maxMs || durationMs >= VERY_SLOW.minMs) {
                return VERY_SLOW;
            }
            return CRITICAL;
        }
    }

    /**
     * Determines performance tier based on execution duration.
     */
    public void calculatePerformanceTier(Long slowQueryThreshold) {
        if (executionDurationMs == null) {
            performanceTier = PerformanceTier.NORMAL;
            return;
        }

        performanceTier = PerformanceTier.fromDuration(executionDurationMs);

        // Check against custom slow query threshold
        if (slowQueryThreshold != null && executionDurationMs > slowQueryThreshold) {
            if (performanceTier == PerformanceTier.NORMAL || performanceTier == PerformanceTier.FAST) {
                performanceTier = PerformanceTier.SLOW;
            }
        }
    }

    /**
     * Checks if query is considered slow.
     */
    public boolean isSlow(Long slowQueryThreshold) {
        if (executionDurationMs == null) {
            return false;
        }
        long threshold = slowQueryThreshold != null ? slowQueryThreshold : 1000L;
        return executionDurationMs > threshold;
    }

    /**
     * Checks if query is very slow.
     */
    public boolean isVerySlow() {
        if (executionDurationMs == null) {
            return false;
        }
        return executionDurationMs > 5000L;
    }

    /**
     * Calculates throughput metric (rows per second).
     */
    public Double getThroughputRowsPerSecond() {
        if (rowsAffected == null || executionDurationMs == null || executionDurationMs == 0) {
            return null;
        }
        return (rowsAffected * 1000.0) / executionDurationMs;
    }

    /**
     * Checks if query had full table scan.
     */
    public boolean hadFullTableScan() {
        return sequentialScan != null && sequentialScan
                && (indexesUsed == null || indexesUsed.isEmpty());
    }

    /**
     * Checks if query had potential performance issues.
     */
    public boolean hasPerformanceIssues() {
        return isVerySlow()
                || hadFullTableScan()
                || (performanceTier == PerformanceTier.CRITICAL);
    }
}
