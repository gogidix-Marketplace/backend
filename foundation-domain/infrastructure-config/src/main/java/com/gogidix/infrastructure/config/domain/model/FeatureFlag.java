package com.gogidix.infrastructure.config.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Domain model for feature flags.
 *
 * <p>Feature flags enable dynamic feature toggling with the following capabilities:</p>
 * <ul>
 *   <li>Percentage-based rollout</li>
 *   <li>User whitelisting</li>
 *   <li>Gradual rollout strategies</li>
 *   <li>Condition-based evaluation</li>
 *   <li>Tenant isolation</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "feature_flags")
public class FeatureFlag {

    /**
     * Unique identifier for the feature flag.
     */
    @Id
    private String id;

    /**
     * Tenant identifier for multi-tenancy support.
     */
    @Indexed
    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    /**
     * Unique key for the feature flag within tenant scope.
     */
    @Indexed
    @NotBlank(message = "Flag key is required")
    private String flagKey;

    /**
     * Human-readable name for the feature flag.
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * Detailed description of what the feature flag controls.
     */
    private String description;

    /**
     * Whether the feature flag is currently enabled.
     */
    @NotNull(message = "Enabled status is required")
    private Boolean isEnabled;

    /**
     * Type of rollout strategy.
     */
    @NotNull(message = "Rollout strategy is required")
    private RolloutStrategy rolloutStrategy;

    /**
     * Percentage of users who should have the feature enabled (0-100).
     * Only applicable for PERCENTAGE rollout strategy.
     */
    @Builder.Default
    private Integer rolloutPercentage = 0;

    /**
     * List of user IDs who should have the feature enabled.
     * Only applicable for WHITELIST rollout strategy.
     */
    private List<String> whitelistedUsers;

    /**
     * Conditions for evaluating the feature flag.
     * Only applicable for CONDITIONAL rollout strategy.
     */
    private List<FeatureFlagCondition> conditions;

    /**
     * Whether user assignments should be sticky (consistent across requests).
     */
    @Builder.Default
    private boolean isSticky = true;

    /**
     * Priority for evaluating this flag (higher = evaluated first).
     */
    @Builder.Default
    private Integer priority = 0;

    /**
     * Tags for categorization and search.
     */
    private Set<String> tags;

    /**
     * Owner/team responsible for this feature flag.
     */
    private String owner;

    /**
     * User who created this feature flag.
     */
    private String createdBy;

    /**
     * User who last updated this feature flag.
     */
    private String lastUpdatedBy;

    /**
     * Current version number.
     */
    @Builder.Default
    private Integer version = 1;

    /**
     * Metadata associated with this feature flag.
     */
    private Map<String, Object> metadata;

    /**
     * Timestamp when this feature flag was created.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * Timestamp when this feature flag was last modified.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Timestamp until which this flag is valid.
     */
    private LocalDateTime expiresAt;

    /**
     * Timestamp when this flag was last enabled/disabled.
     */
    private LocalDateTime lastToggledAt;

    /**
     * User who last enabled/disabled this flag.
     */
    private String lastToggledBy;

    /**
     * Rollout strategy enumeration.
     */
    public enum RolloutStrategy {
        /**
         * All users get the same value based on isEnabled.
         */
        ALL_USERS,

        /**
         * Percentage of users get the feature enabled.
         */
        PERCENTAGE,

        /**
         * Only whitelisted users get the feature enabled.
         */
        WHITELIST,

        /**
         * Gradual rollout based on percentage over time.
         */
        GRADUAL,

        /**
         * Conditions-based evaluation.
         */
        CONDITIONAL,

        /**
         * A/B testing variant assignment.
         */
        AB_TEST
    }

    /**
     * Evaluates if the feature flag is enabled for a specific user.
     *
     * @param userId The user ID to evaluate
     * @param context Additional context for evaluation
     * @return true if the feature is enabled for the user
     */
    public boolean isEnabledForUser(String userId, Map<String, Object> context) {
        // Global flag is disabled
        if (!isEnabled) {
            return false;
        }

        // Flag has expired
        if (expiresAt != null && LocalDateTime.now().isAfter(expiresAt)) {
            return false;
        }

        return switch (rolloutStrategy) {
            case ALL_USERS -> true;

            case PERCENTAGE -> {
                if (rolloutPercentage == null || rolloutPercentage <= 0) {
                    yield false;
                }
                if (rolloutPercentage >= 100) {
                    yield true;
                }

                // Consistent hash based on user ID for sticky assignments
                int hash = Math.abs((userId + flagKey).hashCode() % 100);
                yield hash < rolloutPercentage;
            }

            case WHITELIST -> whitelistedUsers != null && whitelistedUsers.contains(userId);

            case CONDITIONAL -> evaluateConditions(context);

            case GRADUAL -> evaluateGradualRollout(userId, context);

            case AB_TEST -> evaluateABTest(userId, context);
        };
    }

    /**
     * Evaluates conditional feature flag based on conditions.
     */
    private boolean evaluateConditions(Map<String, Object> context) {
        if (conditions == null || conditions.isEmpty()) {
            return true;
        }

        // All conditions must be satisfied (AND logic)
        return conditions.stream()
                .allMatch(condition -> condition.evaluate(context));
    }

    /**
     * Evaluates gradual rollout strategy.
     */
    private boolean evaluateGradualRollout(String userId, Map<String, Object> context) {
        // Similar to PERCENTAGE but can consider time-based factors
        if (rolloutPercentage == null || rolloutPercentage <= 0) {
            return false;
        }

        // Get time factor from context (0-100 based on time since start)
        int timeFactor = (Integer) context.getOrDefault("timeFactor", 0);

        // User must be within both percentage and time factor
        int hash = Math.abs((userId + flagKey).hashCode() % 100);
        return hash < Math.min(rolloutPercentage, timeFactor);
    }

    /**
     * Evaluates A/B test assignment.
     */
    private boolean evaluateABTest(String userId, Map<String, Object> context) {
        // Assign users to variants based on hash
        int hash = Math.abs((userId + flagKey).hashCode() % 100);
        int threshold = rolloutPercentage != null ? rolloutPercentage : 50;
        return hash < threshold;
    }

    /**
     * Increments the version number.
     */
    public void incrementVersion() {
        this.version = (this.version != null ? this.version : 0) + 1;
    }

    /**
     * Checks if the flag is currently active (enabled and not expired).
     */
    public boolean isActive() {
        return isEnabled && (expiresAt == null || LocalDateTime.now().isBefore(expiresAt));
    }
}
