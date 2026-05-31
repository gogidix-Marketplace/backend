package com.gogidix.marketing.campaign.domain.model;

import com.gogidix.marketing.campaign.shared.domain.BaseEntity;
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
 * CampaignMetrics - Performance metrics tracking for campaigns
 *
 * <p>Stores aggregated performance metrics for campaigns
 * with support for time-series data and channel breakdowns.</p>
 */
@Document(collection = "campaign_metrics")
@TypeAlias("campaign_metrics")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "metrics_tenant_campaign_date_idx", def = "{'tenantId': 1, 'campaignId': 1, 'metricDate': -1}")
@CompoundIndex(name = "metrics_tenant_channel_date_idx", def = "{'tenantId': 1, 'channelId': 1, 'metricDate': -1}")
public class CampaignMetrics extends BaseEntity {

    /**
     * Campaign ID these metrics belong to
     */
    @Indexed
    private String campaignId;

    /**
     * Channel ID for channel-specific metrics (optional)
     */
    @Indexed
    private String channelId;

    /**
     * Metric date (for time-series tracking)
     */
    @Indexed
    private Instant metricDate;

    /**
     * Impressions (number of times content was displayed)
     */
    @Builder.Default
    private Long impressions = 0L;

    /**
     * Reach (unique people who saw the content)
     */
    @Builder.Default
    private Long reach = 0L;

    /**
     * Clicks (number of clicks on content)
     */
    @Builder.Default
    private Long clicks = 0L;

    /**
     * Conversions (desired actions taken)
     */
    @Builder.Default
    private Long conversions = 0L;

    /**
     * Spend (amount spent for this period)
     */
    @Builder.Default
    private BigDecimal spend = BigDecimal.ZERO;

    /**
     * Revenue generated
     */
    @Builder.Default
    private BigDecimal revenue = BigDecimal.ZERO;

    /**
     * Cost per click (CPC)
     */
    private BigDecimal costPerClick;

    /**
     * Cost per impression (CPM)
     */
    private BigDecimal costPerImpression;

    /**
     * Cost per acquisition/conversion (CPA)
     */
    private BigDecimal costPerAcquisition;

    /**
     * Click-through rate (CTR)
     */
    private BigDecimal clickThroughRate;

    /**
     * Conversion rate
     */
    private BigDecimal conversionRate;

    /**
     * Return on ad spend (ROAS)
     */
    private BigDecimal returnOnAdSpend;

    /**
     * Return on investment (ROI)
     */
    private BigDecimal returnOnInvestment;

    /**
     * Engagement rate
     */
    private BigDecimal engagementRate;

    /**
     * Bounce rate
     */
    private BigDecimal bounceRate;

    /**
     * Average time spent (in seconds)
     */
    private BigDecimal averageTimeSpent;

    /**
     * Number of new leads generated
     */
    @Builder.Default
    private Long leads = 0L;

    /**
     * Number of qualified leads
     */
    @Builder.Default
    private Long qualifiedLeads = 0L;

    /**
     * Number of sales
     */
    @Builder.Default
    private Long sales = 0L;

    /**
     * Average order value
     */
    private BigDecimal averageOrderValue;

    /**
     * Lifetime value of acquired customers
     */
    private BigDecimal customerLifetimeValue;

    /**
     * Custom metrics
     */
    private Map<String, BigDecimal> customMetrics;

    /**
     * Dimension values for filtering
     */
    private Map<String, String> dimensions;

    /**
     * Metric source (platform where data came from)
     */
    private String source;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Whether these are aggregated metrics
     */
    @Builder.Default
    private Boolean isAggregated = false;

    /**
     * Aggregation period (HOURLY, DAILY, WEEKLY, MONTHLY)
     */
    private String aggregationPeriod;

    /**
     * Create new CampaignMetrics for a campaign.
     *
     * @param tenantId   the tenant ID
     * @param campaignId the campaign ID
     * @param metricDate the metric date
     */
    public CampaignMetrics(String tenantId, String campaignId, Instant metricDate) {
        super(tenantId);
        this.campaignId = campaignId;
        this.metricDate = metricDate;
        this.impressions = 0L;
        this.reach = 0L;
        this.clicks = 0L;
        this.conversions = 0L;
        this.spend = BigDecimal.ZERO;
        this.revenue = BigDecimal.ZERO;
        this.leads = 0L;
        this.qualifiedLeads = 0L;
        this.sales = 0L;
        this.isAggregated = false;
        this.customMetrics = new HashMap<>();
        this.dimensions = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    /**
     * Create new CampaignMetrics for a channel.
     *
     * @param tenantId   the tenant ID
     * @param campaignId the campaign ID
     * @param channelId  the channel ID
     * @param metricDate the metric date
     */
    public CampaignMetrics(String tenantId, String campaignId, String channelId, Instant metricDate) {
        this(tenantId, campaignId, metricDate);
        this.channelId = channelId;
    }

    /**
     * Add impressions.
     *
     * @param count the number of impressions to add
     */
    public void addImpressions(Long count) {
        if (count != null && count > 0) {
            this.impressions = (this.impressions == null ? 0L : this.impressions) + count;
            this.touch();
        }
    }

    /**
     * Add clicks.
     *
     * @param count the number of clicks to add
     */
    public void addClicks(Long count) {
        if (count != null && count > 0) {
            this.clicks = (this.clicks == null ? 0L : this.clicks) + count;
            this.touch();
        }
    }

    /**
     * Add conversions.
     *
     * @param count the number of conversions to add
     */
    public void addConversions(Long count) {
        if (count != null && count > 0) {
            this.conversions = (this.conversions == null ? 0L : this.conversions) + count;
            this.touch();
        }
    }

    /**
     * Add spend.
     *
     * @param amount the amount to add
     */
    public void addSpend(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            this.spend = (this.spend == null ? BigDecimal.ZERO : this.spend).add(amount);
            this.touch();
        }
    }

    /**
     * Add revenue.
     *
     * @param amount the amount to add
     */
    public void addRevenue(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            this.revenue = (this.revenue == null ? BigDecimal.ZERO : this.revenue).add(amount);
            this.touch();
        }
    }

    /**
     * Calculate click-through rate.
     *
     * @return CTR as percentage or null if impressions is 0
     */
    public BigDecimal calculateClickThroughRate() {
        if (this.impressions == null || this.impressions == 0 || this.clicks == null) {
            return null;
        }
        this.clickThroughRate = new BigDecimal(this.clicks)
            .divide(new BigDecimal(this.impressions), 4, java.math.RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
        return this.clickThroughRate;
    }

    /**
     * Calculate conversion rate.
     *
     * @return conversion rate as percentage or null if clicks is 0
     */
    public BigDecimal calculateConversionRate() {
        if (this.clicks == null || this.clicks == 0 || this.conversions == null) {
            return null;
        }
        this.conversionRate = new BigDecimal(this.conversions)
            .divide(new BigDecimal(this.clicks), 4, java.math.RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
        return this.conversionRate;
    }

    /**
     * Calculate cost per click.
     *
     * @return CPC or null if clicks is 0
     */
    public BigDecimal calculateCostPerClick() {
        if (this.clicks == null || this.clicks == 0 || this.spend == null) {
            return null;
        }
        this.costPerClick = this.spend.divide(new BigDecimal(this.clicks), 2, java.math.RoundingMode.HALF_UP);
        return this.costPerClick;
    }

    /**
     * Calculate cost per thousand impressions.
     *
     * @return CPM or null if impressions is 0
     */
    public BigDecimal calculateCostPerThousandImpressions() {
        if (this.impressions == null || this.impressions == 0 || this.spend == null) {
            return null;
        }
        this.costPerImpression = this.spend
            .divide(new BigDecimal(this.impressions), 2, java.math.RoundingMode.HALF_UP)
            .multiply(new BigDecimal("1000"));
        return this.costPerImpression;
    }

    /**
     * Calculate cost per acquisition.
     *
     * @return CPA or null if conversions is 0
     */
    public BigDecimal calculateCostPerAcquisition() {
        if (this.conversions == null || this.conversions == 0 || this.spend == null) {
            return null;
        }
        this.costPerAcquisition = this.spend.divide(new BigDecimal(this.conversions), 2, java.math.RoundingMode.HALF_UP);
        return this.costPerAcquisition;
    }

    /**
     * Calculate return on ad spend.
     *
     * @return ROAS as multiple or null if spend is 0
     */
    public BigDecimal calculateReturnOnAdSpend() {
        if (this.spend == null || this.spend.compareTo(BigDecimal.ZERO) == 0 || this.revenue == null) {
            return null;
        }
        this.returnOnAdSpend = this.revenue.divide(this.spend, 2, java.math.RoundingMode.HALF_UP);
        return this.returnOnAdSpend;
    }

    /**
     * Calculate return on investment.
     *
     * @return ROI as percentage or null if spend is 0
     */
    public BigDecimal calculateReturnOnInvestment() {
        if (this.spend == null || this.spend.compareTo(BigDecimal.ZERO) == 0 || this.revenue == null) {
            return null;
        }
        BigDecimal profit = this.revenue.subtract(this.spend);
        this.returnOnInvestment = profit.divide(this.spend, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        return this.returnOnInvestment;
    }

    /**
     * Calculate all derived metrics.
     */
    public void calculateAllMetrics() {
        calculateClickThroughRate();
        calculateConversionRate();
        calculateCostPerClick();
        calculateCostPerThousandImpressions();
        calculateCostPerAcquisition();
        calculateReturnOnAdSpend();
        calculateReturnOnInvestment();
    }

    /**
     * Set a custom metric.
     *
     * @param name  the metric name
     * @param value the metric value
     */
    public void setCustomMetric(String name, BigDecimal value) {
        if (this.customMetrics == null) {
            this.customMetrics = new HashMap<>();
        }
        this.customMetrics.put(name, value);
        this.touch();
    }

    /**
     * Get a custom metric.
     *
     * @param name the metric name
     * @return the metric value or null if not set
     */
    public BigDecimal getCustomMetric(String name) {
        if (this.customMetrics == null) {
            return null;
        }
        return this.customMetrics.get(name);
    }

    /**
     * Set a dimension.
     *
     * @param key   the dimension key
     * @param value the dimension value
     */
    public void setDimension(String key, String value) {
        if (this.dimensions == null) {
            this.dimensions = new HashMap<>();
        }
        this.dimensions.put(key, value);
        this.touch();
    }

    /**
     * Get a dimension.
     *
     * @param key the dimension key
     * @return the dimension value or null if not set
     */
    public String getDimension(String key) {
        if (this.dimensions == null) {
            return null;
        }
        return this.dimensions.get(key);
    }

    /**
     * Add metadata.
     *
     * @param key   the metadata key
     * @param value the metadata value
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
        this.touch();
    }

    /**
     * Get metadata.
     *
     * @param key the metadata key
     * @return the metadata value or null if not set
     */
    public Object getMetadata(String key) {
        if (this.metadata == null) {
            return null;
        }
        return this.metadata.get(key);
    }

    /**
     * Check if metrics have any data.
     *
     * @return true if any metric has a positive value
     */
    public boolean hasData() {
        return (this.impressions != null && this.impressions > 0) ||
               (this.clicks != null && this.clicks > 0) ||
               (this.conversions != null && this.conversions > 0) ||
               (this.spend != null && this.spend.compareTo(BigDecimal.ZERO) > 0) ||
               (this.revenue != null && this.revenue.compareTo(BigDecimal.ZERO) > 0);
    }

    /**
     * Get efficiency score (0-100) based on performance.
     *
     * @return efficiency score or null if insufficient data
     */
    public BigDecimal getEfficiencyScore() {
        if (!hasData()) {
            return null;
        }

        // Simple efficiency calculation based on ROAS and conversion rate
        BigDecimal score = BigDecimal.ZERO;

        if (this.returnOnAdSpend != null) {
            // ROAS > 3 = excellent, > 2 = good, > 1 = acceptable
            if (this.returnOnAdSpend.compareTo(new BigDecimal("3")) >= 0) {
                score = score.add(new BigDecimal("40"));
            } else if (this.returnOnAdSpend.compareTo(new BigDecimal("2")) >= 0) {
                score = score.add(new BigDecimal("30"));
            } else if (this.returnOnAdSpend.compareTo(BigDecimal.ONE) >= 0) {
                score = score.add(new BigDecimal("20"));
            }
        }

        if (this.conversionRate != null) {
            // Conversion rate > 5% = excellent, > 2% = good, > 1% = acceptable
            if (this.conversionRate.compareTo(new BigDecimal("5")) >= 0) {
                score = score.add(new BigDecimal("40"));
            } else if (this.conversionRate.compareTo(new BigDecimal("2")) >= 0) {
                score = score.add(new BigDecimal("30"));
            } else if (this.conversionRate.compareTo(BigDecimal.ONE) >= 0) {
                score = score.add(new BigDecimal("20"));
            }
        }

        if (this.clickThroughRate != null) {
            // CTR > 2% = excellent, > 1% = good, > 0.5% = acceptable
            if (this.clickThroughRate.compareTo(new BigDecimal("2")) >= 0) {
                score = score.add(new BigDecimal("20"));
            } else if (this.clickThroughRate.compareTo(BigDecimal.ONE) >= 0) {
                score = score.add(new BigDecimal("15"));
            } else if (this.clickThroughRate.compareTo(new BigDecimal("0.5")) >= 0) {
                score = score.add(new BigDecimal("10"));
            }
        }

        return score.min(new BigDecimal("100"));
    }
}
