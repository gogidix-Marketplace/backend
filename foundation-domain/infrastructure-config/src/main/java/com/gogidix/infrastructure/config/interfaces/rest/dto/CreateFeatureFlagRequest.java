package com.gogidix.infrastructure.config.interfaces.rest.dto;

import com.gogidix.infrastructure.config.domain.model.FeatureFlag;
import com.gogidix.infrastructure.config.domain.model.FeatureFlagCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DTO for creating a new feature flag.
 */
public record CreateFeatureFlagRequest(
        @NotBlank(message = "Tenant ID is required")
        String tenantId,

        @NotBlank(message = "Flag key is required")
        String flagKey,

        @NotBlank(message = "Name is required")
        String name,

        String description,

        @NotNull(message = "Enabled status is required")
        Boolean isEnabled,

        @NotNull(message = "Rollout strategy is required")
        FeatureFlag.RolloutStrategy rolloutStrategy,

        Integer rolloutPercentage,

        List<String> whitelistedUsers,

        List<FeatureFlagCondition> conditions,

        Boolean isSticky,

        Integer priority,

        Set<String> tags,

        String owner,

        Map<String, Object> metadata,

        LocalDateTime expiresAt,

        String changeReason
) {
}
