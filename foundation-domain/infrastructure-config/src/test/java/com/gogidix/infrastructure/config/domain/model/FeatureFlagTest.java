package com.gogidix.infrastructure.config.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FeatureFlag domain model.
 */
@DisplayName("FeatureFlag Tests")
class FeatureFlagTest {

    @Test
    @DisplayName("Should create valid feature flag")
    void shouldCreateValidFeatureFlag() {
        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("new-feature")
                .name("New Feature")
                .description("A new feature flag")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.ALL_USERS)
                .build();

        assertNotNull(flag);
        assertEquals("tenant-1", flag.getTenantId());
        assertEquals("new-feature", flag.getFlagKey());
        assertEquals("New Feature", flag.getName());
        assertTrue(flag.getIsEnabled());
    }

    @Test
    @DisplayName("Should enable feature for all users when strategy is ALL_USERS")
    void shouldEnableFeatureForAllUsers() {
        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("new-feature")
                .name("New Feature")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.ALL_USERS)
                .build();

        assertTrue(flag.isEnabledForUser("user1", Map.of()));
        assertTrue(flag.isEnabledForUser("user2", Map.of()));
    }

    @Test
    @DisplayName("Should disable feature for all users when disabled")
    void shouldDisableFeatureForAllUsersWhenDisabled() {
        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("new-feature")
                .name("New Feature")
                .isEnabled(false)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.ALL_USERS)
                .build();

        assertFalse(flag.isEnabledForUser("user1", Map.of()));
    }

    @Test
    @DisplayName("Should respect percentage rollout")
    void shouldRespectPercentageRollout() {
        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("new-feature")
                .name("New Feature")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.PERCENTAGE)
                .rolloutPercentage(50)
                .isSticky(true)
                .build();

        // Test with multiple users to ensure distribution
        int enabledCount = 0;
        for (int i = 0; i < 100; i++) {
            if (flag.isEnabledForUser("user" + i, Map.of())) {
                enabledCount++;
            }
        }

        // Should be around 50%, allow for some variance (30-70%)
        assertTrue(enabledCount >= 30 && enabledCount <= 70,
                "Expected ~50% enabled, got " + enabledCount + "%");
    }

    @Test
    @DisplayName("Should enable feature for whitelisted users")
    void shouldEnableFeatureForWhitelistedUsers() {
        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("new-feature")
                .name("New Feature")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.WHITELIST)
                .whitelistedUsers(List.of("user1", "user2", "user3"))
                .build();

        assertTrue(flag.isEnabledForUser("user1", Map.of()));
        assertTrue(flag.isEnabledForUser("user2", Map.of()));
        assertFalse(flag.isEnabledForUser("user4", Map.of()));
    }

    @Test
    @DisplayName("Should handle empty whitelist")
    void shouldHandleEmptyWhitelist() {
        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("new-feature")
                .name("New Feature")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.WHITELIST)
                .whitelistedUsers(List.of())
                .build();

        assertFalse(flag.isEnabledForUser("user1", Map.of()));
    }

    @Test
    @DisplayName("Should evaluate conditions correctly")
    void shouldEvaluateConditionsCorrectly() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("role")
                .operator(FeatureFlagCondition.Operator.EQUALS)
                .value("admin")
                .build();

        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("admin-feature")
                .name("Admin Feature")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.CONDITIONAL)
                .conditions(List.of(condition))
                .build();

        assertTrue(flag.isEnabledForUser("user1", Map.of("role", "admin")));
        assertFalse(flag.isEnabledForUser("user2", Map.of("role", "user")));
    }

    @Test
    @DisplayName("Should evaluate multiple conditions with AND logic")
    void shouldEvaluateMultipleConditionsWithAndLogic() {
        List<FeatureFlagCondition> conditions = List.of(
                FeatureFlagCondition.builder()
                        .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                        .attribute("role")
                        .operator(FeatureFlagCondition.Operator.EQUALS)
                        .value("premium")
                        .build(),
                FeatureFlagCondition.builder()
                        .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                        .attribute("country")
                        .operator(FeatureFlagCondition.Operator.EQUALS)
                        .value("US")
                        .build()
        );

        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("premium-us-feature")
                .name("Premium US Feature")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.CONDITIONAL)
                .conditions(conditions)
                .build();

        // Both conditions met
        assertTrue(flag.isEnabledForUser("user1", Map.of("role", "premium", "country", "US")));
        // Only one condition met
        assertFalse(flag.isEnabledForUser("user2", Map.of("role", "premium", "country", "UK")));
    }

    @Test
    @DisplayName("Should respect expiration date")
    void shouldRespectExpirationDate() {
        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("expiring-feature")
                .name("Expiring Feature")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.ALL_USERS)
                .expiresAt(LocalDateTime.now().minusDays(1))
                .build();

        assertFalse(flag.isEnabledForUser("user1", Map.of()));
    }

    @Test
    @DisplayName("Should not expire feature with future expiration")
    void shouldNotExpireFeatureWithFutureExpiration() {
        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("future-feature")
                .name("Future Feature")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.ALL_USERS)
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();

        assertTrue(flag.isEnabledForUser("user1", Map.of()));
    }

    @Test
    @DisplayName("Should handle null expiration")
    void shouldHandleNullExpiration() {
        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("permanent-feature")
                .name("Permanent Feature")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.ALL_USERS)
                .expiresAt(null)
                .build();

        assertTrue(flag.isEnabledForUser("user1", Map.of()));
    }

    @Test
    @DisplayName("Should increment version")
    void shouldIncrementVersion() {
        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("new-feature")
                .name("New Feature")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.ALL_USERS)
                .version(1)
                .build();

        flag.incrementVersion();
        assertEquals(2, flag.getVersion());
    }

    @Test
    @DisplayName("Should check if flag is active")
    void shouldCheckIfFlagIsActive() {
        FeatureFlag activeFlag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("active-feature")
                .name("Active Feature")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.ALL_USERS)
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();

        FeatureFlag inactiveFlag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("inactive-feature")
                .name("Inactive Feature")
                .isEnabled(false)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.ALL_USERS)
                .build();

        assertTrue(activeFlag.isActive());
        assertFalse(inactiveFlag.isActive());
    }

    @Test
    @DisplayName("Should evaluate AB test variants")
    void shouldEvaluateABTestVariants() {
        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("ab-test")
                .name("AB Test")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.AB_TEST)
                .rolloutPercentage(50)
                .isSticky(true)
                .build();

        // Users should have consistent assignments
        boolean firstEvaluation = flag.isEnabledForUser("user1", Map.of());
        boolean secondEvaluation = flag.isEnabledForUser("user1", Map.of());

        assertEquals(firstEvaluation, secondEvaluation,
                "User should have same result across evaluations");
    }

    @Test
    @DisplayName("Should handle 100% rollout")
    void shouldHandle100PercentRollout() {
        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("full-rollout")
                .name("Full Rollout")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.PERCENTAGE)
                .rolloutPercentage(100)
                .build();

        assertTrue(flag.isEnabledForUser("user1", Map.of()));
        assertTrue(flag.isEnabledForUser("user2", Map.of()));
    }

    @Test
    @DisplayName("Should handle 0% rollout")
    void shouldHandle0PercentRollout() {
        FeatureFlag flag = FeatureFlag.builder()
                .tenantId("tenant-1")
                .flagKey("no-rollout")
                .name("No Rollout")
                .isEnabled(true)
                .rolloutStrategy(FeatureFlag.RolloutStrategy.PERCENTAGE)
                .rolloutPercentage(0)
                .build();

        assertFalse(flag.isEnabledForUser("user1", Map.of()));
        assertFalse(flag.isEnabledForUser("user2", Map.of()));
    }
}
