package com.gogidix.digitalmarketing.socialmedia.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Map;

/**
 * Request DTO for creating a social account
 */
@Schema(description = "Request to create a new social media account connection")
public record CreateSocialAccountRequest(

    @Schema(description = "Platform type (FACEBOOK, TWITTER, INSTAGRAM, LINKEDIN, TIKTOK)")
    @NotBlank(message = "Platform is required")
    @Pattern(regexp = "FACEBOOK|TWITTER|INSTAGRAM|LINKEDIN|TIKTOK|YOUTUBE|PINTEREST",
             message = "Invalid platform type")
    String platform,

    @Schema(description = "Account ID on the platform")
    @NotBlank(message = "Account ID is required")
    String accountId,

    @Schema(description = "Account username")
    @NotBlank(message = "Username is required")
    String username,

    @Schema(description = "Display name")
    String displayName,

    @Schema(description = "Profile image URL")
    String profileImageUrl,

    @Schema(description = "Profile URL")
    String profileUrl,

    @Schema(description = "OAuth access token (will be encrypted)")
    String accessToken,

    @Schema(description = "OAuth refresh token (will be encrypted)")
    String refreshToken,

    @Schema(description = "Token expiration timestamp")
    Long tokenExpiresAtEpochSeconds,

    @Schema(description = "Is primary account for platform")
    Boolean primary,

    @Schema(description = "Account capabilities")
    List<String> capabilities,

    @Schema(description = "Platform-specific metadata")
    Map<String, Object> metadata
) {
}
