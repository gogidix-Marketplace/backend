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

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * SocialPost - Social media post with scheduling
 *
 * <p>Represents a social media post that can be scheduled, published, or drafted.
 * Supports multiple platforms with platform-specific content variations.</p>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "social_posts")
@TypeAlias("social_post")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "post_tenant_status_idx", def = "{'tenantId': 1, 'status': 1, 'scheduledAt': 1}")
@CompoundIndex(name = "post_tenant_account_idx", def = "{'tenantId': 1, 'accountId': 1, 'createdAt': -1}")
public class SocialPost extends BaseEntity {

    /**
     * Account ID to post to
     */
    @Indexed
    private String accountId;

    /**
     * Platform for this post
     */
    @Indexed
    private String platform;

    /**
     * Post content
     */
    private String content;

    /**
     * Media URLs (images, videos)
     */
    private java.util.List<String> mediaUrls;

    /**
     * Media types (IMAGE, VIDEO, GIF, DOCUMENT)
     */
    private java.util.List<String> mediaTypes;

    /**
     * Post link URL
     */
    private String linkUrl;

    /**
     * Link title/preview
     */
    private String linkTitle;

    /**
     * Link description/preview
     */
    private String linkDescription;

    /**
     * Link image/preview
     */
    private String linkImageUrl;

    /**
     * Post status (DRAFT, SCHEDULED, PUBLISHING, PUBLISHED, FAILED, CANCELLED)
     */
    @Indexed
    private String status;

    /**
     * Publish priority (LOW, NORMAL, HIGH, URGENT)
     */
    @Builder.Default
    private String priority = "NORMAL";

    /**
     * Scheduled publish time
     */
    @Indexed
    private Instant scheduledAt;

    /**
     * Actual publish time
     */
    private Instant publishedAt;

    /**
     * Post visibility (PUBLIC, FRIENDS, PRIVATE)
     */
    @Builder.Default
    private String visibility = "PUBLIC";

    /**
     * Whether to allow comments
     */
    @Builder.Default
    private Boolean allowComments = true;

    /**
     * Content library ID if created from library
     */
    private String contentLibraryId;

    /**
     * Campaign ID if part of campaign
     */
    private String campaignId;

    /**
     * Post tags/hashtags
     */
    private java.util.List<String> tags;

    /**
     * Post categories
     */
    private java.util.List<String> categories;

    /**
     * Target audience criteria
     */
    private Map<String, Object> targetAudience;

    /**
     * Platform-specific post options
     */
    private Map<String, Object> platformOptions;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * External post ID after publishing
     */
    private String externalPostId;

    /**
     * External post URL
     */
    private String externalPostUrl;

    /**
     * Post creation method (MANUAL, AUTOMATED, AI_GENERATED)
     */
    @Builder.Default
    private String creationMethod = "MANUAL";

    /**
     * Number of retries for failed posts
     */
    @Builder.Default
    private Integer retryCount = 0;

    /**
     * Maximum retry attempts
     */
    @Builder.Default
    private Integer maxRetries = 3;

    /**
     * Last error message
     */
    private String lastError;

    /**
     * Estimated engagement metrics
     */
    private Long estimatedReach;

    /**
     * Approval status (DRAFT, PENDING_APPROVAL, APPROVED, REJECTED)
     */
    @Builder.Default
    private String approvalStatus = "DRAFT";

    /**
     * Approved by user ID
     */
    private String approvedBy;

    /**
     * Approved at timestamp
     */
    private Instant approvedAt;

    /**
     * Rejection reason
     */
    private String rejectionReason;

    /**
     * Create a new SocialPost.
     *
     * @param tenantId the tenant ID
     * @param accountId the account ID
     * @param platform the platform
     * @param content the post content
     */
    public SocialPost(String tenantId, String accountId, String platform, String content) {
        super(tenantId);
        this.accountId = accountId;
        this.platform = platform;
        this.content = content;
        this.status = "DRAFT";
        this.priority = "NORMAL";
        this.visibility = "PUBLIC";
        this.allowComments = true;
        this.creationMethod = "MANUAL";
        this.retryCount = 0;
        this.maxRetries = 3;
        this.approvalStatus = "DRAFT";
    }

    /**
     * Schedule the post for publishing.
     *
     * @param scheduledAt scheduled time
     */
    public void schedule(Instant scheduledAt) {
        this.scheduledAt = scheduledAt;
        this.status = "SCHEDULED";
        this.touch();
    }

    /**
     * Mark post as publishing.
     */
    public void markAsPublishing() {
        this.status = "PUBLISHING";
        this.touch();
    }

    /**
     * Mark post as published.
     *
     * @param externalPostId platform post ID
     * @param externalPostUrl platform post URL
     */
    public void markAsPublished(String externalPostId, String externalPostUrl) {
        this.status = "PUBLISHED";
        this.publishedAt = Instant.now();
        this.externalPostId = externalPostId;
        this.externalPostUrl = externalPostUrl;
        this.touch();
    }

    /**
     * Mark post as failed.
     *
     * @param error error message
     */
    public void markAsFailed(String error) {
        this.status = "FAILED";
        this.lastError = error;
        this.retryCount++;
        this.touch();
    }

    /**
     * Check if post can be retried.
     *
     * @return true if retries available
     */
    public boolean canRetry() {
        return this.retryCount < this.maxRetries;
    }

    /**
     * Reset for retry.
     */
    public void resetForRetry() {
        this.status = "SCHEDULED";
        this.lastError = null;
        this.touch();
    }

    /**
     * Cancel the post.
     */
    public void cancel() {
        if ("SCHEDULED".equals(this.status) || "PUBLISHING".equals(this.status)) {
            this.status = "CANCELLED";
            this.touch();
        }
    }

    /**
     * Check if post is published.
     *
     * @return true if published
     */
    public boolean isPublished() {
        return "PUBLISHED".equals(this.status);
    }

    /**
     * Check if post is scheduled.
     *
     * @return true if scheduled
     */
    public boolean isScheduled() {
        return "SCHEDULED".equals(this.status);
    }

    /**
     * Check if post is draft.
     *
     * @return true if draft
     */
    public boolean isDraft() {
        return "DRAFT".equals(this.status);
    }

    /**
     * Check if post is due for publishing now.
     *
     * @return true if due
     */
    public boolean isDue() {
        return "SCHEDULED".equals(this.status) && scheduledAt != null && !Instant.now().isBefore(scheduledAt);
    }

    /**
     * Approve post for publishing.
     *
     * @param approvedBy user approving
     */
    public void approve(String approvedBy) {
        this.approvalStatus = "APPROVED";
        this.approvedBy = approvedBy;
        this.approvedAt = Instant.now();
        this.touch();
    }

    /**
     * Reject post.
     *
     * @param reason rejection reason
     */
    public void reject(String reason) {
        this.approvalStatus = "REJECTED";
        this.rejectionReason = reason;
        this.touch();
    }

    /**
     * Check if approved for publishing.
     *
     * @return true if approved
     */
    public boolean isApproved() {
        return "APPROVED".equals(this.approvalStatus) || "DRAFT".equals(this.approvalStatus);
    }

    /**
     * Check if post has media.
     *
     * @return true if has media
     */
    public boolean hasMedia() {
        return mediaUrls != null && !mediaUrls.isEmpty();
    }

    /**
     * Get media count.
     *
     * @return number of media items
     */
    public int getMediaCount() {
        return mediaUrls != null ? mediaUrls.size() : 0;
    }

    /**
     * Add tag to post.
     *
     * @param tag tag to add
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new java.util.ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Add metadata to post.
     *
     * @param key the metadata key
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
     * Get content length.
     *
     * @return content length
     */
    public int getContentLength() {
        return content != null ? content.length() : 0;
    }

    /**
     * Check if content exceeds platform limit.
     *
     * @param limit character limit
     * @return true if exceeds limit
     */
    public boolean exceedsLimit(int limit) {
        return getContentLength() > limit;
    }
}
