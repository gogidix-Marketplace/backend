package com.gogidix.shared.infrastructure.services.communication.socialmedia.application.dto.response;

import com.gogidix.shared.infrastructure.services.communication.socialmedia.domain.model.SocialMediaPost;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for social media post response with domain-specific fields
 */
@Data
public class SocialMediaPostResponse {

    // Add a custom setter for domainSpecificFields to ensure it exists
    public void setDomainSpecificFields(Map<String, Object> domainSpecificFields) {
        this.domainSpecificFields = domainSpecificFields;
    }

    private Long id;
    private String tenantId;
    private String domainContext;
    private String postId;
    private String platformPostId;
    private String postType;
    private String status;
    private String content;
    private String mediaUrls;
    private String linkUrl;
    private String hashtags;
    private String mentions;
    private LocalDateTime scheduledAt;
    private LocalDateTime publishedAt;
    private Long likeCount;
    private Long shareCount;
    private Long commentCount;
    private Long viewCount;
    private Long clickCount;
    private String boostStatus;
    private Double boostBudget;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Map<String, Object> domainSpecificFields;

    /**
     * Convert entity to DTO
     */
    public static SocialMediaPostResponse fromEntity(SocialMediaPost post) {
        SocialMediaPostResponse response = new SocialMediaPostResponse();
        response.setId(post.getId() != null ? Long.parseLong(post.getId()) : null);
        response.setTenantId(post.getTenantId() != null ? post.getTenantId().getValue() : null);
        response.setDomainContext(post.getDomainContext());
        response.setPostId(post.getPostId());
        response.setPlatformPostId(post.getPlatformPostId());
        response.setPostType(post.getPostType() != null ? post.getPostType().name() : null);
        response.setStatus(post.getStatus() != null ? post.getStatus().name() : null);
        response.setContent(post.getContent());
        response.setMediaUrls(post.getMediaUrls());
        response.setLinkUrl(post.getLinkUrl());
        response.setHashtags(post.getHashtags());
        response.setMentions(post.getMentions());
        response.setScheduledAt(post.getScheduledAt());
        response.setPublishedAt(post.getPublishedAt());
        response.setLikeCount(post.getLikeCount());
        response.setShareCount(post.getShareCount());
        response.setCommentCount(post.getCommentCount());
        response.setViewCount(post.getViewCount());
        response.setClickCount(post.getClickCount());
        response.setBoostStatus(post.getBoostStatus() != null ? post.getBoostStatus().name() : null);
        response.setBoostBudget(post.getBoostBudget());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        return response;
    }
}
