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
import java.util.Objects;

/**
 * CampaignMetric - Campaign-specific performance metrics
 *
 * <p>This entity stores aggregated performance metrics for individual marketing campaigns.
 * It provides comprehensive analytics for measuring campaign effectiveness.</p>
 *
 * <p>Key Metrics:</p>
 * <ul>
 *   <li>Impressions - Number of times campaign was displayed</li>
 *   <li>Clicks - Number of clicks on campaign assets</li>
 *   <li>Conversions - Number of desired actions taken</li>
 *   <li>Cost - Total spend on the campaign</li>
 *   <li>Revenue - Total revenue generated</li>
 *   <li>ROI - Return on Investment</li>
 * </ul>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "campaign_metrics")
@TypeAlias("campaign_metric")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "campaign_metric_tenant_campaign_idx", def = "{'tenantId': 1, 'campaignId': 1, 'startDate': -1}")
@CompoundIndex(name = "campaign_metric_tenant_date_idx", def = "{'tenantId': 1, 'startDate': -1}")
@CompoundIndex(name = "campaign_metric_tenant_status_idx", def = "{'tenantId': 1, 'status': 1, 'startDate': -1}")
public class CampaignMetric extends BaseEntity {

    /**
     * Reference to the campaign ID
     */
    @Indexed
    private String campaignId;

    /**
     * Campaign name (denormalized for queries)
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
     * Start date of the metric period
     */
    @Indexed
    private Instant startDate;

    /**
     * End date of the metric period
     */
    @Indexed
    private Instant endDate;

    // === Reach Metrics ===

    /**
     * Total impressions
     */
    private BigDecimal impressions;

    /**
     * Unique reach (number of unique people who saw the campaign)
     */
    private BigDecimal reach;

    /**
     * Frequency (average number of times each person saw the campaign)
     */
    private BigDecimal frequency;

    // === Engagement Metrics ===

    /**
     * Total clicks
     */
    private BigDecimal clicks;

    /**
     * Click-through rate (CTR)
     */
    private BigDecimal ctr;

    /**
     * Total engagements (likes, shares, comments, etc.)
     */
    private BigDecimal engagements;

    /**
     * Engagement rate
     */
    private BigDecimal engagementRate;

    /**
     * Video views (if applicable)
     */
    private BigDecimal videoViews;

    /**
     * Video completion rate
     */
    private BigDecimal videoCompletionRate;

    /**
     * Average time spent (in seconds)
     */
    private BigDecimal avgTimeSpent;

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
     * Cost per conversion (CPA)
     */
    private BigDecimal costPerConversion;

    /**
     * Conversion value (total value of all conversions)
     */
    private BigDecimal conversionValue;

    // === Cost Metrics ===

    /**
     * Total spend/cost
     */
    private BigDecimal spend;

    /**
     * Cost per click (CPC)
     */
    private BigDecimal costPerClick;

    /**
     * Cost per thousand impressions (CPM)
     */
    private BigDecimal costPerThousand;

    /**
     * Cost per acquisition (CPA)
     */
    private BigDecimal costPerAcquisition;

    // === Revenue Metrics ===

    /**
     * Total revenue generated
     */
    private BigDecimal revenue;

    /**
     * Return on investment (ROI) as percentage
     */
    private BigDecimal roi;

    /**
     * Return on ad spend (ROAS)
     */
    private BigDecimal roas;

    /**
     * Profit (revenue - spend)
     */
    private BigDecimal profit;

    /**
     * Profit margin as percentage
     */
    private BigDecimal profitMargin;

    // === Customer Metrics ===

    /**
     * New customers acquired
     */
    private BigDecimal newCustomers;

    /**
     * Customer acquisition cost (CAC)
     */
    private BigDecimal customerAcquisitionCost;

    /**
     * Customer lifetime value (CLV)
     */
    private BigDecimal customerLifetimeValue;

    /**
     * CLV to CAC ratio
     */
    private BigDecimal clvToCacRatio;

    // === Target Metrics ===

    /**
     * Target impressions
     */
    private BigDecimal targetImpressions;

    /**
     * Target clicks
     */
    private BigDecimal targetClicks;

    /**
     * Target conversions
     */
    private BigDecimal targetConversions;

    /**
     * Target budget
     */
    private BigDecimal targetBudget;

    /**
     * Achievement status (AHEAD, ON_TRACK, BEHIND)
     */
    private String achievementStatus;

    // === Additional Data ===

    /**
     * Segmentation metrics (by demographic, location, device, etc.)
     */
    private Map<String, Object> segmentationData;

    /**
     * A/B test results
     */
    private Map<String, Object> abTestResults;

    /**
     * Creative performance data
     */
    private Map<String, Object> creativePerformance;

    /**
     * Additional metrics
     */
    private Map<String, Object> additionalMetrics;

    /**
     * Last updated timestamp
     */
    private Instant lastUpdatedAt;

    /**
     * Data freshness score (0.0 to 1.0)
     */
    private BigDecimal dataFreshness;

    /**
     * Create a new campaign metric.
     *
     * @param tenantId     the tenant ID
     * @param campaignId   the campaign ID
     * @param campaignName the campaign name
     * @param campaignType the campaign type
     */
    public CampaignMetric(String tenantId, String campaignId, String campaignName, String campaignType) {
        super(tenantId);
        this.campaignId = Objects.requireNonNull(campaignId, "Campaign ID is required");
        this.campaignName = campaignName;
        this.campaignType = Objects.requireNonNull(campaignType, "Campaign type is required");
        this.status = "ACTIVE";
        this.startDate = Instant.now();
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
     * Calculate ROI.
     */
    public void calculateRoi() {
        if (spend != null && spend.compareTo(BigDecimal.ZERO) > 0 && revenue != null) {
            this.profit = revenue.subtract(spend);
            this.roi = profit.divide(spend, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            this.profitMargin = profit.divide(revenue, 4, java.math.RoundingMode.HALF_UP)
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
     * Calculate customer acquisition cost.
     */
    public void calculateCustomerAcquisitionCost() {
        if (spend != null && newCustomers != null && newCustomers.compareTo(BigDecimal.ZERO) > 0) {
            this.customerAcquisitionCost = spend.divide(newCustomers, 2, java.math.RoundingMode.HALF_UP);
        }
    }

    /**
     * Calculate CLV to CAC ratio.
     */
    public void calculateClvToCacRatio() {
        if (customerLifetimeValue != null && customerAcquisitionCost != null
                && customerAcquisitionCost.compareTo(BigDecimal.ZERO) > 0) {
            this.clvToCacRatio = customerLifetimeValue.divide(customerAcquisitionCost, 2, java.math.RoundingMode.HALF_UP);
        }
    }

    /**
     * Calculate all derived metrics.
     */
    public void calculateAllMetrics() {
        calculateCtr();
        calculateConversionRate();
        calculateEngagementRate();
        calculateCostPerClick();
        calculateCostPerThousand();
        calculateCostPerAcquisition();
        calculateRoi();
        calculateRoas();
        calculateCustomerAcquisitionCost();
        calculateClvToCacRatio();
        updateAchievementStatus();
        markAsUpdated();
    }

    /**
     * Update achievement status based on targets.
     */
    public void updateAchievementStatus() {
        int goalsMet = 0;
        int goalsSet = 0;

        if (targetImpressions != null && impressions != null) {
            goalsSet++;
            if (impressions.compareTo(targetImpressions) >= 0) goalsMet++;
            else if (impressions.compareTo(targetImpressions.multiply(new BigDecimal("0.8"))) < 0) {
                // Below 80% - behind
                this.achievementStatus = "BEHIND";
                return;
            }
        }

        if (targetClicks != null && clicks != null) {
            goalsSet++;
            if (clicks.compareTo(targetClicks) >= 0) goalsMet++;
            else if (clicks.compareTo(targetClicks.multiply(new BigDecimal("0.8"))) < 0) {
                this.achievementStatus = "BEHIND";
                return;
            }
        }

        if (targetConversions != null && conversions != null) {
            goalsSet++;
            if (conversions.compareTo(targetConversions) >= 0) goalsMet++;
            else if (conversions.compareTo(targetConversions.multiply(new BigDecimal("0.8"))) < 0) {
                this.achievementStatus = "BEHIND";
                return;
            }
        }

        if (targetBudget != null && spend != null) {
            goalsSet++;
            if (spend.compareTo(targetBudget) <= 0) goalsMet++;
            else if (spend.compareTo(targetBudget.multiply(new BigDecimal("1.2"))) > 0) {
                // Over budget by 20%
                this.achievementStatus = "BEHIND";
                return;
            }
        }

        if (goalsSet == 0) {
            this.achievementStatus = "NO_TARGET";
        } else if (goalsMet == goalsSet) {
            this.achievementStatus = "AHEAD";
        } else {
            this.achievementStatus = "ON_TRACK";
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
     * Get the duration of the campaign in days.
     *
     * @return duration in days, or null if end date not set
     */
    public Long getDurationDays() {
        if (endDate == null) {
            return null;
        }
        long seconds = java.time.Duration.between(startDate, endDate).getSeconds();
        return seconds / (24 * 60 * 60);
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
     * Add A/B test result.
     *
     * @param testName the test name
     * @param result   the test result
     */
    public void addAbTestResult(String testName, Object result) {
        if (this.abTestResults == null) {
            this.abTestResults = new HashMap<>();
        }
        this.abTestResults.put(testName, result);
    }

    /**
     * Add creative performance data.
     *
     * @param creativeId the creative ID
     * @param metrics    the creative metrics
     */
    public void addCreativePerformance(String creativeId, Object metrics) {
        if (this.creativePerformance == null) {
            this.creativePerformance = new HashMap<>();
        }
        this.creativePerformance.put(creativeId, metrics);
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
     * Calculate efficiency score (0-100) based on ROI, ROAS, and conversion rate.
     *
     * @return efficiency score
     */
    public BigDecimal calculateEfficiencyScore() {
        BigDecimal score = BigDecimal.ZERO;
        int factors = 0;

        // ROI factor (40 points max)
        if (roi != null) {
            BigDecimal roiScore = roi.compareTo(new BigDecimal("300")) > 0
                ? new BigDecimal("40")
                : roi.divide(new BigDecimal("300"), 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("40"));
            score = score.add(roiScore);
            factors++;
        }

        // ROAS factor (30 points max)
        if (roas != null) {
            BigDecimal roasScore = roas.compareTo(new BigDecimal("4")) > 0
                ? new BigDecimal("30")
                : roas.divide(new BigDecimal("4"), 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("30"));
            score = score.add(roasScore);
            factors++;
        }

        // Conversion rate factor (30 points max)
        if (conversionRate != null) {
            BigDecimal convScore = conversionRate.compareTo(new BigDecimal("5")) > 0
                ? new BigDecimal("30")
                : conversionRate.divide(new BigDecimal("5"), 2, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("30"));
            score = score.add(convScore);
            factors++;
        }

        return factors > 0 ? score : BigDecimal.ZERO;
    }

    /**
     * Get performance rating based on efficiency score.
     *
     * @return performance rating (EXCELLENT, GOOD, AVERAGE, POOR)
     */
    public String getPerformanceRating() {
        BigDecimal score = calculateEfficiencyScore();
        if (score.compareTo(new BigDecimal("80")) >= 0) {
            return "EXCELLENT";
        } else if (score.compareTo(new BigDecimal("60")) >= 0) {
            return "GOOD";
        } else if (score.compareTo(new BigDecimal("40")) >= 0) {
            return "AVERAGE";
        } else {
            return "POOR";
        }
    }
}
