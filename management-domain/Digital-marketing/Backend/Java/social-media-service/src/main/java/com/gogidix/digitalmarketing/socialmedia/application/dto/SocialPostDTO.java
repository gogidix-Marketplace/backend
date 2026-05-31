package com.gogidix.digitalmarketing.socialmedia.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Map;

/**
 * DTO for SocialPost
 */
@Schema(description = "Social media post representation")
public record SocialPostDTO(

    @Schema(description = "Post ID")
    String id,

    @Schema(description = "Tenant ID")
    String tenantId,

    @Schema(description = "Account ID to post to")
    String accountId,

    @Schema(description = "Platform for this post")
    @NotBlank(message = "Platform is required")
    @Pattern(regexp = "FACEBOOK|TWITTER|INSTAGRAM|LINKEDIN|TIKTOK|YOUTUBE|PINTEREST",
             message = "Invalid platform type")
    String platform,

    @Schema(description = "Post content")
    String content,

    @Schema(description = "Media URLs (images, videos)")
    List<String> mediaUrls,

    @Schema(description = "Media types (IMAGE, VIDEO, GIF, DOCUMENT)")
    List<String> mediaTypes,

    @Schema(description = "Post link URL")
    String linkUrl,

    @Schema(description = "Link title/preview")
    String linkTitle,

    @Schema(description = "Link description/preview")
    String linkDescription,

    @Schema(description = "Link image/preview")
    String linkImageUrl,

    @Schema(description = "Post status (DRAFT, SCHEDULED, PUBLISHING, PUBLISHED, FAILED, CANCELLED)")
    String status,

    @Schema(description = "Publish priority (LOW, NORMAL, HIGH, URGENT)")
    String priority,

    @Schema(description = "Scheduled publish time")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    java.time.Instant scheduledAt,

    @Schema(description = "Actual publish time")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    java.time.Instant publishedAt,

    @Schema(description = "Post visibility (PUBLIC, FRIENDS, PRIVATE)")
    String visibility,

    @Schema(description = "Whether to allow comments")
    Boolean allowComments,

    @Schema(description = "Content library ID if created from library")
    String contentLibraryId,

    @Schema(description = "Campaign ID if part of campaign")
    String campaignId,

    @Schema(description = "Post tags/hashtags")
    List<String> tags,

    @Schema(description = "Post categories")
    List<String> categories,

    @Schema(description = "Target audience criteria")
    Map<String, Object> targetAudience,

    @Schema(description = "Platform-specific post options")
    Map<String, Object> platformOptions,

    @Schema(description = "External post ID after publishing")
    String externalPostId,

    @Schema(description = "External post URL")
    String externalPostUrl,

    @Schema(description = "Post creation method (MANUAL, AUTOMATED, AI_GENERATED)")
    String creationMethod,

    @Schema(description = "Approval status (DRAFT, PENDING_APPROVAL, APPROVED, REJECTED)")
    String approvalStatus,

    @Schema(description = "Approved by user ID")
    String approvedBy,

    @Schema(description = "Approved at timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    java.time.Instant approvedAt,

    @Schema(description = "Created at timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    java.time.Instant createdAt,

    @Schema(description = "Updated at timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    java.time.Instant updatedAt
) {
}
