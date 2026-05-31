package com.gogidix.digitalmarketing.emailmarketing.domain.model;

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

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EmailMetrics - Aggregated delivery and engagement metrics for campaigns and lists.
 *
 * <p>Contains aggregated statistics for email campaign performance and list health.
 * Metrics are calculated from individual email message events.</p>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "email_metrics")
@TypeAlias("email_metrics")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "metrics_tenant_campaign_idx", def = "{'tenantId': 1, 'campaignId': 1, 'metricDate': -1}")
@CompoundIndex(name = "metrics_tenant_entity_idx", def = "{'tenantId': 1, 'entityType': 1, 'entityId': 1, 'metricDate': -1}")
public class EmailMetrics extends BaseEntity {

    /**
     * Entity type these metrics are for (CAMPAIGN, LIST, TEMPLATE, GLOBAL)
     */
    @Indexed
    private String entityType;

    /**
     * Entity ID (campaign ID, list ID, template ID, or null for global)
     */
    @Indexed
    private String entityId;

    /**
     * Campaign ID (if metrics are for a campaign)
     */
    @Indexed
    private String campaignId;

    /**
     * List ID (if metrics are for a list)
     */
    @Indexed
    private String listId;

    /**
     * Template ID (if metrics are for a template)
     */
    @Indexed
    private String templateId;

    /**
     * Metric date (for time-series data)
     */
    @Indexed
    private Instant metricDate;

    /**
     * Period type (HOURLY, DAILY, WEEKLY, MONTHLY, LIFETIME)
     */
    private String periodType;

    /**
     * Period start date
     */
    private Instant periodStart;

    /**
     * Period end date
     */
    private Instant periodEnd;

    // Delivery Metrics

    /**
     * Total emails sent
     */
    private Integer sentCount;

    /**
     * Total emails delivered
     */
    private Integer deliveredCount;

    /**
     * Total hard bounces
     */
    private Integer hardBounceCount;

    /**
     * Total soft bounces
     */
    private Integer softBounceCount;

    /**
     * Total bounced emails
     */
    private Integer bouncedCount;

    /**
     * Total deferred emails
     */
    private Integer deferredCount;

    /**
     * Total failed emails
     */
    private Integer failedCount;

    // Engagement Metrics

    /**
     * Total opens
     */
    private Integer openCount;

    /**
     * Unique opens
     */
    private Integer uniqueOpenCount;

    /**
     * Total clicks
     */
    private Integer clickCount;

    /**
     * Unique clicks
     */
    private Integer uniqueClickCount;

    /**
     * Forward count
     */
    private Integer forwardCount;

    /**
     * Share count
     */
    private Integer shareCount;

    /**
     * Reply count
     */
    private Integer replyCount;

    // Unsubscribe/Complaint Metrics

    /**
     * Unsubscribe count
     */
    private Integer unsubscribeCount;

    /**
     * Spam complaint count
     */
    private Integer complaintCount;

    /**
     * Spam report count
     */
    private Integer spamReportCount;

    // Rate Metrics (calculated)

    /**
     * Delivery rate percentage
     */
    private Double deliveryRate;

    /**
     * Open rate percentage
     */
    private Double openRate;

    /**
     * Click rate percentage
     */
    private Double clickRate;

    /**
     * Click-to-open rate percentage
     */
    private Double clickToOpenRate;

    /**
     * Bounce rate percentage
     */
    private Double bounceRate;

    /**
     * Hard bounce rate percentage
     */
    private Double hardBounceRate;

    /**
     * Soft bounce rate percentage
     */
    private Double softBounceRate;

    /**
     * Unsubscribe rate percentage
     */
    private Double unsubscribeRate;

    /**
     * Complaint rate percentage
     */
    private Double complaintRate;

    // Conversion Metrics

    /**
     * Conversion count
     */
    private Integer conversionCount;

    /**
     * Conversion rate percentage
     */
    private Double conversionRate;

    /**
     * Revenue generated
     */
    private Double revenue;

    /**
     * Cost incurred
     */
    private Double cost;

    /**
     * Return on investment
     */
    private Double roi;

    // Time Metrics

    /**
     * Average time to open (seconds)
     */
    private Double avgTimeToOpen;

    /**
     * Average time to click (seconds)
     */
    private Double avgTimeToClick;

    // Device Metrics

    /**
     * Desktop open count
     */
    private Integer desktopOpenCount;

    /**
     * Mobile open count
     */
    private Integer mobileOpenCount;

    /**
     * Tablet open count
     */
    private Integer tabletOpenCount;

    /**
     * Desktop open percentage
     */
    private Double desktopOpenRate;

    /**
     * Mobile open percentage
     */
    private Double mobileOpenRate;

    /**
     * Tablet open percentage
     */
    private Double tabletOpenRate;

    // Link Metrics

    /**
     * Top clicked URLs with counts
     */
    private List<LinkMetric> linkMetrics;

    /**
     * Geographic distribution
     */
    private List<GeoMetric> geoMetrics;

    /**
     * ISP/domain distribution
     */
    private List<IspMetric> ispMetrics;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * When metrics were last calculated
     */
    private Instant calculatedAt;

    /**
     * Metrics version (for tracking recalculations)
     */
    private String version;

    /**
     * Link metric inner class
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LinkMetric {
        private String url;
        private Integer clicks;
        private Integer uniqueClicks;
        private Double clickRate;

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public Integer getClicks() { return clicks; }
        public void setClicks(Integer clicks) { this.clicks = clicks; }
        public Integer getUniqueClicks() { return uniqueClicks; }
        public void setUniqueClicks(Integer uniqueClicks) { this.uniqueClicks = uniqueClicks; }
        public Double getClickRate() { return clickRate; }
        public void setClickRate(Double clickRate) { this.clickRate = clickRate; }
    }

    /**
     * Geographic metric inner class
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeoMetric {
        private String country;
        private String city;
        private Integer opens;
        private Integer clicks;
        private Double openRate;

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public Integer getOpens() { return opens; }
        public void setOpens(Integer opens) { this.opens = opens; }
        public Integer getClicks() { return clicks; }
        public void setClicks(Integer clicks) { this.clicks = clicks; }
        public Double getOpenRate() { return openRate; }
        public void setOpenRate(Double openRate) { this.openRate = openRate; }
    }

    /**
     * ISP metric inner class
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IspMetric {
        private String isp;
        private String domain;
        private Integer delivered;
        private Integer opened;
        private Integer clicked;
        private Double openRate;
        private Double clickRate;
    }

    /**
     * Create new metrics for an entity.
     *
     * @param tenantId  the tenant ID
     * @param entityType the entity type
     * @param entityId   the entity ID
     */
    public EmailMetrics(String tenantId, String entityType, String entityId) {
        super(tenantId);
        this.entityType = entityType;
        this.entityId = entityId;
        this.periodType = "LIFETIME";
        this.metricDate = Instant.now();
        this.linkMetrics = new ArrayList<>();
        this.geoMetrics = new ArrayList<>();
        this.ispMetrics = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.initializeCounts();
    }

    /**
     * Create new metrics for a campaign.
     *
     * @param tenantId  the tenant ID
     * @param campaignId the campaign ID
     */
    public EmailMetrics(String tenantId, String campaignId) {
        this(tenantId, "CAMPAIGN", campaignId);
        this.campaignId = campaignId;
    }

    /**
     * Initialize all counts to zero.
     */
    private void initializeCounts() {
        this.sentCount = 0;
        this.deliveredCount = 0;
        this.hardBounceCount = 0;
        this.softBounceCount = 0;
        this.bouncedCount = 0;
        this.deferredCount = 0;
        this.failedCount = 0;
        this.openCount = 0;
        this.uniqueOpenCount = 0;
        this.clickCount = 0;
        this.uniqueClickCount = 0;
        this.forwardCount = 0;
        this.shareCount = 0;
        this.replyCount = 0;
        this.unsubscribeCount = 0;
        this.complaintCount = 0;
        this.spamReportCount = 0;
        this.conversionCount = 0;
        this.desktopOpenCount = 0;
        this.mobileOpenCount = 0;
        this.tabletOpenCount = 0;
    }

    /**
     * Calculate all rate metrics.
     */
    public void calculateRates() {
        int totalSent = sentCount != null ? sentCount : 0;
        int totalDelivered = deliveredCount != null ? deliveredCount : 0;
        int totalOpened = uniqueOpenCount != null ? uniqueOpenCount : 0;
        int totalClicked = uniqueClickCount != null ? uniqueClickCount : 0;
        int totalBounced = bouncedCount != null ? bouncedCount : 0;

        if (totalSent > 0) {
            this.deliveryRate = totalDelivered > 0 ? ((double) totalDelivered / totalSent) * 100 : 0.0;
            this.openRate = totalOpened > 0 ? ((double) totalOpened / totalSent) * 100 : 0.0;
            this.clickRate = totalClicked > 0 ? ((double) totalClicked / totalSent) * 100 : 0.0;
            this.bounceRate = totalBounced > 0 ? ((double) totalBounced / totalSent) * 100 : 0.0;
            this.unsubscribeRate = (unsubscribeCount != null && unsubscribeCount > 0) ?
                ((double) unsubscribeCount / totalSent) * 100 : 0.0;
            this.complaintRate = (complaintCount != null && complaintCount > 0) ?
                ((double) complaintCount / totalSent) * 100 : 0.0;
        }

        if (totalOpened > 0) {
            this.clickToOpenRate = totalClicked > 0 ? ((double) totalClicked / totalOpened) * 100 : 0.0;
        }

        if (totalBounced > 0) {
            this.hardBounceRate = (hardBounceCount != null && hardBounceCount > 0) ?
                ((double) hardBounceCount / totalBounced) * 100 : 0.0;
            this.softBounceRate = (softBounceCount != null && softBounceCount > 0) ?
                ((double) softBounceCount / totalBounced) * 100 : 0.0;
        }

        if (totalDelivered > 0) {
            this.conversionRate = (conversionCount != null && conversionCount > 0) ?
                ((double) conversionCount / totalDelivered) * 100 : 0.0;
        }

        calculateDeviceRates();
        this.calculatedAt = Instant.now();
    }

    /**
     * Calculate device open rates.
     */
    private void calculateDeviceRates() {
        int totalOpens = openCount != null ? openCount : 0;
        if (totalOpens > 0) {
            this.desktopOpenRate = (desktopOpenCount != null && desktopOpenCount > 0) ?
                ((double) desktopOpenCount / totalOpens) * 100 : 0.0;
            this.mobileOpenRate = (mobileOpenCount != null && mobileOpenCount > 0) ?
                ((double) mobileOpenCount / totalOpens) * 100 : 0.0;
            this.tabletOpenRate = (tabletOpenCount != null && tabletOpenCount > 0) ?
                ((double) tabletOpenCount / totalOpens) * 100 : 0.0;
        }
    }

    /**
     * Calculate ROI.
     */
    public void calculateRoi() {
        if (cost != null && cost > 0 && revenue != null) {
            this.roi = ((revenue - cost) / cost) * 100;
        }
    }

    /**
     * Increment sent count.
     */
    public void incrementSent() {
        this.sentCount = (this.sentCount != null ? this.sentCount : 0) + 1;
    }

    /**
     * Increment delivered count.
     */
    public void incrementDelivered() {
        this.deliveredCount = (this.deliveredCount != null ? this.deliveredCount : 0) + 1;
    }

    /**
     * Increment bounce count.
     *
     * @param isHard true if hard bounce
     */
    public void incrementBounce(boolean isHard) {
        this.bouncedCount = (this.bouncedCount != null ? this.bouncedCount : 0) + 1;
        if (isHard) {
            this.hardBounceCount = (this.hardBounceCount != null ? this.hardBounceCount : 0) + 1;
        } else {
            this.softBounceCount = (this.softBounceCount != null ? this.softBounceCount : 0) + 1;
        }
    }

    /**
     * Increment open count.
     */
    public void incrementOpen() {
        this.openCount = (this.openCount != null ? this.openCount : 0) + 1;
    }

    /**
     * Increment unique open count.
     */
    public void incrementUniqueOpen() {
        this.uniqueOpenCount = (this.uniqueOpenCount != null ? this.uniqueOpenCount : 0) + 1;
    }

    /**
     * Increment click count.
     */
    public void incrementClick() {
        this.clickCount = (this.clickCount != null ? this.clickCount : 0) + 1;
    }

    /**
     * Increment unique click count.
     */
    public void incrementUniqueClick() {
        this.uniqueClickCount = (this.uniqueClickCount != null ? this.uniqueClickCount : 0) + 1;
    }

    /**
     * Increment unsubscribe count.
     */
    public void incrementUnsubscribe() {
        this.unsubscribeCount = (this.unsubscribeCount != null ? this.unsubscribeCount : 0) + 1;
    }

    /**
     * Increment complaint count.
     */
    public void incrementComplaint() {
        this.complaintCount = (this.complaintCount != null ? this.complaintCount : 0) + 1;
    }

    /**
     * Add a link metric.
     *
     * @param url   the URL
     * @param clicks the click count
     */
    public void addLinkMetric(String url, int clicks) {
        if (this.linkMetrics == null) {
            this.linkMetrics = new ArrayList<>();
        }
        LinkMetric metric = new LinkMetric();
        metric.setUrl(url);
        metric.setClicks(clicks);
        this.linkMetrics.add(metric);
    }

    /**
     * Add a geographic metric.
     *
     * @param country the country
     * @param opens   the open count
     */
    public void addGeoMetric(String country, int opens) {
        if (this.geoMetrics == null) {
            this.geoMetrics = new ArrayList<>();
        }
        GeoMetric metric = new GeoMetric();
        metric.setCountry(country);
        metric.setOpens(opens);
        this.geoMetrics.add(metric);
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
    }

    /**
     * Mark metrics as calculated.
     */
    public void markAsCalculated() {
        this.calculatedAt = Instant.now();
        this.version = String.valueOf(Instant.now().toEpochMilli());
        this.touch();
    }

    /**
     * Get health score (0-100).
     *
     * @return health score based on rates
     */
    public int getHealthScore() {
        int score = 100;

        // Deduct for high bounce rate (max 40 points)
        double bounceRate = this.bounceRate != null ? this.bounceRate : 0;
        score -= Math.min(40, (int) (bounceRate * 2));

        // Deduct for high unsubscribe rate (max 30 points)
        double unsubscribeRate = this.unsubscribeRate != null ? this.unsubscribeRate : 0;
        score -= Math.min(30, (int) (unsubscribeRate * 3));

        // Deduct for high complaint rate (max 30 points)
        double complaintRate = this.complaintRate != null ? this.complaintRate : 0;
        score -= Math.min(30, (int) (complaintRate * 10));

        return Math.max(0, score);
    }

    /**
     * Get engagement score (0-100).
     *
     * @return engagement score based on opens and clicks
     */
    public int getEngagementScore() {
        double openRate = this.openRate != null ? this.openRate : 0;
        double clickRate = this.clickRate != null ? this.clickRate : 0;
        double clickToOpen = this.clickToOpenRate != null ? this.clickToOpenRate : 0;

        return Math.min(100, (int) ((openRate * 0.4) + (clickRate * 0.4) + (clickToOpen * 0.2)));
    }

    /**
     * Get grade (A-F) based on performance.
     *
     * @return letter grade
     */
    public String getGrade() {
        int score = getEngagementScore();
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }

    /**
     * Check if metrics need recalculation.
     *
     * @param hoursThreshold hours since last calculation
     * @return true if needs recalculation
     */
    public boolean needsRecalculation(int hoursThreshold) {
        if (calculatedAt == null) {
            return true;
        }
        Instant threshold = Instant.now().minusSeconds(hoursThreshold * 3600L);
        return calculatedAt.isBefore(threshold);
    }
}
