package com.gogidix.monitoring.monitoringdataservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Domain model representing aggregated metrics over a time window.
 * Used for storing pre-aggregated data for faster query performance.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricAggregation {

    /**
     * Unique identifier for this aggregation.
     */
    private String id;

    /**
     * Tenant identifier.
     */
    private String tenantId;

    /**
     * Service name.
     */
    private String serviceName;

    /**
     * Service type.
     */
    private MetricDataPoint.ServiceType serviceType;

    /**
     * Metric name.
     */
    private String metricName;

    /**
     * Aggregation window type.
     */
    private AggregationWindow window;

    /**
     * Start of the aggregation window.
     */
    private Instant windowStart;

    /**
     * End of the aggregation window.
     */
    private Instant windowEnd;

    /**
     * Number of data points in this aggregation.
     */
    private long count;

    /**
     * Minimum value in the window.
     */
    private Double min;

    /**
     * Maximum value in the window.
     */
    private Double max;

    /**
     * Average value in the window.
     */
    private Double avg;

    /**
     * Sum of all values in the window.
     */
    private Double sum;

    /**
     * 50th percentile (median).
     */
    private Double p50;

    /**
     * 95th percentile.
     */
    private Double p95;

    /**
     * 99th percentile.
     */
    private Double p99;

    /**
     * Standard deviation.
     */
    private Double stdDev;

    /**
     * Additional tags.
     */
    private Map<String, String> tags;

    /**
     * Timestamp when aggregation was computed.
     */
    private Instant computedAt;

    /**
     * Aggregation window types.
     */
    public enum AggregationWindow {
        MINUTE(60),
        FIVE_MINUTES(300),
        FIFTEEN_MINUTES(900),
        HOUR(3600),
        SIX_HOURS(21600),
        DAY(86400),
        WEEK(604800);

        private final int seconds;

        AggregationWindow(int seconds) {
            this.seconds = seconds;
        }

        public int getSeconds() {
            return seconds;
        }
    }

    /**
     * Validates this aggregation.
     */
    public boolean isValid() {
        return tenantId != null && !tenantId.isBlank()
                && serviceName != null && !serviceName.isBlank()
                && metricName != null && !metricName.isBlank()
                && window != null
                && windowStart != null
                && windowEnd != null
                && count > 0;
    }

    /**
     * Gets the duration of this aggregation window in seconds.
     */
    public long getDurationSeconds() {
        return windowEnd.getEpochSecond() - windowStart.getEpochSecond();
    }
}
