package com.gogidix.infrastructure.config.interfaces.rest.dto;

import com.gogidix.infrastructure.config.domain.model.FeatureFlag;

import java.time.LocalDateTime;

/**
 * DTO for feature flag evaluation result.
 */
public record FeatureFlagEvaluation(
        String flagKey,
        boolean enabled,
        String reason,
        FeatureFlag.RolloutStrategy rolloutStrategy,
        boolean isSticky,
        String userId,
        LocalDateTime evaluatedAt
) {
    // Alias method for compatibility
    public boolean isEnabled() {
        return enabled;
    }

    public static FeatureFlagEvaluationBuilder builder() {
        return new FeatureFlagEvaluationBuilder();
    }

    public static class FeatureFlagEvaluationBuilder {
        private String flagKey;
        private boolean enabled;
        private String reason;
        private FeatureFlag.RolloutStrategy rolloutStrategy;
        private boolean isSticky;
        private String userId;
        private LocalDateTime evaluatedAt;

        public FeatureFlagEvaluationBuilder flagKey(String flagKey) {
            this.flagKey = flagKey;
            return this;
        }

        public FeatureFlagEvaluationBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public FeatureFlagEvaluationBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public FeatureFlagEvaluationBuilder rolloutStrategy(FeatureFlag.RolloutStrategy rolloutStrategy) {
            this.rolloutStrategy = rolloutStrategy;
            return this;
        }

        public FeatureFlagEvaluationBuilder isSticky(boolean isSticky) {
            this.isSticky = isSticky;
            return this;
        }

        public FeatureFlagEvaluationBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public FeatureFlagEvaluationBuilder evaluatedAt(LocalDateTime evaluatedAt) {
            this.evaluatedAt = evaluatedAt;
            return this;
        }

        public FeatureFlagEvaluation build() {
            return new FeatureFlagEvaluation(flagKey, enabled, reason, rolloutStrategy, isSticky, userId, evaluatedAt);
        }
    }
}
