package com.gogidix.digitalmarketing.socialmedia.domain.model;

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
 * SocialEngagement - Engagement metrics for social posts
 *
 * <p>Stores engagement metrics (likes, shares, comments, views, clicks)
 * tracked for social media posts across platforms.</p>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "social_engagement")
@TypeAlias("social_engagement")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "engagement_tenant_post_idx", def = "{'tenantId': 1, 'postId': 1}")
@CompoundIndex(name = "engagement_tenant_date_idx", def = "{'tenantId': 1, 'engagementDate': -1}")
public class SocialEngagement extends BaseEntity {

    /**
     * Associated post ID
     */
    @Indexed
    private String postId;

    /**
     * External post ID on platform
     */
    private String externalPostId;

    /**
     * Platform for this engagement
     */
    @Indexed
    private String platform;

    /**
     * Account ID
     */
    @Indexed
    private String accountId;

    /**
     * Engagement date (when metrics were captured)
     */
    @Indexed
    private Instant engagementDate;

    /**
     * Likes count
     */
    @Builder.Default
    private Long likes = 0L;

    /**
     * Loves/reactions count
     */
    @Builder.Default
    private Long loves = 0L;

    /**
     * Shares/retweets count
     */
    @Builder.Default
    private Long shares = 0L;

    /**
     * Comments count
     */
    @Builder.Default
    private Long comments = 0L;

    /**
     * Views/impressions count
     */
    @Builder.Default
    private Long views = 0L;

    /**
     * Unique views count
     */
    @Builder.Default
    private Long uniqueViews = 0L;

    /**
     * Clicks count
     */
    @Builder.Default
    private Long clicks = 0L;

    /**
     * Link clicks count
     */
    @Builder.Default
    private Long linkClicks = 0L;

    /**
     * Saves/bookmarks count
     */
    @Builder.Default
    private Long saves = 0L;

    /**
     * Mentions count
     */
    @Builder.Default
    private Long mentions = 0L;

    /**
     * Followers gained
     */
    @Builder.Default
    private Long followersGained = 0L;

    /**
     * Total engagement (sum of all interactions)
     */
    @Builder.Default
    private Long totalEngagement = 0L;

    /**
     * Engagement rate (percentage)
     */
    private BigDecimal engagementRate;

    /**
     * Reach (unique users who saw post)
     */
    @Builder.Default
    private Long reach = 0L;

    /**
     * Impressions (total times post displayed)
     */
    @Builder.Default
    private Long impressions = 0L;

    /**
     * Video views count (if video)
     */
    @Builder.Default
    private Long videoViews = 0L;

    /**
     * Video completion count
     */
    @Builder.Default
    private Long videoCompletions = 0L;

    /**
     * Video average watch time (seconds)
     */
    @Builder.Default
    private Long avgWatchTime = 0L;

    /**
     * Sentiment score (negative to positive)
     */
    private BigDecimal sentimentScore;

    /**
     * Sentiment category (POSITIVE, NEUTRAL, NEGATIVE)
     */
    private String sentiment;

    /**
     * Is latest snapshot flag
     */
    @Builder.Default
    private Boolean isLatest = true;

    /**
     * Previous engagement ID (for delta tracking)
     */
    private String previousEngagementId;

    /**
     * Additional metrics
     */
    private Map<String, Object> additionalMetrics;

    /**
     * Data source (API, WEBHOOK, MANUAL)
     */
    @Builder.Default
    private String dataSource = "API";

    /**
     * Sync status (PENDING, SYNCED, ERROR)
     */
    @Builder.Default
    private String syncStatus = "SYNCED";

    /**
     * Last error message
     */
    private String lastError;

    /**
     * Create a new SocialEngagement.
     *
     * @param tenantId the tenant ID
     * @param postId the post ID
     * @param platform the platform
     * @param engagementDate the engagement date
     */
    public SocialEngagement(String tenantId, String postId, String platform, Instant engagementDate) {
        super(tenantId);
        this.postId = postId;
        this.platform = platform;
        this.engagementDate = engagementDate;
        this.isLatest = true;
        this.dataSource = "API";
        this.syncStatus = "SYNCED";
        initializeDefaults();
    }

    /**
     * Initialize default values.
     */
    private void initializeDefaults() {
        this.likes = 0L;
        this.loves = 0L;
        this.shares = 0L;
        this.comments = 0L;
        this.views = 0L;
        this.uniqueViews = 0L;
        this.clicks = 0L;
        this.linkClicks = 0L;
        this.saves = 0L;
        this.mentions = 0L;
        this.followersGained = 0L;
        this.totalEngagement = 0L;
        this.reach = 0L;
        this.impressions = 0L;
        this.videoViews = 0L;
        this.videoCompletions = 0L;
        this.avgWatchTime = 0L;
    }

    /**
     * Calculate total engagement.
     *
     * @return total engagement count
     */
    public long calculateTotalEngagement() {
        this.totalEngagement = likes + loves + shares + comments + clicks + saves + mentions;
        return this.totalEngagement;
    }

    /**
     * Calculate engagement rate.
     *
     * Formula: (total_engagement / reach) * 100
     *
     * @return engagement rate as percentage
     */
    public BigDecimal calculateEngagementRate() {
        if (reach != null && reach > 0) {
            long total = calculateTotalEngagement();
            this.engagementRate = BigDecimal.valueOf(total)
                .divide(BigDecimal.valueOf(reach), 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        } else {
            this.engagementRate = BigDecimal.ZERO;
        }
        return this.engagementRate;
    }

    /**
     * Calculate click-through rate.
     *
     * @return CTR as percentage
     */
    public BigDecimal calculateCTR() {
        if (impressions != null && impressions > 0 && linkClicks != null) {
            return BigDecimal.valueOf(linkClicks)
                .divide(BigDecimal.valueOf(impressions), 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    /**
     * Calculate video completion rate.
     *
     * @return completion rate as percentage
     */
    public BigDecimal calculateCompletionRate() {
        if (videoViews != null && videoViews > 0 && videoCompletions != null) {
            return BigDecimal.valueOf(videoCompletions)
                .divide(BigDecimal.valueOf(videoViews), 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    /**
     * Update all metrics.
     *
     * @param likes likes
     * @param shares shares
     * @param comments comments
     * @param views views
     * @param clicks clicks
     * @param reach reach
     * @param impressions impressions
     */
    public void updateMetrics(Long likes, Long shares, Long comments, Long views,
                              Long clicks, Long reach, Long impressions) {
        this.likes = likes != null ? likes : 0L;
        this.shares = shares != null ? shares : 0L;
        this.comments = comments != null ? comments : 0L;
        this.views = views != null ? views : 0L;
        this.clicks = clicks != null ? clicks : 0L;
        this.reach = reach != null ? reach : 0L;
        this.impressions = impressions != null ? impressions : 0L;
        calculateTotalEngagement();
        calculateEngagementRate();
        this.touch();
    }

    /**
     * Increment a metric.
     *
     * @param metricName metric name
     * @param value value to add
     */
    public void incrementMetric(String metricName, long value) {
        switch (metricName.toLowerCase()) {
            case "likes" -> this.likes += value;
            case "loves" -> this.loves += value;
            case "shares" -> this.shares += value;
            case "comments" -> this.comments += value;
            case "views" -> this.views += value;
            case "uniqueviews" -> this.uniqueViews += value;
            case "clicks" -> this.clicks += value;
            case "linkclicks" -> this.linkClicks += value;
            case "saves" -> this.saves += value;
            case "mentions" -> this.mentions += value;
            case "followersgained" -> this.followersGained += value;
            case "reach" -> this.reach += value;
            case "impressions" -> this.impressions += value;
            case "videoviews" -> this.videoViews += value;
            case "videocompletions" -> this.videoCompletions += value;
        }
        calculateTotalEngagement();
        calculateEngagementRate();
        this.touch();
    }

    /**
     * Calculate delta from previous engagement.
     *
     * @param previous previous engagement snapshot
     * @return delta map
     */
    public Map<String, Long> calculateDelta(SocialEngagement previous) {
        Map<String, Long> delta = new HashMap<>();
        if (previous != null) {
            delta.put("likes", this.likes - previous.likes);
            delta.put("shares", this.shares - previous.shares);
            delta.put("comments", this.comments - previous.comments);
            delta.put("views", this.views - previous.views);
            delta.put("clicks", this.clicks - previous.clicks);
            delta.put("totalEngagement", this.totalEngagement - previous.totalEngagement);
        }
        return delta;
    }

    /**
     * Determine sentiment based on score.
     *
     * @param score sentiment score (-1 to 1)
     */
    public void setSentiment(BigDecimal score) {
        this.sentimentScore = score;
        if (score.compareTo(BigDecimal.valueOf(0.3)) >= 0) {
            this.sentiment = "POSITIVE";
        } else if (score.compareTo(BigDecimal.valueOf(-0.3)) <= 0) {
            this.sentiment = "NEGATIVE";
        } else {
            this.sentiment = "NEUTRAL";
        }
    }

    /**
     * Add additional metric.
     *
     * @param key metric key
     * @param value metric value
     */
    public void addAdditionalMetric(String key, Object value) {
        if (this.additionalMetrics == null) {
            this.additionalMetrics = new HashMap<>();
        }
        this.additionalMetrics.put(key, value);
    }

    /**
     * Get additional metric.
     *
     * @param key metric key
     * @return metric value or null
     */
    public Object getAdditionalMetric(String key) {
        if (this.additionalMetrics == null) {
            return null;
        }
        return this.additionalMetrics.get(key);
    }

    /**
     * Mark as latest snapshot.
     */
    public void markAsLatest() {
        this.isLatest = true;
        this.touch();
    }

    /**
     * Mark as not latest (superseded by newer snapshot).
     */
    public void markAsSuperseded() {
        this.isLatest = false;
        this.touch();
    }

    /**
     * Check if this is a positive performing post.
     *
     * @return true if performance is good
     */
    public boolean isPositivePerforming() {
        BigDecimal threshold = BigDecimal.valueOf(2.0); // 2% engagement rate
        return engagementRate != null && engagementRate.compareTo(threshold) >= 0;
    }

    /**
     * Get performance rating.
     *
     * @return rating (EXCELLENT, GOOD, AVERAGE, POOR)
     */
    public String getPerformanceRating() {
        if (engagementRate == null) {
            return "UNKNOWN";
        }
        if (engagementRate.compareTo(BigDecimal.valueOf(5.0)) >= 0) {
            return "EXCELLENT";
        } else if (engagementRate.compareTo(BigDecimal.valueOf(2.0)) >= 0) {
            return "GOOD";
        } else if (engagementRate.compareTo(BigDecimal.valueOf(0.5)) >= 0) {
            return "AVERAGE";
        } else {
            return "POOR";
        }
    }
}
