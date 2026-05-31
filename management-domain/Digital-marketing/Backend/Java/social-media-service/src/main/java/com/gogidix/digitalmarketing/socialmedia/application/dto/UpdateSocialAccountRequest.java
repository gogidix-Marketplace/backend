package com.gogidix.digitalmarketing.socialmedia.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Map;

/**
 * Request DTO for updating a social account
 */
@Schema(description = "Request to update an existing social media account")
public record UpdateSocialAccountRequest(

    @Schema(description = "Display name")
    String displayName,

    @Schema(description = "Profile image URL")
    String profileImageUrl,

    @Schema(description = "Profile URL")
    String profileUrl,

    @Schema(description = "Account status (ACTIVE, INACTIVE, EXPIRED, SUSPENDED, ERROR)")
    @Pattern(regexp = "ACTIVE|INACTIVE|EXPIRED|SUSPENDED|ERROR",
             message = "Invalid status")
    String status,

    @Schema(description = "Is primary account for platform")
    Boolean primary,

    @Schema(description = "Account capabilities")
    List<String> capabilities,

    @Schema(description = "Platform-specific metadata")
    Map<String, Object> metadata
) {
}
