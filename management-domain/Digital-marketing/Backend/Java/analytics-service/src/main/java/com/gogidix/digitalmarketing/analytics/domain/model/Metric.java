package com.gogidix.digitalmarketing.analytics.domain.model;

import com.gogidix.digitalmarketing.shared.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Metric - Time-series metric storage for marketing analytics
 *
 * <p>This entity stores individual metric data points with timestamps for time-series analysis.
 * Each metric represents a single measurement at a specific point in time.</p>
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Time-series data with configurable granularity</li>
 *   <li>Flexible tagging for filtering and grouping</li>
 *   <li>Data quality tracking</li>
 *   <li>Multi-dimensional aggregation support</li>
 * </ul>
 *
 * <p>Use Cases:</p>
 * <ul>
 *   <li>Daily/hourly metric tracking</li>
 *   <li>Trend analysis over time</li>
 *   <li>Dashboard data points</li>
 *   <li>Performance monitoring</li>
 * </ul>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "metrics")
@TypeAlias("metric")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "metric_tenant_type_timestamp_idx", def = "{'tenantId': 1, 'metricType': 1, 'timestamp': -1}")
@CompoundIndex(name = "metric_tenant_tags_timestamp_idx", def = "{'tenantId': 1, 'tags.source': 1, 'timestamp': -1}")
@CompoundIndex(name = "metric_tenant_campaign_timestamp_idx", def = "{'tenantId': 1, 'campaignId': 1, 'timestamp': -1}")
@CompoundIndex(name = "metric_tenant_channel_timestamp_idx", def = "{'tenantId': 1, 'channelId': 1, 'timestamp': -1}")
public class Metric extends BaseEntity {

    /**
     * The metric name/identifier (e.g., "page_views", "conversions", "revenue")
     */
    @Indexed
    private String name;

    /**
     * The metric value
     */
    private BigDecimal value;

    /**
     * The previous value for trend comparison
     */
    private BigDecimal previousValue;

    /**
     * The timestamp when this metric was recorded
     */
    @Indexed
    private Instant timestamp;

    /**
     * The data granularity (HOURLY, DAILY, WEEKLY, MONTHLY, RAW)
     */
    @Indexed
    private String granularity;

    /**
     * The data type of the value (COUNTER, GAUGE, PERCENTAGE, CURRENCY)
     */
    private String dataType;

    /**
     * The unit of measurement (e.g., "USD", "count", "seconds", "percentage")
     */
    private String unit;

    /**
     * Associated campaign ID (optional)
     */
    @Indexed
    private String campaignId;

    /**
     * Associated channel ID (optional)
     */
    @Indexed
    private String channelId;

    /**
     * The source of this metric (Google Analytics, CRM, Facebook, etc.)
     */
    @Indexed
    private String source;

    /**
     * Key-value pairs for flexible tagging and filtering
     * Examples: {"country": "US", "device": "mobile", "browser": "chrome"}
     */
    private Map<String, String> tags;

    /**
     * Data quality score (0.0 to 1.0)
     */
    private BigDecimal qualityScore;

    /**
     * Whether this metric has been verified
     */
    @Builder.Default
    private Boolean verified = false;

    /**
     * The currency code for financial metrics (USD, EUR, GBP, etc.)
     */
    private String currency;

    /**
     * Target value for this metric (for performance tracking)
     */
    private BigDecimal targetValue;

    /**
     * Status relative to target (ON_TRACK, BELOW_TARGET, ABOVE_TARGET, NO_TARGET)
     */
    private String targetStatus;

    /**
     * Confidence interval lower bound
     */
    private BigDecimal confidenceLower;

    /**
     * Confidence interval upper bound
     */
    private BigDecimal confidenceUpper;

    /**
     * Sample size (for statistical metrics)
     */
    private Long sampleSize;

    /**
     * Standard deviation (for statistical analysis)
     */
    private BigDecimal standardDeviation;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Create a new metric with basic information.
     *
     * @param tenantId   the tenant ID
     * @param name       the metric name
     * @param value      the metric value
     * @param timestamp  the timestamp
     */
    public Metric(String tenantId, String name, BigDecimal value, Instant timestamp) {
        super(tenantId);
        this.name = Objects.requireNonNull(name, "Metric name is required");
        this.value = Objects.requireNonNull(value, "Metric value is required");
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp is required");
        this.granularity = "RAW";
        this.dataType = "GAUGE";
        this.verified = false;
    }

    /**
     * Create a new metric with granularity.
     *
     * @param tenantId   the tenant ID
     * @param name       the metric name
     * @param value      the metric value
     * @param timestamp  the timestamp
     * @param granularity the data granularity
     */
    public Metric(String tenantId, String name, BigDecimal value, Instant timestamp, String granularity) {
        this(tenantId, name, value, timestamp);
        this.granularity = granularity;
    }

    /**
     * Calculate percentage change from previous value.
     */
    public void calculatePercentChange() {
        if (previousValue != null && previousValue.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal change = value.subtract(previousValue);
            BigDecimal percentChange = change.divide(previousValue, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            addMetadata("percentChange", percentChange);
        }
    }

    /**
     * Update target status based on value vs target.
     */
    public void updateTargetStatus() {
        if (targetValue != null && targetValue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ratio = value.divide(targetValue, 2, java.math.RoundingMode.HALF_UP);

            if (ratio.compareTo(new BigDecimal("1.0")) >= 0) {
                this.targetStatus = "ABOVE_TARGET";
            } else if (ratio.compareTo(new BigDecimal("0.8")) >= 0) {
                this.targetStatus = "ON_TRACK";
            } else {
                this.targetStatus = "BELOW_TARGET";
            }
        } else {
            this.targetStatus = "NO_TARGET";
        }
    }

    /**
     * Check if this metric is on track.
     *
     * @return true if status is ON_TRACK or ABOVE_TARGET
     */
    public boolean isOnTrack() {
        return "ON_TRACK".equals(this.targetStatus) || "ABOVE_TARGET".equals(this.targetStatus);
    }

    /**
     * Check if this metric is below target.
     *
     * @return true if status is BELOW_TARGET
     */
    public boolean isBelowTarget() {
        return "BELOW_TARGET".equals(this.targetStatus);
    }

    /**
     * Add a tag to this metric.
     *
     * @param key   the tag key
     * @param value the tag value
     */
    public void addTag(String key, String value) {
        if (this.tags == null) {
            this.tags = new HashMap<>();
        }
        this.tags.put(key, value);
    }

    /**
     * Get a tag value.
     *
     * @param key the tag key
     * @return the tag value, or null if not set
     */
    public String getTag(String key) {
        if (this.tags == null) {
            return null;
        }
        return this.tags.get(key);
    }

    /**
     * Add metadata to this metric.
     *
     * @param key   the metadata key
     * @param value the metadata value
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Get a metadata value.
     *
     * @param key the metadata key
     * @return the metadata value, or null if not set
     */
    public Object getMetadata(String key) {
        if (this.metadata == null) {
            return null;
        }
        return this.metadata.get(key);
    }

    /**
     * Set the data quality score.
     *
     * @param score the quality score (0.0 to 1.0)
     * @throws IllegalArgumentException if score is out of range
     */
    public void setQualityScore(BigDecimal score) {
        if (score != null) {
            if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(BigDecimal.ONE) > 0) {
                throw new IllegalArgumentException("Quality score must be between 0.0 and 1.0");
            }
        }
        this.qualityScore = score;
    }

    /**
     * Check if data quality is acceptable.
     *
     * @param minScore the minimum acceptable score
     * @return true if quality score is above minimum
     */
    public boolean hasAcceptableQuality(BigDecimal minScore) {
        return this.qualityScore != null && this.qualityScore.compareTo(minScore) >= 0;
    }

    /**
     * Check if this is a financial metric.
     *
     * @return true if currency is set
     */
    public boolean isFinancialMetric() {
        return this.currency != null && !this.currency.isBlank();
    }

    /**
     * Check if this is a percentage metric.
     *
     * @return true if data type is PERCENTAGE
     */
    public boolean isPercentageMetric() {
        return "PERCENTAGE".equals(this.dataType);
    }

    /**
     * Check if this is a counter metric (cumulative).
     *
     * @return true if data type is COUNTER
     */
    public boolean isCounterMetric() {
        return "COUNTER".equals(this.dataType);
    }

    /**
     * Get the age of this metric in milliseconds.
     *
     * @return age in milliseconds
     */
    public long getAgeMillis() {
        return Instant.now().toEpochMilli() - this.timestamp.toEpochMilli();
    }

    /**
     * Check if this metric is stale (older than specified duration).
     *
     * @param maxAgeMillis maximum age in milliseconds
     * @return true if metric is stale
     */
    public boolean isStale(long maxAgeMillis) {
        return getAgeMillis() > maxAgeMillis;
    }

    /**
     * Create a builder pre-configured with current timestamp.
     *
     * @return a builder with timestamp set to now
     */
    public static MetricBuilder builderWithNow() {
        return Metric.builder().timestamp(Instant.now());
    }

    /**
     * Create a copy of this metric with a new value.
     *
     * @param newValue the new value
     * @return a new Metric instance
     */
    public Metric withValue(BigDecimal newValue) {
        Metric copy = Metric.builder()
                .name(this.name)
                .value(newValue)
                .previousValue(this.value)
                .timestamp(Instant.now())
                .granularity(this.granularity)
                .dataType(this.dataType)
                .unit(this.unit)
                .campaignId(this.campaignId)
                .channelId(this.channelId)
                .source(this.source)
                .tags(this.tags != null ? new HashMap<>(this.tags) : null)
                .currency(this.currency)
                .targetValue(this.targetValue)
                .build();
        copy.setTenantId(this.tenantId);
        copy.calculatePercentChange();
        copy.updateTargetStatus();
        return copy;
    }
}
