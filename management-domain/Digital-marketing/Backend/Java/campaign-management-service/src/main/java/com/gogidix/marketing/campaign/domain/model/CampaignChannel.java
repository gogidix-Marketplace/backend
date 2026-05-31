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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CampaignChannel - Channel configuration for campaigns
 *
 * <p>Represents a marketing channel (email, social media, etc.)
 * with its specific configuration, budget allocation, and settings.</p>
 */
@Document(collection = "campaign_channels")
@TypeAlias("campaign_channel")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "channel_tenant_campaign_idx", def = "{'tenantId': 1, 'campaignId': 1, 'channelType': 1}")
public class CampaignChannel extends BaseEntity {

    /**
     * Campaign ID this channel belongs to
     */
    @Indexed
    private String campaignId;

    /**
     * Channel type (EMAIL, SOCIAL_MEDIA, SMS, PUSH_NOTIFICATION, WEB, etc.)
     */
    @Indexed
    private String channelType;

    /**
     * Platform name (e.g., FACEBOOK, GOOGLE, TWITTER)
     */
    private String platform;

    /**
     * Channel configuration map
     */
    private Map<String, Object> config;

    /**
     * Channel name (custom name for this instance)
     */
    private String channelName;

    /**
     * Channel status (ACTIVE, PAUSED, INACTIVE)
     */
    @Builder.Default
    private String status = "ACTIVE";

    /**
     * Budget allocated to this channel
     */
    private BigDecimal allocatedBudget;

    /**
     * Amount spent on this channel
     */
    @Builder.Default
    private BigDecimal spentAmount = BigDecimal.ZERO;

    /**
     * Channel-specific settings
     */
    private Map<String, Object> settings;

    /**
     * Channel target audience segment
     */
    private String targetSegment;

    /**
     * Channel priority relative to other channels
     */
    @Builder.Default
    private Integer priority = 0;

    /**
     * Expected reach (number of people)
     */
    private Long expectedReach;

    /**
     * Actual reach achieved
     */
    private Long actualReach;

    /**
     * Channel start date (can differ from campaign start)
     */
    private Instant startDate;

    /**
     * Scheduled date/time for this channel
     */
    private Instant scheduledAt;

    /**
     * Channel end date (can differ from campaign end)
     */
    private Instant endDate;

    /**
     * Channel-specific KPIs
     */
    private Map<String, BigDecimal> kpis;

    /**
     * Channel metrics snapshot
     */
    private Map<String, Object> metrics;

    /**
     * Integration provider ID (e.g., Mailchimp, Facebook Ads)
     */
    private String providerId;

    /**
     * External campaign ID (in the provider's system)
     */
    private String externalCampaignId;

    /**
     * External configuration reference
     */
    private String externalConfigId;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Whether this channel is enabled
     */
    @Builder.Default
    private Boolean enabled = true;

    /**
     * Create a new CampaignChannel.
     *
     * @param tenantId   the tenant ID
     * @param campaignId the campaign ID
     * @param channelType the channel type
     */
    public CampaignChannel(String tenantId, String campaignId, String channelType) {
        super(tenantId);
        this.campaignId = campaignId;
        this.channelType = channelType;
        this.status = "ACTIVE";
        this.spentAmount = BigDecimal.ZERO;
        this.priority = 0;
        this.enabled = true;
        this.settings = new HashMap<>();
        this.kpis = new HashMap<>();
        this.metrics = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    /**
     * Check if channel is active.
     *
     * @return true if channel is active
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status) && Boolean.TRUE.equals(this.enabled);
    }

    /**
     * Check if channel is paused.
     *
     * @return true if channel is paused
     */
    public boolean isPaused() {
        return "PAUSED".equals(this.status);
    }

    /**
     * Check if channel is inactive.
     *
     * @return true if channel is inactive
     */
    public boolean isInactive() {
        return "INACTIVE".equals(this.status) || !Boolean.TRUE.equals(this.enabled);
    }

    /**
     * Activate this channel.
     */
    public void activate() {
        this.status = "ACTIVE";
        this.enabled = true;
        this.touch();
    }

    /**
     * Pause this channel.
     */
    public void pause() {
        this.status = "PAUSED";
        this.touch();
    }

    /**
     * Deactivate this channel.
     */
    public void deactivate() {
        this.status = "INACTIVE";
        this.enabled = false;
        this.touch();
    }

    /**
     * Record spending against this channel.
     *
     * @param amount the amount spent
     */
    public void recordSpending(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.spentAmount == null) {
            this.spentAmount = BigDecimal.ZERO;
        }
        this.spentAmount = this.spentAmount.add(amount);
        this.touch();
    }

    /**
     * Get remaining budget for this channel.
     *
     * @return remaining budget or null if no budget set
     */
    public BigDecimal getRemainingBudget() {
        if (this.allocatedBudget == null || this.spentAmount == null) {
            return this.allocatedBudget;
        }
        return this.allocatedBudget.subtract(this.spentAmount);
    }

    /**
     * Check if channel budget has been exhausted.
     *
     * @return true if spent amount equals or exceeds allocated budget
     */
    public boolean isBudgetExhausted() {
        if (this.allocatedBudget == null || this.spentAmount == null) {
            return false;
        }
        return this.spentAmount.compareTo(this.allocatedBudget) >= 0;
    }

    /**
     * Get budget utilization as percentage.
     *
     * @return percentage spent (0-100) or null if no budget set
     */
    public BigDecimal getBudgetUtilization() {
        if (this.allocatedBudget == null || this.spentAmount == null || this.allocatedBudget.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return this.spentAmount.divide(this.allocatedBudget, 4, java.math.RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
    }

    /**
     * Get reach achievement rate as percentage.
     *
     * @return percentage of expected reach achieved or null
     */
    public BigDecimal getReachAchievement() {
        if (this.expectedReach == null || this.expectedReach == 0 || this.actualReach == null) {
            return null;
        }
        return new BigDecimal(this.actualReach)
            .divide(new BigDecimal(this.expectedReach), 4, java.math.RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
    }

    /**
     * Set a channel-specific setting.
     *
     * @param key   the setting key
     * @param value the setting value
     */
    public void setSetting(String key, Object value) {
        if (this.settings == null) {
            this.settings = new HashMap<>();
        }
        this.settings.put(key, value);
        this.touch();
    }

    /**
     * Get a channel-specific setting.
     *
     * @param key the setting key
     * @return the setting value or null if not set
     */
    public Object getSetting(String key) {
        if (this.settings == null) {
            return null;
        }
        return this.settings.get(key);
    }

    /**
     * Set a KPI value.
     *
     * @param kpiName the KPI name
     * @param value   the KPI value
     */
    public void setKpi(String kpiName, BigDecimal value) {
        if (this.kpis == null) {
            this.kpis = new HashMap<>();
        }
        this.kpis.put(kpiName, value);
        this.touch();
    }

    /**
     * Get a KPI value.
     *
     * @param kpiName the KPI name
     * @return the KPI value or null if not set
     */
    public BigDecimal getKpi(String kpiName) {
        if (this.kpis == null) {
            return null;
        }
        return this.kpis.get(kpiName);
    }

    /**
     * Update a metric value.
     *
     * @param metricName the metric name
     * @param value      the metric value
     */
    public void updateMetric(String metricName, Object value) {
        if (this.metrics == null) {
            this.metrics = new HashMap<>();
        }
        this.metrics.put(metricName, value);
        this.touch();
    }

    /**
     * Get a metric value.
     *
     * @param metricName the metric name
     * @return the metric value or null if not set
     */
    public Object getMetric(String metricName) {
        if (this.metrics == null) {
            return null;
        }
        return this.metrics.get(metricName);
    }

    /**
     * Add metadata to the channel.
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
     * Get metadata value.
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
     * Check if channel is currently running based on dates.
     *
     * @return true if current date is within start and end dates
     */
    public boolean isCurrentlyRunning() {
        Instant now = Instant.now();
        if (this.startDate != null && now.isBefore(this.startDate)) {
            return false;
        }
        if (this.endDate != null && now.isAfter(this.endDate)) {
            return false;
        }
        return true;
    }

    /**
     * Link to external provider campaign.
     *
     * @param providerId        the provider ID
     * @param externalCampaignId the external campaign ID
     */
    public void linkToExternal(String providerId, String externalCampaignId) {
        this.providerId = providerId;
        this.externalCampaignId = externalCampaignId;
        this.touch();
    }

    /**
     * Unlink from external provider.
     */
    public void unlinkFromExternal() {
        this.providerId = null;
        this.externalCampaignId = null;
        this.touch();
    }

    /**
     * Check if linked to external provider.
     *
     * @return true if linked to external provider
     */
    public boolean isLinkedToExternal() {
        return this.providerId != null && this.externalCampaignId != null;
    }
}
