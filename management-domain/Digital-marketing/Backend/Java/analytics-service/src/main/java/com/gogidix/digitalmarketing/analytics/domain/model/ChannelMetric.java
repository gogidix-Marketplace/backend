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
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ChannelMetric - Channel-specific performance metrics
 *
 * <p>This entity stores aggregated performance metrics for marketing channels.
 * It enables comparison of different channel effectiveness and ROI.</p>
 *
 * <p>Channel Types:</p>
 * <ul>
 *   <li>EMAIL - Email marketing campaigns</li>
 *   <li>SOCIAL - Social media platforms (Facebook, Instagram, LinkedIn, Twitter)</li>
 *   <li>SEARCH - Search engine marketing (Google Ads, Bing Ads)</li>
 *   <li>DISPLAY - Display advertising</li>
 *   <li>DIRECT - Direct traffic</li>
 *   <li>REFERRAL - Referral traffic</li>
 *   <li>ORGANIC - Organic search traffic</li>
 *   <li>PAID - Paid advertising</li>
 *   <li>AFFILIATE - Affiliate marketing</li>
 *   <li>INFLUENCER - Influencer marketing</li>
 * </ul>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "channel_metrics")
@TypeAlias("channel_metric")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "channel_metric_tenant_channel_period_idx", def = "{'tenantId': 1, 'channelType': 1, 'periodStart': -1}")
@CompoundIndex(name = "channel_metric_tenant_platform_period_idx", def = "{'tenantId': 1, 'channelType': 1, 'platform': 1, 'periodStart': -1}")
@CompoundIndex(name = "channel_metric_tenant_date_idx", def = "{'tenantId': 1, 'periodStart': -1}")
public class ChannelMetric extends BaseEntity {

    /**
     * The channel type
     */
    @Indexed
    private String channelType;

    /**
     * The specific platform (e.g., Google, Facebook, LinkedIn, Instagram)
     */
    @Indexed
    private String platform;

    /**
     * The period identifier (e.g., "2024-01", "Q1-2024", "W01-2024")
     */
    @Indexed
    private String period;

    /**
     * The period granularity (HOURLY, DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY)
     */
    @Indexed
    private String periodGranularity;

    /**
     * Start of the period
     */
    @Indexed
    private Instant periodStart;

    /**
     * End of the period
     */
    private Instant periodEnd;

    // === Reach Metrics ===

    /**
     * Total impressions
     */
    private BigDecimal impressions;

    /**
     * Unique reach
     */
    private BigDecimal reach;

    /**
     * Share of voice relative to competitors
     */
    private BigDecimal shareOfVoice;

    // === Engagement Metrics ===

    /**
     * Total clicks
     */
    private BigDecimal clicks;

    /**
     * Click-through rate
     */
    private BigDecimal ctr;

    /**
     * Total engagements
     */
    private BigDecimal engagements;

    /**
     * Engagement rate
     */
    private BigDecimal engagementRate;

    /**
     * Average position (for search)
     */
    private BigDecimal avgPosition;

    /**
     * Bounce rate
     */
    private BigDecimal bounceRate;

    /**
     * Average session duration (seconds)
     */
    private BigDecimal avgSessionDuration;

    /**
     * Pages per session
     */
    private BigDecimal pagesPerSession;

    // === Conversion Metrics ===

    /**
     * Total conversions
     */
    private BigDecimal conversions;

    /**
     * Conversion rate
     */
    private BigDecimal conversionRate;

    /**
     * Lead quality score (0-100)
     */
    private BigDecimal leadQualityScore;

    /**
     * Sales qualified leads
     */
    private BigDecimal salesQualifiedLeads;

    // === Cost Metrics ===

    /**
     * Total spend/cost
     */
    private BigDecimal spend;

    /**
     * Cost per click
     */
    private BigDecimal costPerClick;

    /**
     * Cost per thousand impressions
     */
    private BigDecimal costPerThousand;

    /**
     * Cost per acquisition
     */
    private BigDecimal costPerAcquisition;

    /**
     * Cost per lead
     */
    private BigDecimal costPerLead;

    // === Revenue Metrics ===

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
     * Average order value
     */
    private BigDecimal averageOrderValue;

    // === Customer Metrics ===

    /**
     * New customers acquired
     */
    private BigDecimal newCustomers;

    /**
     * Customer acquisition cost
     */
    private BigDecimal customerAcquisitionCost;

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

    // === Sentiment Metrics (for social channels) ===

    /**
     * Net Promoter Score
     */
    private BigDecimal npsScore;

    /**
     * Customer satisfaction score
     */
    private BigDecimal satisfactionScore;

    /**
     * Sentiment score (-1 to +1)
     */
    private BigDecimal sentimentScore;

    // === Trend Metrics ===

    /**
     * Previous period revenue for comparison
     */
    private BigDecimal previousPeriodRevenue;

    /**
     * Percentage change from previous period
     */
    private BigDecimal percentChange;

    /**
     * Trend direction (UP, DOWN, STABLE)
     */
    private String trendDirection;

    // === Performance Metrics ===

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
     * Performance rating (EXCELLENT, GOOD, AVERAGE, POOR)
     */
    private String performanceRating;

    // === Additional Data ===

    /**
     * Audience demographics data
     */
    private Map<String, Object> demographicsData;

    /**
     * Geographic distribution data
     */
    private Map<String, Object> geographicData;

    /**
     * Device breakdown data
     */
    private Map<String, Object> deviceBreakdown;

    /**
     * Time-of-day performance data
     */
    private Map<String, Object> timeOfDayData;

    /**
     * Day-of-week performance data
     */
    private Map<String, Object> dayOfWeekData;

    /**
     * Additional metrics
     */
    private Map<String, Object> additionalMetrics;

    /**
     * Last updated timestamp
     */
    private Instant lastUpdatedAt;

    /**
     * Create a new channel metric.
     *
     * @param tenantId        the tenant ID
     * @param channelType     the channel type
     * @param platform        the platform
     * @param period          the period identifier
     * @param periodStart     the period start
     */
    public ChannelMetric(String tenantId, String channelType, String platform, String period, Instant periodStart) {
        super(tenantId);
        this.channelType = Objects.requireNonNull(channelType, "Channel type is required");
        this.platform = platform;
        this.period = period;
        this.periodStart = Objects.requireNonNull(periodStart, "Period start is required");
    }

    /**
     * Create a daily channel metric.
     *
     * @param tenantId    the tenant ID
     * @param channelType the channel type
     * @param platform    the platform
     * @param date        the date
     * @return a new ChannelMetric for the daily period
     */
    public static ChannelMetric daily(String tenantId, String channelType, String platform, LocalDate date) {
        Instant start = date.atStartOfDay(java.time.ZoneOffset.UTC).toInstant();
        Instant end = date.plusDays(1).atStartOfDay(java.time.ZoneOffset.UTC).toInstant();
        String period = date.toString();
        return new ChannelMetric(tenantId, channelType, platform, period, start);
    }

    /**
     * Create a monthly channel metric.
     *
     * @param tenantId    the tenant ID
     * @param channelType the channel type
     * @param platform    the platform
     * @param yearMonth   the year-month
     * @return a new ChannelMetric for the monthly period
     */
    public static ChannelMetric monthly(String tenantId, String channelType, String platform, YearMonth yearMonth) {
        Instant start = yearMonth.atDay(1).atStartOfDay(java.time.ZoneOffset.UTC).toInstant();
        Instant end = yearMonth.plusMonths(1).atDay(1).atStartOfDay(java.time.ZoneOffset.UTC).toInstant();
        String period = yearMonth.toString();
        ChannelMetric metric = new ChannelMetric(tenantId, channelType, platform, period, start);
        metric.setPeriodEnd(end);
        metric.setPeriodGranularity("MONTHLY");
        return metric;
    }

    /**
     * Calculate all derived metrics.
     */
    public void calculateAllMetrics() {
        calculateCtr();
        calculateConversionRate();
        calculateCostPerClick();
        calculateCostPerThousand();
        calculateCostPerAcquisition();
        calculateCostPerLead();
        calculateRoi();
        calculateRoas();
        calculateAverageOrderValue();
        calculateCustomerAcquisitionCost();
        calculatePercentChange();
        updateTrendDirection();
        calculateChannelScore();
        updatePerformanceRating();
        markAsUpdated();
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
     * Calculate engagement rate.
     */
    public void calculateEngagementRate() {
        if (impressions != null && impressions.compareTo(BigDecimal.ZERO) > 0 && engagements != null) {
            this.engagementRate = engagements.divide(impressions, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
    }

    /**
     * Calculate cost per click.
     */
    public void calculateCostPerClick() {
        if (spend != null && clicks != null && clicks.compareTo(BigDecimal.ZERO) > 0) {
            this.costPerClick = spend.divide(clicks, 2, java.math.RoundingMode.HALF_UP);
        }
    }

    /**
     * Calculate cost per thousand impressions.
     */
    public void calculateCostPerThousand() {
        if (spend != null && impressions != null && impressions.compareTo(BigDecimal.ZERO) > 0) {
            this.costPerThousand = spend.divide(impressions, 2, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("1000"));
        }
    }

    /**
     * Calculate cost per acquisition.
     */
    public void calculateCostPerAcquisition() {
        if (spend != null && conversions != null && conversions.compareTo(BigDecimal.ZERO) > 0) {
            this.costPerAcquisition = spend.divide(conversions, 2, java.math.RoundingMode.HALF_UP);
        }
    }

    /**
     * Calculate cost per lead.
     */
    public void calculateCostPerLead() {
        if (spend != null && salesQualifiedLeads != null && salesQualifiedLeads.compareTo(BigDecimal.ZERO) > 0) {
            this.costPerLead = spend.divide(salesQualifiedLeads, 2, java.math.RoundingMode.HALF_UP);
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
     * Calculate average order value.
     */
    public void calculateAverageOrderValue() {
        if (revenue != null && conversions != null && conversions.compareTo(BigDecimal.ZERO) > 0) {
            this.averageOrderValue = revenue.divide(conversions, 2, java.math.RoundingMode.HALF_UP);
        }
    }

    /**
     * Calculate customer acquisition cost.
     */
    public void calculateCustomerAcquisitionCost() {
        if (spend != null && newCustomers != null && newCustomers.compareTo(BigDecimal.ZERO) > 0) {
            this.customerAcquisitionCost = spend.divide(newCustomers, 2, java.math.RoundingMode.HALF_UP);
        }
    }

    /**
     * Calculate percentage change from previous period.
     */
    public void calculatePercentChange() {
        if (previousPeriodRevenue != null && previousPeriodRevenue.compareTo(BigDecimal.ZERO) != 0 && revenue != null) {
            BigDecimal change = revenue.subtract(previousPeriodRevenue);
            this.percentChange = change.divide(previousPeriodRevenue, 4, java.math.RoundingMode.HALF_UP)
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
     * Score ranges from 0 to 100.
     */
    public void calculateChannelScore() {
        BigDecimal score = BigDecimal.ZERO;
        int factors = 0;

        // ROI factor (weight: 30%)
        if (roi != null) {
            BigDecimal roiScore = roi.compareTo(new BigDecimal("100")) > 0
                ? new BigDecimal("30")
                : roi.divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("30"));
            score = score.add(roiScore.min(new BigDecimal("30")));
            factors++;
        }

        // Conversion rate factor (weight: 25%)
        if (conversionRate != null) {
            BigDecimal convScore = conversionRate.compareTo(new BigDecimal("5")) > 0
                ? new BigDecimal("25")
                : conversionRate.divide(new BigDecimal("5"), 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("25"));
            score = score.add(convScore.min(new BigDecimal("25")));
            factors++;
        }

        // ROAS factor (weight: 20%)
        if (roas != null) {
            BigDecimal roasScore = roas.compareTo(new BigDecimal("3")) > 0
                ? new BigDecimal("20")
                : roas.divide(new BigDecimal("3"), 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("20"));
            score = score.add(roasScore.min(new BigDecimal("20")));
            factors++;
        }

        // Engagement rate factor (weight: 15%)
        if (engagementRate != null) {
            BigDecimal engScore = engagementRate.compareTo(new BigDecimal("10")) > 0
                ? new BigDecimal("15")
                : engagementRate.divide(new BigDecimal("10"), 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("15"));
            score = score.add(engScore.min(new BigDecimal("15")));
            factors++;
        }

        // CTR factor (weight: 10%)
        if (ctr != null) {
            BigDecimal ctrScore = ctr.compareTo(new BigDecimal("5")) > 0
                ? new BigDecimal("10")
                : ctr.divide(new BigDecimal("5"), 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("10"));
            score = score.add(ctrScore.min(new BigDecimal("10")));
            factors++;
        }

        this.channelScore = factors > 0 ? score : BigDecimal.ZERO;
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

    /**
     * Check if channel is trending up.
     *
     * @return true if trend is UP
     */
    public boolean isTrendingUp() {
        return "UP".equals(this.trendDirection);
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
     * Add geographic data.
     *
     * @param location the location name
     * @param data     the location data
     */
    public void addGeographicData(String location, Object data) {
        if (this.geographicData == null) {
            this.geographicData = new HashMap<>();
        }
        this.geographicData.put(location, data);
    }

    /**
     * Add device breakdown data.
     *
     * @param device the device type
     * @param data   the device data
     */
    public void addDeviceBreakdown(String device, Object data) {
        if (this.deviceBreakdown == null) {
            this.deviceBreakdown = new HashMap<>();
        }
        this.deviceBreakdown.put(device, data);
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
     * Mark metrics as updated.
     */
    public void markAsUpdated() {
        this.lastUpdatedAt = Instant.now();
        this.touch();
    }

    /**
     * Get efficiency score (normalized 0-100).
     *
     * @return efficiency score
     */
    public BigDecimal getEfficiencyScore() {
        return channelScore != null ? channelScore : BigDecimal.ZERO;
    }

    /**
     * Get cost efficiency ratio (revenue per dollar spent).
     *
     * @return cost efficiency ratio
     */
    public BigDecimal getCostEfficiencyRatio() {
        if (spend != null && spend.compareTo(BigDecimal.ZERO) > 0 && revenue != null) {
            return revenue.divide(spend, 2, java.math.RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Get lead quality score (0-100).
     *
     * @return lead quality score
     */
    public BigDecimal getLeadQualityScore() {
        if (leadQualityScore != null) {
            return leadQualityScore;
        }
        // Calculate from conversion rate and satisfaction score
        if (conversionRate != null && satisfactionScore != null) {
            return conversionRate.add(satisfactionScore).divide(new BigDecimal("2"), 2, java.math.RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
