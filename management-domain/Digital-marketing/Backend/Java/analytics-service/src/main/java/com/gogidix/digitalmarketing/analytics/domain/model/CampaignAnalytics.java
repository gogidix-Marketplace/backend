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
 * Campaign Analytics - Campaign performance data
 *
 * <p>Aggregates performance metrics for marketing campaigns.</p>
 *
 * <p>Includes:</p>
 * <ul>
 *   <li>Impressions and reach metrics</li>
 *   <li>Engagement metrics (clicks, CTR)</li>
 *   <li>Conversion metrics</li>
 *   <li>Cost metrics (CPC, CPM, CPA)</li>
 *   <li>ROI and ROAS</li>
 * </ul>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "campaign_analytics")
@TypeAlias("campaign_analytics")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "campaign_analytics_tenant_idx", def = "{'tenantId': 1, 'campaignId': 1, 'startDate': -1}")
@CompoundIndex(name = "campaign_analytics_tenant_status_idx", def = "{'tenantId': 1, 'status': 1, 'startDate': -1}")
public class CampaignAnalytics extends BaseEntity {

    /**
     * Reference to the campaign ID
     */
    @Indexed
    private String campaignId;

    /**
     * Campaign name (for quick reference)
     */
    @Indexed
    private String campaignName;

    /**
     * Campaign type (EMAIL, SOCIAL, SEARCH, DISPLAY, etc.)
     */
    @Indexed
    private String campaignType;

    /**
     * Campaign status (ACTIVE, PAUSED, COMPLETED, CANCELLED)
     */
    @Indexed
    private String status;

    /**
     * Start date of the campaign
     */
    @Indexed
    private Instant startDate;

    /**
     * End date of the campaign
     */
    private Instant endDate;

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
     * Click-through rate (CTR)
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
     * Cost per click (CPC)
     */
    private BigDecimal cpc;

    /**
     * Cost per thousand impressions (CPM)
     */
    private BigDecimal cpm;

    /**
     * Cost per acquisition (CPA)
     */
    private BigDecimal cpa;

    /**
     * Total revenue generated
     */
    private BigDecimal revenue;

    /**
     * Return on investment (ROI)
     */
    private BigDecimal roi;

    /**
     * Return on ad spend (ROAS)
     */
    private BigDecimal roas;

    /**
     * Average order value
     */
    private BigDecimal averageOrderValue;

    /**
     * Customer acquisition cost
     */
    private BigDecimal customerAcquisitionCost;

    /**
     * Lifetime value of acquired customers
     */
    private BigDecimal customerLifetimeValue;

    /**
     * Target budget for the campaign
     */
    private BigDecimal targetBudget;

    /**
     * Budget utilization percentage
     */
    private BigDecimal budgetUtilization;

    /**
     * Target impressions
     */
    private BigDecimal targetImpressions;

    /**
     * Target conversions
     */
    private BigDecimal targetConversions;

    /**
     * Achievement status (ON_TRACK, AHEAD, BEHIND)
     */
    private String achievementStatus;

    /**
     * Additional metrics as key-value pairs
     */
    private Map<String, Object> additionalMetrics;

    /**
     * Segmentation data (by demographics, location, etc.)
     */
    private Map<String, Object> segmentationData;

    /**
     * Last updated timestamp for analytics
     */
    private Instant lastAnalyticsUpdate;

    /**
     * Data freshness score (0.0 to 1.0)
     */
    private BigDecimal dataFreshness;

    /**
     * Number of touchpoints in the campaign
     */
    private Integer touchpointCount;

    /**
     * Create a new campaign analytics entry.
     *
     * @param tenantId   the tenant ID
     * @param campaignId the campaign ID
     * @param campaignName the campaign name
     * @param campaignType the campaign type
     */
    public CampaignAnalytics(String tenantId, String campaignId, String campaignName, String campaignType) {
        super(tenantId);
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.campaignType = campaignType;
        this.startDate = Instant.now();
        this.status = "ACTIVE";
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
        calculateBudgetUtilization();
        calculateAverageOrderValue();
        updateAchievementStatus();
    }

    /**
     * Calculate budget utilization.
     */
    public void calculateBudgetUtilization() {
        if (targetBudget != null && targetBudget.compareTo(BigDecimal.ZERO) > 0 && spend != null) {
            this.budgetUtilization = spend.divide(targetBudget, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
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
     * Update achievement status based on targets.
     */
    public void updateAchievementStatus() {
        boolean aheadTarget = false;
        boolean onTrack = false;

        // Check impressions target
        if (targetImpressions != null && impressions != null) {
            if (impressions.compareTo(targetImpressions) >= 0) {
                aheadTarget = true;
            } else if (impressions.compareTo(targetImpressions.multiply(new BigDecimal("0.8"))) >= 0) {
                onTrack = true;
            }
        }

        // Check conversions target
        if (targetConversions != null && conversions != null) {
            if (conversions.compareTo(targetConversions) >= 0) {
                aheadTarget = true;
            } else if (conversions.compareTo(targetConversions.multiply(new BigDecimal("0.8"))) >= 0) {
                onTrack = true;
            }
        }

        if (aheadTarget) {
            this.achievementStatus = "AHEAD";
        } else if (onTrack) {
            this.achievementStatus = "ON_TRACK";
        } else {
            this.achievementStatus = "BEHIND";
        }
    }

    /**
     * Check if campaign is profitable (ROI > 0).
     *
     * @return true if ROI is positive
     */
    public boolean isProfitable() {
        return roi != null && roi.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Check if campaign is active.
     *
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    /**
     * Check if campaign has ended.
     *
     * @return true if end date has passed
     */
    public boolean hasEnded() {
        return endDate != null && endDate.isBefore(Instant.now());
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
     * Add segmentation data.
     *
     * @param segment the segment name
     * @param data    the segment data
     */
    public void addSegmentationData(String segment, Object data) {
        if (this.segmentationData == null) {
            this.segmentationData = new HashMap<>();
        }
        this.segmentationData.put(segment, data);
    }

    /**
     * Update the analytics timestamp.
     */
    public void markAnalyticsUpdated() {
        this.lastAnalyticsUpdate = Instant.now();
        this.touch();
    }
}
