package com.gogidix.digitalmarketing.analytics.domain.model;

import com.gogidix.digitalmarketing.shared.domain.BaseEntity;
import lombok.AllArgsConstructor;
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
 * Channel Analytics - Channel performance comparison
 *
 * <p>Aggregates performance metrics by marketing channel.</p>
 *
 * <p>Channels include:</p>
 * <ul>
 *   <li>EMAIL - Email marketing</li>
 *   <li>SOCIAL - Social media platforms</li>
 *   <li>SEARCH - Search engine marketing</li>
 *   <li>DISPLAY - Display advertising</li>
 *   <li>DIRECT - Direct traffic</li>
 *   <li>REFERRAL - Referral traffic</li>
 *   <li>ORGANIC - Organic search</li>
 *   <li>PAID - Paid advertising</li>
 * </ul>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "channel_analytics")
@TypeAlias("channel_analytics")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "channel_analytics_tenant_idx", def = "{'tenantId': 1, 'channelType': 1, 'period': -1}")
@CompoundIndex(name = "channel_analytics_tenant_platform_idx", def = "{'tenantId': 1, 'channelType': 1, 'platform': 1, 'period': -1}")
public class ChannelAnalytics extends BaseEntity {

    /**
     * The channel type (EMAIL, SOCIAL, SEARCH, DISPLAY, DIRECT, REFERRAL, ORGANIC, PAID)
     */
    @Indexed
    private String channelType;

    /**
     * The specific platform (e.g., Google, Facebook, LinkedIn, Instagram)
     */
    @Indexed
    private String platform;

    /**
     * The period identifier (e.g., "2024-01", "Q1-2024")
     */
    @Indexed
    private String period;

    /**
     * Start of the period
     */
    @Indexed
    private Instant periodStart;

    /**
     * End of the period
     */
    private Instant periodEnd;

    /**
     * Total impressions
     */
    private BigDecimal impressions;

    /**
     * Total reach (unique users)
     */
    private BigDecimal reach;

    /**
     * Total clicks
     */
    private BigDecimal clicks;

    /**
     * Click-through rate
     */
    private BigDecimal ctr;

    /**
     * Total conversions
     */
    private BigDecimal conversions;

    /**
     * Conversion rate
     */
    private BigDecimal conversionRate;

    /**
     * Total spend/cost
     */
    private BigDecimal spend;

    /**
     * Cost per click
     */
    private BigDecimal cpc;

    /**
     * Cost per thousand impressions
     */
    private BigDecimal cpm;

    /**
     * Cost per acquisition
     */
    private BigDecimal cpa;

    /**
     * Total revenue generated
     */
    private BigDecimal revenue;

    /**
     * Return on investment
     */
    private BigDecimal roi;

    /**
     * Return on ad spend
     */
    private BigDecimal roas;

    /**
     * Engagement score (0-100)
     */
    private BigDecimal engagementScore;

    /**
     * Customer satisfaction score
     */
    private BigDecimal satisfactionScore;

    /**
     * Net Promoter Score
     */
    private BigDecimal npsScore;

    /**
     * Customer lifetime value
     */
    private BigDecimal customerLifetimeValue;

    /**
     * Retention rate
     */
    private BigDecimal retentionRate;

    /**
     * Churn rate
     */
    private BigDecimal churnRate;

    /**
     * Average session duration (in seconds)
     */
    private BigDecimal avgSessionDuration;

    /**
     * Bounce rate
     */
    private BigDecimal bounceRate;

    /**
     * Pages per session
     */
    private BigDecimal pagesPerSession;

    /**
     * New vs returning visitor ratio
     */
    private BigDecimal newVisitorRatio;

    /**
     * Mobile traffic percentage
     */
    private BigDecimal mobileTrafficPercentage;

    /**
     * Channel rank compared to other channels
     */
    private Integer channelRank;

    /**
     * Channel score (0-100) for comparison
     */
    private BigDecimal channelScore;

    /**
     * Share of total conversions
     */
    private BigDecimal conversionShare;

    /**
     * Share of total budget
     */
    private BigDecimal budgetShare;

    /**
     * Trend direction (UP, DOWN, STABLE)
     */
    private String trendDirection;

    /**
     * Previous period value for trend comparison
     */
    private BigDecimal previousPeriodValue;

    /**
     * Percentage change from previous period
     */
    private BigDecimal percentChange;

    /**
     * Channel performance rating (EXCELLENT, GOOD, AVERAGE, POOR)
     */
    private String performanceRating;

    /**
     * Additional metrics as key-value pairs
     */
    private Map<String, Object> additionalMetrics;

    /**
     * Audience demographics data
     */
    private Map<String, Object> demographicsData;

    /**
     * Geographic distribution data
     */
    private Map<String, Object> geographicData;

    /**
     * Last updated timestamp
     */
    private Instant lastUpdated;

    /**
     * Create a new channel analytics entry.
     *
     * @param tenantId   the tenant ID
     * @param channelType the channel type
     * @param platform   the platform
     * @param period     the period identifier
     */
    public ChannelAnalytics(String tenantId, String channelType, String platform, String period) {
        super(tenantId);
        this.channelType = channelType;
        this.platform = platform;
        this.period = period;
    }

    /**
     * Calculate all derived metrics.
     */
    public void calculateAllMetrics() {
        calculateCtr();
        calculateConversionRate();
        calculateCpc();
        calculateCpm();
        calculateCpa();
        calculateRoi();
        calculateRoas();
        calculatePercentChange();
        updateTrendDirection();
        calculateChannelScore();
        updatePerformanceRating();
    }

    /**
     * Calculate click-through rate.
     */
    public void calculateCtr() {
        if (impressions != null && impressions.compareTo(BigDecimal.ZERO) > 0 && clicks != null) {
            this.ctr = clicks.divide(impressions, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
    }

    /**
     * Calculate conversion rate.
     */
    public void calculateConversionRate() {
        if (clicks != null && clicks.compareTo(BigDecimal.ZERO) > 0 && conversions != null) {
            this.conversionRate = conversions.divide(clicks, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
    }

    /**
     * Calculate cost per click.
     */
    public void calculateCpc() {
        if (spend != null && clicks != null && clicks.compareTo(BigDecimal.ZERO) > 0) {
            this.cpc = spend.divide(clicks, 2, java.math.RoundingMode.HALF_UP);
        }
    }

    /**
     * Calculate cost per thousand impressions.
     */
    public void calculateCpm() {
        if (spend != null && impressions != null && impressions.compareTo(BigDecimal.ZERO) > 0) {
            this.cpm = spend.divide(impressions, 2, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("1000"));
        }
    }

    /**
     * Calculate cost per acquisition.
     */
    public void calculateCpa() {
        if (spend != null && conversions != null && conversions.compareTo(BigDecimal.ZERO) > 0) {
            this.cpa = spend.divide(conversions, 2, java.math.RoundingMode.HALF_UP);
        }
    }

    /**
     * Calculate ROI.
     */
    public void calculateRoi() {
        if (spend != null && spend.compareTo(BigDecimal.ZERO) > 0 && revenue != null) {
            BigDecimal profit = revenue.subtract(spend);
            this.roi = profit.divide(spend, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
    }

    /**
     * Calculate ROAS.
     */
    public void calculateRoas() {
        if (spend != null && spend.compareTo(BigDecimal.ZERO) > 0 && revenue != null) {
            this.roas = revenue.divide(spend, 2, java.math.RoundingMode.HALF_UP);
        }
    }

    /**
     * Calculate percentage change from previous period.
     */
    public void calculatePercentChange() {
        if (previousPeriodValue != null && previousPeriodValue.compareTo(BigDecimal.ZERO) != 0 && revenue != null) {
            BigDecimal change = revenue.subtract(previousPeriodValue);
            this.percentChange = change.divide(previousPeriodValue, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
    }

    /**
     * Update trend direction based on percent change.
     */
    public void updateTrendDirection() {
        if (percentChange != null) {
            if (percentChange.compareTo(new BigDecimal("5")) > 0) {
                this.trendDirection = "UP";
            } else if (percentChange.compareTo(new BigDecimal("-5")) < 0) {
                this.trendDirection = "DOWN";
            } else {
                this.trendDirection = "STABLE";
            }
        }
    }

    /**
     * Calculate channel score based on multiple factors.
     */
    public void calculateChannelScore() {
        BigDecimal score = BigDecimal.ZERO;
        int factors = 0;

        // ROI factor (weight: 30%)
        if (roi != null) {
            BigDecimal roiScore = roi.compareTo(new BigDecimal("100")) > 0 ?
                new BigDecimal("30") : roi.divide(new BigDecimal("100").multiply(new BigDecimal("30")), 2, java.math.RoundingMode.HALF_UP);
            score = score.add(roiScore);
            factors++;
        }

        // Conversion rate factor (weight: 25%)
        if (conversionRate != null) {
            BigDecimal convScore = conversionRate.compareTo(new BigDecimal("5")) > 0 ?
                new BigDecimal("25") : conversionRate.divide(new BigDecimal("5"), 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("25"));
            score = score.add(convScore);
            factors++;
        }

        // Engagement score factor (weight: 20%)
        if (engagementScore != null) {
            score = score.add(engagementScore.divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("20")));
            factors++;
        }

        // ROAS factor (weight: 15%)
        if (roas != null) {
            BigDecimal roasScore = roas.compareTo(new BigDecimal("3")) > 0 ?
                new BigDecimal("15") : roas.divide(new BigDecimal("3"), 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("15"));
            score = score.add(roasScore);
            factors++;
        }

        // Satisfaction score factor (weight: 10%)
        if (satisfactionScore != null) {
            score = score.add(satisfactionScore.divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("10")));
            factors++;
        }

        this.channelScore = factors > 0 ? score : null;
    }

    /**
     * Update performance rating based on channel score.
     */
    public void updatePerformanceRating() {
        if (channelScore != null) {
            if (channelScore.compareTo(new BigDecimal("80")) >= 0) {
                this.performanceRating = "EXCELLENT";
            } else if (channelScore.compareTo(new BigDecimal("60")) >= 0) {
                this.performanceRating = "GOOD";
            } else if (channelScore.compareTo(new BigDecimal("40")) >= 0) {
                this.performanceRating = "AVERAGE";
            } else {
                this.performanceRating = "POOR";
            }
        }
    }

    /**
     * Check if channel is profitable (ROI > 0).
     *
     * @return true if ROI is positive
     */
    public boolean isProfitable() {
        return roi != null && roi.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isNotProfitable() {
        return roi != null && roi.compareTo(BigDecimal.ZERO) <= 0;
    }

    /**
     * Check if channel is trending up.
     *
     * @return true if trend is UP
     */
    public boolean isTrendingUp() {
        return "UP".equals(this.trendDirection);
    }

    /**
     * Add additional metric.
     *
     * @param key   the metric key
     * @param value the metric value
     */
    public void addAdditionalMetric(String key, Object value) {
        if (this.additionalMetrics == null) {
            this.additionalMetrics = new HashMap<>();
        }
        this.additionalMetrics.put(key, value);
    }

    /**
     * Add demographic data.
     *
     * @param segment the segment name
     * @param data    the segment data
     */
    public void addDemographicsData(String segment, Object data) {
        if (this.demographicsData == null) {
            this.demographicsData = new HashMap<>();
        }
        this.demographicsData.put(segment, data);
    }

    /**
     * Update the analytics timestamp.
     */
    public void markUpdated() {
        this.lastUpdated = Instant.now();
        this.touch();
    }
}
