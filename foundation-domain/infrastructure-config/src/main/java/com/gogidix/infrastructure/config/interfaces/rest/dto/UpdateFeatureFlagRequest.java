package com.gogidix.infrastructure.config.interfaces.rest.dto;

import com.gogidix.infrastructure.config.domain.model.FeatureFlag;
import com.gogidix.infrastructure.config.domain.model.FeatureFlagCondition;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DTO for updating an existing feature flag.
 */
public record UpdateFeatureFlagRequest(
        String name,

        String description,

        Boolean isEnabled,

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
