package com.gogidix.shared.infrastructure.services.communication.socialmedia.domain.model;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;
import com.gogidix.shared.infrastructure.services.communication.socialmedia.domain.model.SocialMediaAccount;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Social Media Post MongoDB Document
 * Multi-tenant support with tenant isolation.
 */
@Document(collection = "social_media_posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialMediaPost {

    @Field("tenant_id")
    @Indexed
    private TenantId tenantId;

    @Id
    private String id;

    // Domain context for cross-domain identification
    @Indexed
    private String domainContext;

    @DBRef
    @Indexed
    private SocialMediaAccount socialMediaAccount;

    @Indexed
    private Long productId;

    @Indexed
    private Long campaignId;

    @NotBlank
    @Size(max = 100)
    @Indexed
    private String postId;

    @Size(max = 255)
    private String platformPostId;

    @NotNull
    @Indexed
    private PostType postType;

    @NotNull
    @Indexed
    private PostStatus status;

    @NotBlank
    private String content;

    @Size(max = 1000)
    private String mediaUrls;

    @Size(max = 500)
    private String linkUrl;

    private String hashtags;

    private String mentions;

    private LocalDateTime scheduledAt;

    @Indexed
    private LocalDateTime publishedAt;

    private LocalDateTime expiresAt;

    // Engagement metrics
    @Builder.Default
    private Long likeCount = 0L;

    @Builder.Default
    private Long shareCount = 0L;

    @Builder.Default
    private Long commentCount = 0L;

    @Builder.Default
    private Long viewCount = 0L;

    @Builder.Default
    private Long clickCount = 0L;

    // Boost/Promotion settings
    @Indexed
    private BoostStatus boostStatus = BoostStatus.NONE;

    private Double boostBudget;

    private LocalDateTime boostStartAt;

    private LocalDateTime boostEndAt;

    // Analytics and tracking
    @Builder.Default
    private Boolean conversionTracking = false;

    private String trackingPixels;

    private String customAttributes;

    @CreatedDate
    @Indexed
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Enums
    public enum PostType {
        TEXT,
        IMAGE,
        VIDEO,
        LINK,
        CAROUSEL,
        STORY,
        REEL,
        POLL
    }

    public enum PostStatus {
        DRAFT,
        SCHEDULED,
        PUBLISHED,
        FAILED,
        DELETED,
        EXPIRED
    }

    public enum BoostStatus {
        NONE,
        PENDING,
        ACTIVE,
        COMPLETED,
        FAILED
    }

    // Business methods
    public boolean isPublished() {
        return status == PostStatus.PUBLISHED;
    }

    public boolean isScheduled() {
        return status == PostStatus.SCHEDULED && scheduledAt != null;
    }

    public boolean isBoosted() {
        return boostStatus != BoostStatus.NONE;
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean shouldPublish() {
        return isScheduled() && scheduledAt.isBefore(LocalDateTime.now());
    }

    public List<String> getMediaUrlList() {
        // Parse mediaUrls JSON string to List
        return List.of(); // Placeholder
    }

    public void setMediaUrlList(List<String> urls) {
        // Convert List to JSON string
        this.mediaUrls = "[]"; // Placeholder
    }

    public List<String> getHashtagList() {
        // Parse hashtags string to List
        return List.of(); // Placeholder
    }

    public void setHashtagList(List<String> tags) {
        // Convert List to comma-separated string
        this.hashtags = String.join(",", tags);
    }

    public Map<String, Object> getCustomAttributes() {
        // Parse customAttributes JSON string to Map
        return Map.of(); // Placeholder
    }

    public void setCustomAttributes(Map<String, Object> attributes) {
        // Convert Map to JSON string
        this.customAttributes = "{}"; // Placeholder
    }

    public Long getTotalEngagement() {
        return likeCount + shareCount + commentCount;
    }

    public Double getEngagementRate(Long followerCount) {
        if (followerCount == null || followerCount == 0) {
            return 0.0;
        }
        return (getTotalEngagement().doubleValue() / followerCount) * 100;
    }

    // Explicit getters for safety
    public Double getBoostBudget() {
        return boostBudget;
    }

    public Long getFollowerCount() {
        return 0L; // Default implementation
    }
}
