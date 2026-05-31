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

/**
 * Marketing Metric - Key marketing performance metrics
 *
 * <p>Tracks key metrics such as impressions, clicks, conversions, and engagement.</p>
 *
 * <p>Examples:</p>
 * <ul>
 *   <li>Website traffic (impressions, sessions)</li>
 *   <li>Engagement metrics (clicks, time on site)</li>
 *   <li>Conversion metrics (leads, sales)</li>
 *   <li>Social media metrics (followers, shares)</li>
 * </ul>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "marketing_metrics")
@TypeAlias("marketing_metric")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "metric_tenant_campaign_idx", def = "{'tenantId': 1, 'campaignId': 1, 'metricType': 1, 'timestamp': -1}")
@CompoundIndex(name = "metric_tenant_channel_idx", def = "{'tenantId': 1, 'channelType': 1, 'metricType': 1, 'timestamp': -1}")
@CompoundIndex(name = "metric_tenant_date_idx", def = "{'tenantId': 1, 'metricType': 1, 'timestamp': -1}")
public class MarketingMetric extends BaseEntity {

    /**
     * The type of metric (IMPRESSIONS, CLICKS, CONVERSIONS, LEADS, SALES, etc.)
     */
    @Indexed
    private String metricType;

    /**
     * The channel type (EMAIL, SOCIAL, SEARCH, DISPLAY, DIRECT, etc.)
     */
    @Indexed
    private String channelType;

    /**
     * Associated campaign ID (optional)
     */
    @Indexed
    private String campaignId;

    /**
     * The numeric value of the metric
     */
    private BigDecimal value;

    /**
     * The previous value for comparison
     */
    private BigDecimal previousValue;

    /**
     * Percentage change from previous period
     */
    private BigDecimal percentChange;

    /**
     * The timestamp when the metric was recorded
     */
    @Indexed
    private Instant timestamp;

    /**
     * The granularity of the metric (HOURLY, DAILY, WEEKLY, MONTHLY)
     */
    @Indexed
    private String granularity;

    /**
     * The source of the metric data (Google Analytics, CRM, etc.)
     */
    private String dataSource;

    /**
     * Additional metadata as key-value pairs
     */
    private Map<String, Object> metadata;

    /**
     * Quality score of the data (0.0 to 1.0)
     */
    private BigDecimal qualityScore;

    /**
     * Whether this metric has been verified
     */
    @Builder.Default
    private Boolean verified = false;

    /**
     * The currency if applicable (for revenue metrics)
     */
    private String currency;

    /**
     * The unit of measurement (count, percentage, currency, etc.)
     */
    private String unit;

    /**
     * Target value for this metric
     */
    private BigDecimal targetValue;

    /**
     * Status relative to target (ON_TRACK, BELOW_TARGET, ABOVE_TARGET)
     */
    private String targetStatus;

    /**
     * Create a new marketing metric for a tenant.
     *
     * @param tenantId   the tenant ID
     * @param metricType the metric type
     * @param channelType the channel type
     * @param value      the metric value
     */
    public MarketingMetric(String tenantId, String metricType, String channelType, BigDecimal value) {
        super(tenantId);
        this.metricType = metricType;
        this.channelType = channelType;
        this.value = value;
        this.timestamp = Instant.now();
        this.unit = "count";
    }

    /**
     * Create a new marketing metric with campaign association.
     *
     * @param tenantId   the tenant ID
     * @param metricType the metric type
     * @param channelType the channel type
     * @param campaignId the campaign ID
     * @param value      the metric value
     */
    public MarketingMetric(String tenantId, String metricType, String channelType, String campaignId, BigDecimal value) {
        super(tenantId);
        this.metricType = metricType;
        this.channelType = channelType;
        this.campaignId = campaignId;
        this.value = value;
        this.timestamp = Instant.now();
        this.unit = "count";
    }

    /**
     * Calculate percent change from previous value.
     */
    public void calculatePercentChange() {
        if (previousValue != null && previousValue.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal change = value.subtract(previousValue);
            this.percentChange = change.divide(previousValue, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        } else {
            this.percentChange = null;
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
     */
    public void setQualityScore(BigDecimal score) {
        if (score != null && (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(BigDecimal.ONE) > 0)) {
            throw new IllegalArgumentException("Quality score must be between 0.0 and 1.0");
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
}
