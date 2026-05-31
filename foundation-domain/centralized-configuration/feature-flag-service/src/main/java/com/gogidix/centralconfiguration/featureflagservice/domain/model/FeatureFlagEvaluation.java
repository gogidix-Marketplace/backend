package com.gogidix.centralconfiguration.featureflagservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Value object representing a feature flag evaluation result.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureFlagEvaluation {

    private String flagKey;
    private Boolean enabled;
    private String reason;
    private String userId;
    private String tenantId;
    private Integer rolloutPercentage;
    private RolloutStrategy strategy;
    private LocalDateTime evaluatedAt;

    public static FeatureFlagEvaluation enabled(String flagKey, String userId, String tenantId) {
        return FeatureFlagEvaluation.builder()
                .flagKey(flagKey)
                .enabled(true)
                .reason("Feature flag is enabled")
                .userId(userId)
                .tenantId(tenantId)
                .evaluatedAt(LocalDateTime.now())
                .build();
    }

    public static FeatureFlagEvaluation disabled(String flagKey, String userId, String tenantId, String reason) {
        return FeatureFlagEvaluation.builder()
                .flagKey(flagKey)
                .enabled(false)
                .reason(reason)
                .userId(userId)
                .tenantId(tenantId)
                .evaluatedAt(LocalDateTime.now())
                .build();
    }
}
