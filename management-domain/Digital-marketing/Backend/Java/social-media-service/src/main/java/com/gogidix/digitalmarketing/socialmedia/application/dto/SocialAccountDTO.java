package com.gogidix.digitalmarketing.socialmedia.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * DTO for SocialAccount
 */
@Schema(description = "Social media account representation")
public record SocialAccountDTO(

    @Schema(description = "Account ID")
    String id,

    @Schema(description = "Tenant ID")
    String tenantId,

    @Schema(description = "Platform type (FACEBOOK, TWITTER, INSTAGRAM, LINKEDIN, TIKTOK)")
    @NotBlank(message = "Platform is required")
    @Pattern(regexp = "FACEBOOK|TWITTER|INSTAGRAM|LINKEDIN|TIKTOK|YOUTUBE|PINTEREST",
             message = "Invalid platform type")
    String platform,

    @Schema(description = "Account ID on the platform")
    @NotBlank(message = "Account ID is required")
    String accountId,

    @Schema(description = "Account username")
    String username,

    @Schema(description = "Display name")
    String displayName,

    @Schema(description = "Profile image URL")
    String profileImageUrl,

    @Schema(description = "Profile URL")
    String profileUrl,

    @Schema(description = "Account status (ACTIVE, INACTIVE, EXPIRED, SUSPENDED, ERROR)")
    String status,

    @Schema(description = "Connection status (CONNECTED, DISCONNECTED, PENDING, FAILED)")
    String connectionStatus,

    @Schema(description = "Number of followers")
    Long followerCount,

    @Schema(description = "Number of following")
    Long followingCount,

    @Schema(description = "Number of posts")
    Long postCount,

    @Schema(description = "Is account verified")
    Boolean verified,

    @Schema(description = "Is primary account for platform")
    Boolean primary,

    @Schema(description = "Account capabilities")
    List<String> capabilities,

    @Schema(description = "Platform-specific metadata")
    Map<String, Object> metadata,

    @Schema(description = "Last sync timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant lastSyncedAt,

    @Schema(description = "Health score (0-100)")
    Integer healthScore,

    @Schema(description = "Created at timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant createdAt,

    @Schema(description = "Updated at timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant updatedAt
) {
}
