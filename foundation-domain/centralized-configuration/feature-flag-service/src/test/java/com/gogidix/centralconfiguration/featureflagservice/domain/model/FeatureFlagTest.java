package com.gogidix.centralconfiguration.featureflagservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FeatureFlag Domain Entity Tests")
class FeatureFlagTest {

    private static final Long ID = 1L;
    private static final String TENANT_ID = "tenant-001";
    private static final String FLAG_KEY = "new-ui-feature";
    private static final String NAME = "New UI Feature";
    private static final String DESCRIPTION = "Enable new user interface";

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            LocalDateTime now = LocalDateTime.now();
            Set<FeatureFlagCondition> conditions = Set.of(FeatureFlagCondition.builder().build());

            FeatureFlag flag = FeatureFlag.builder()
                    .id(ID)
                    .tenantId(TENANT_ID)
                    .flagKey(FLAG_KEY)
                    .name(NAME)
                    .description(DESCRIPTION)
                    .isEnabled(true)
                    .rolloutPercentage(75)
                    .rolloutStrategy(RolloutStrategy.PERCENTAGE)
                    .whitelistedUsers("user1,user2")
                    .isSticky(true)
                    .tags("ui,experiment")
                    .owner("team-lead")
                    .expiresAt(now.plusDays(30))
                    .createdBy("admin")
                    .updatedBy("admin")
                    .createdAt(now)
                    .updatedAt(now)
                    .conditions(conditions)
                    .build();

            assertThat(flag.getId()).isEqualTo(ID);
            assertThat(flag.getTenantId()).isEqualTo(TENANT_ID);
            assertThat(flag.getFlagKey()).isEqualTo(FLAG_KEY);
            assertThat(flag.getName()).isEqualTo(NAME);
            assertThat(flag.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(flag.getIsEnabled()).isTrue();
            assertThat(flag.getRolloutPercentage()).isEqualTo(75);
            assertThat(flag.getRolloutStrategy()).isEqualTo(RolloutStrategy.PERCENTAGE);
            assertThat(flag.getWhitelistedUsers()).isEqualTo("user1,user2");
            assertThat(flag.getIsSticky()).isTrue();
            assertThat(flag.getTags()).isEqualTo("ui,experiment");
            assertThat(flag.getOwner()).isEqualTo("team-lead");
        }

        @Test
        @DisplayName("Should build with required fields only")
        void shouldBuildWithRequiredFieldsOnly() {
            FeatureFlag flag = FeatureFlag.builder()
                    .tenantId(TENANT_ID)
                    .flagKey(FLAG_KEY)
                    .name(NAME)
                    .build();

            assertThat(flag.getTenantId()).isEqualTo(TENANT_ID);
            assertThat(flag.getFlagKey()).isEqualTo(FLAG_KEY);
            assertThat(flag.getName()).isEqualTo(NAME);
        }

        @Test
        @DisplayName("Should set default values")
        void shouldSetDefaultValues() {
            FeatureFlag flag = FeatureFlag.builder()
                    .tenantId(TENANT_ID)
                    .flagKey(FLAG_KEY)
                    .name(NAME)
                    .build();

            assertThat(flag.getIsEnabled()).isFalse();
            assertThat(flag.getRolloutPercentage()).isEqualTo(100);
            assertThat(flag.getRolloutStrategy()).isEqualTo(RolloutStrategy.ALL_USERS);
            assertThat(flag.getIsSticky()).isFalse();
            assertThat(flag.getConditions()).isNotNull().isEmpty();
        }
    }

    @Nested
    @DisplayName("FlagKey Tests")
    class FlagKeyTests {

        @Test
        @DisplayName("Should set and get flag key")
        void shouldSetAndGetFlagKey() {
            FeatureFlag flag = FeatureFlag.builder()
                    .flagKey(FLAG_KEY)
                    .build();

            assertThat(flag.getFlagKey()).isEqualTo(FLAG_KEY);
        }

        @ParameterizedTest
        @ValueSource(strings = {"feature-a", "new-ui", "api-v2", "experiment-123"})
        @DisplayName("Should accept various flag keys")
        void shouldAcceptVariousFlagKeys(String key) {
            FeatureFlag flag = FeatureFlag.builder()
                    .flagKey(key)
                    .build();

            assertThat(flag.getFlagKey()).isEqualTo(key);
        }
    }

    @Nested
    @DisplayName("IsEnabled Tests")
    class IsEnabledTests {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        @DisplayName("Should set enabled status")
        void shouldSetEnabledStatus(boolean enabled) {
            FeatureFlag flag = FeatureFlag.builder()
                    .isEnabled(enabled)
                    .build();

            assertThat(flag.getIsEnabled()).isEqualTo(enabled);
        }

        @Test
        @DisplayName("Should default to disabled")
        void shouldDefaultToDisabled() {
            FeatureFlag flag = FeatureFlag.builder()
                    .build();

            assertThat(flag.getIsEnabled()).isFalse();
        }
    }

    @Nested
    @DisplayName("RolloutPercentage Tests")
    class RolloutPercentageTests {

        @ParameterizedTest
        @ValueSource(ints = {0, 10, 50, 75, 100})
        @DisplayName("Should accept various rollout percentages")
        void shouldAcceptVariousRolloutPercentages(int percentage) {
            FeatureFlag flag = FeatureFlag.builder()
                    .rolloutPercentage(percentage)
                    .build();

            assertThat(flag.getRolloutPercentage()).isEqualTo(percentage);
        }

        @Test
        @DisplayName("Should default to 100 percent")
        void shouldDefaultTo100Percent() {
            FeatureFlag flag = FeatureFlag.builder()
                    .build();

            assertThat(flag.getRolloutPercentage()).isEqualTo(100);
        }
    }

    @Nested
    @DisplayName("RolloutStrategy Tests")
    class RolloutStrategyTests {

        @ParameterizedTest
        @EnumSource(RolloutStrategy.class)
        @DisplayName("Should accept all rollout strategies")
        void shouldAcceptAllRolloutStrategies(RolloutStrategy strategy) {
            FeatureFlag flag = FeatureFlag.builder()
                    .rolloutStrategy(strategy)
                    .build();

            assertThat(flag.getRolloutStrategy()).isEqualTo(strategy);
        }

        @Test
        @DisplayName("Should default to ALL_USERS")
        void shouldDefaultToALL_USERS() {
            FeatureFlag flag = FeatureFlag.builder()
                    .build();

            assertThat(flag.getRolloutStrategy()).isEqualTo(RolloutStrategy.ALL_USERS);
        }
    }

    @Nested
    @DisplayName("WhitelistedUsers Tests")
    class WhitelistedUsersTests {

        @Test
        @DisplayName("Should set and get whitelisted users")
        void shouldSetAndGetWhitelistedUsers() {
            FeatureFlag flag = FeatureFlag.builder()
                    .whitelistedUsers("user1,user2,user3")
                    .build();

            assertThat(flag.getWhitelistedUsers()).isEqualTo("user1,user2,user3");
        }

        @Test
        @DisplayName("Should accept null whitelisted users")
        void shouldAcceptNullWhitelistedUsers() {
            FeatureFlag flag = FeatureFlag.builder()
                    .whitelistedUsers(null)
                    .build();

            assertThat(flag.getWhitelistedUsers()).isNull();
        }

        @Test
        @DisplayName("Should accept empty whitelisted users")
        void shouldAcceptEmptyWhitelistedUsers() {
            FeatureFlag flag = FeatureFlag.builder()
                    .whitelistedUsers("")
                    .build();

            assertThat(flag.getWhitelistedUsers()).isEmpty();
        }
    }

    @Nested
    @DisplayName("IsSticky Tests")
    class IsStickyTests {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        @DisplayName("Should set sticky flag")
        void shouldSetStickyFlag(boolean sticky) {
            FeatureFlag flag = FeatureFlag.builder()
                    .isSticky(sticky)
                    .build();

            assertThat(flag.getIsSticky()).isEqualTo(sticky);
        }

        @Test
        @DisplayName("Should default to not sticky")
        void shouldDefaultToNotSticky() {
            FeatureFlag flag = FeatureFlag.builder()
                    .build();

            assertThat(flag.getIsSticky()).isFalse();
        }
    }

    @Nested
    @DisplayName("Expiration Tests")
    class ExpirationTests {

        @Test
        @DisplayName("Should check if flag is expired")
        void shouldCheckIfFlagIsExpired() {
            FeatureFlag flag = FeatureFlag.builder()
                    .expiresAt(LocalDateTime.now().minusDays(1))
                    .build();

            assertThat(flag.isExpired()).isTrue();
        }

        @Test
        @DisplayName("Should check if flag is not expired")
        void shouldCheckIfFlagIsNotExpired() {
            FeatureFlag flag = FeatureFlag.builder()
                    .expiresAt(LocalDateTime.now().plusDays(1))
                    .build();

            assertThat(flag.isExpired()).isFalse();
        }

        @Test
        @DisplayName("Should not be expired when no expiry set")
        void shouldNotBeExpiredWhenNoExpirySet() {
            FeatureFlag flag = FeatureFlag.builder()
                    .expiresAt(null)
                    .build();

            assertThat(flag.isExpired()).isFalse();
        }

        @Test
        @DisplayName("Should accept null expires at")
        void shouldAcceptNullExpiresAt() {
            FeatureFlag flag = FeatureFlag.builder()
                    .expiresAt(null)
                    .build();

            assertThat(flag.getExpiresAt()).isNull();
        }
    }

    @Nested
    @DisplayName("Whitelist Tests")
    class WhitelistTests {

        @Test
        @DisplayName("Should check if user is whitelisted")
        void shouldCheckIfUserIsWhitelisted() {
            FeatureFlag flag = FeatureFlag.builder()
                    .whitelistedUsers("user1,user2,user3")
                    .build();

            assertThat(flag.isUserWhitelisted("user1")).isTrue();
            assertThat(flag.isUserWhitelisted("user2")).isTrue();
            assertThat(flag.isUserWhitelisted("user4")).isFalse();
        }

        @Test
        @DisplayName("Should return false for null whitelisted users")
        void shouldReturnFalseForNullWhitelistedUsers() {
            FeatureFlag flag = FeatureFlag.builder()
                    .whitelistedUsers(null)
                    .build();

            assertThat(flag.isUserWhitelisted("user1")).isFalse();
        }

        @Test
        @DisplayName("Should return false for empty whitelisted users")
        void shouldReturnFalseForEmptyWhitelistedUsers() {
            FeatureFlag flag = FeatureFlag.builder()
                    .whitelistedUsers("")
                    .build();

            assertThat(flag.isUserWhitelisted("user1")).isFalse();
        }
    }

    @Nested
    @DisplayName("Enable Disable Tests")
    class EnableDisableTests {

        @Test
        @DisplayName("Should enable flag")
        void shouldEnableFlag() {
            FeatureFlag flag = FeatureFlag.builder()
                    .isEnabled(false)
                    .build();

            flag.enable();

            assertThat(flag.getIsEnabled()).isTrue();
        }

        @Test
        @DisplayName("Should disable flag")
        void shouldDisableFlag() {
            FeatureFlag flag = FeatureFlag.builder()
                    .isEnabled(true)
                    .build();

            flag.disable();

            assertThat(flag.getIsEnabled()).isFalse();
        }

        @Test
        @DisplayName("Should allow re-enabling")
        void shouldAllowReEnabling() {
            FeatureFlag flag = FeatureFlag.builder()
                    .isEnabled(true)
                    .build();

            flag.disable();
            assertThat(flag.getIsEnabled()).isFalse();

            flag.enable();
            assertThat(flag.getIsEnabled()).isTrue();
        }
    }

    @Nested
    @DisplayName("Conditions Tests")
    class ConditionsTests {

        @Test
        @DisplayName("Should add condition")
        void shouldAddCondition() {
            FeatureFlag flag = FeatureFlag.builder()
                    .build();

            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeName("country")
                    .operator("eq")
                    .attributeValue("US")
                    .build();

            flag.addCondition(condition);

            assertThat(flag.getConditions()).hasSize(1);
        }

        @Test
        @DisplayName("Should initialize with empty conditions")
        void shouldInitializeWithEmptyConditions() {
            FeatureFlag flag = FeatureFlag.builder()
                    .build();

            assertThat(flag.getConditions()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Should accept null conditions")
        void shouldAcceptNullConditions() {
            FeatureFlag flag = FeatureFlag.builder()
                    .conditions(null)
                    .build();

            assertThat(flag.getConditions()).isNull();
        }
    }

    @Nested
    @DisplayName("Tags Tests")
    class TagsTests {

        @Test
        @DisplayName("Should set and get tags")
        void shouldSetAndGetTags() {
            FeatureFlag flag = FeatureFlag.builder()
                    .tags("ui,experiment,team-a")
                    .build();

            assertThat(flag.getTags()).isEqualTo("ui,experiment,team-a");
        }

        @Test
        @DisplayName("Should accept null tags")
        void shouldAcceptNullTags() {
            FeatureFlag flag = FeatureFlag.builder()
                    .tags(null)
                    .build();

            assertThat(flag.getTags()).isNull();
        }

        @Test
        @DisplayName("Should accept empty tags")
        void shouldAcceptEmptyTags() {
            FeatureFlag flag = FeatureFlag.builder()
                    .tags("")
                    .build();

            assertThat(flag.getTags()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Lombok Data Tests")
    class LombokDataTests {

        @Test
        @DisplayName("Should generate equals")
        void shouldGenerateEquals() {
            FeatureFlag flag1 = FeatureFlag.builder()
                    .tenantId(TENANT_ID)
                    .flagKey(FLAG_KEY)
                    .name(NAME)
                    .build();

            FeatureFlag flag2 = FeatureFlag.builder()
                    .tenantId(TENANT_ID)
                    .flagKey(FLAG_KEY)
                    .name(NAME)
                    .build();

            assertThat(flag1).isEqualTo(flag2);
        }

        @Test
        @DisplayName("Should generate hashCode")
        void shouldGenerateHashCode() {
            FeatureFlag flag1 = FeatureFlag.builder()
                    .flagKey(FLAG_KEY)
                    .name(NAME)
                    .build();

            FeatureFlag flag2 = FeatureFlag.builder()
                    .flagKey(FLAG_KEY)
                    .name(NAME)
                    .build();

            assertThat(flag1.hashCode()).isEqualTo(flag2.hashCode());
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            FeatureFlag flag = FeatureFlag.builder()
                    .flagKey(FLAG_KEY)
                    .name(NAME)
                    .build();

            String toString = flag.toString();

            assertThat(toString).contains(FLAG_KEY);
        }

        @Test
        @DisplayName("Should generate setters")
        void shouldGenerateSetters() {
            FeatureFlag flag = FeatureFlag.builder()
                    .flagKey(FLAG_KEY)
                    .name(NAME)
                    .isEnabled(false)
                    .build();

            flag.setName("Updated Name");
            flag.setIsEnabled(true);

            assertThat(flag.getName()).isEqualTo("Updated Name");
            assertThat(flag.getIsEnabled()).isTrue();
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("Should represent feature for all users")
        void shouldRepresentFeatureForAllUsers() {
            FeatureFlag flag = FeatureFlag.builder()
                    .flagKey("public-feature")
                    .name("Public Feature")
                    .isEnabled(true)
                    .rolloutStrategy(RolloutStrategy.ALL_USERS)
                    .rolloutPercentage(100)
                    .build();

            assertThat(flag.getRolloutStrategy()).isEqualTo(RolloutStrategy.ALL_USERS);
            assertThat(flag.getRolloutPercentage()).isEqualTo(100);
        }

        @Test
        @DisplayName("Should represent percentage rollout")
        void shouldRepresentPercentageRollout() {
            FeatureFlag flag = FeatureFlag.builder()
                    .flagKey("gradual-feature")
                    .name("Gradual Feature")
                    .isEnabled(true)
                    .rolloutStrategy(RolloutStrategy.PERCENTAGE)
                    .rolloutPercentage(25)
                    .build();

            assertThat(flag.getRolloutStrategy()).isEqualTo(RolloutStrategy.PERCENTAGE);
            assertThat(flag.getRolloutPercentage()).isEqualTo(25);
        }

        @Test
        @DisplayName("Should represent whitelisted rollout")
        void shouldRepresentWhitelistedRollout() {
            FeatureFlag flag = FeatureFlag.builder()
                    .flagKey("private-feature")
                    .name("Private Feature")
                    .isEnabled(true)
                    .rolloutStrategy(RolloutStrategy.WHITELIST)
                    .whitelistedUsers("user1,user2,user3")
                    .build();

            assertThat(flag.getRolloutStrategy()).isEqualTo(RolloutStrategy.WHITELIST);
            assertThat(flag.isUserWhitelisted("user1")).isTrue();
        }

        @Test
        @DisplayName("Should represent beta tester rollout")
        void shouldRepresentBetaTesterRollout() {
            FeatureFlag flag = FeatureFlag.builder()
                    .flagKey("beta-feature")
                    .name("Beta Feature")
                    .isEnabled(true)
                    .rolloutStrategy(RolloutStrategy.BETA_TESTERS)
                    .build();

            assertThat(flag.getRolloutStrategy()).isEqualTo(RolloutStrategy.BETA_TESTERS);
        }

        @Test
        @DisplayName("Should represent internal rollout")
        void shouldRepresentInternalRollout() {
            FeatureFlag flag = FeatureFlag.builder()
                    .flagKey("internal-feature")
                    .name("Internal Feature")
                    .isEnabled(true)
                    .rolloutStrategy(RolloutStrategy.INTERNAL)
                    .build();

            assertThat(flag.getRolloutStrategy()).isEqualTo(RolloutStrategy.INTERNAL);
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should get id")
        void shouldGetId() {
            FeatureFlag flag = FeatureFlag.builder()
                    .id(ID)
                    .build();

            assertThat(flag.getId()).isEqualTo(ID);
        }

        @Test
        @DisplayName("Should get tenant ID")
        void shouldGetTenantId() {
            FeatureFlag flag = FeatureFlag.builder()
                    .tenantId(TENANT_ID)
                    .build();

            assertThat(flag.getTenantId()).isEqualTo(TENANT_ID);
        }

        @Test
        @DisplayName("Should get flag key")
        void shouldGetFlagKey() {
            FeatureFlag flag = FeatureFlag.builder()
                    .flagKey(FLAG_KEY)
                    .build();

            assertThat(flag.getFlagKey()).isEqualTo(FLAG_KEY);
        }

        @Test
        @DisplayName("Should get name")
        void shouldGetName() {
            FeatureFlag flag = FeatureFlag.builder()
                    .name(NAME)
                    .build();

            assertThat(flag.getName()).isEqualTo(NAME);
        }

        @Test
        @DisplayName("Should get description")
        void shouldGetDescription() {
            FeatureFlag flag = FeatureFlag.builder()
                    .description(DESCRIPTION)
                    .build();

            assertThat(flag.getDescription()).isEqualTo(DESCRIPTION);
        }

        @Test
        @DisplayName("Should get is enabled")
        void shouldGetIsEnabled() {
            FeatureFlag flag = FeatureFlag.builder()
                    .isEnabled(true)
                    .build();

            assertThat(flag.getIsEnabled()).isTrue();
        }

        @Test
        @DisplayName("Should get rollout percentage")
        void shouldGetRolloutPercentage() {
            FeatureFlag flag = FeatureFlag.builder()
                    .rolloutPercentage(50)
                    .build();

            assertThat(flag.getRolloutPercentage()).isEqualTo(50);
        }

        @Test
        @DisplayName("Should get rollout strategy")
        void shouldGetRolloutStrategy() {
            FeatureFlag flag = FeatureFlag.builder()
                    .rolloutStrategy(RolloutStrategy.GRADUAL)
                    .build();

            assertThat(flag.getRolloutStrategy()).isEqualTo(RolloutStrategy.GRADUAL);
        }

        @Test
        @DisplayName("Should get whitelisted users")
        void shouldGetWhitelistedUsers() {
            FeatureFlag flag = FeatureFlag.builder()
                    .whitelistedUsers("user1,user2")
                    .build();

            assertThat(flag.getWhitelistedUsers()).isEqualTo("user1,user2");
        }

        @Test
        @DisplayName("Should get is sticky")
        void shouldGetIsSticky() {
            FeatureFlag flag = FeatureFlag.builder()
                    .isSticky(true)
                    .build();

            assertThat(flag.getIsSticky()).isTrue();
        }

        @Test
        @DisplayName("Should get tags")
        void shouldGetTags() {
            FeatureFlag flag = FeatureFlag.builder()
                    .tags("ui,experiment")
                    .build();

            assertThat(flag.getTags()).isEqualTo("ui,experiment");
        }

        @Test
        @DisplayName("Should get owner")
        void shouldGetOwner() {
            FeatureFlag flag = FeatureFlag.builder()
                    .owner("team-lead")
                    .build();

            assertThat(flag.getOwner()).isEqualTo("team-lead");
        }

        @Test
        @DisplayName("Should get expires at")
        void shouldGetExpiresAt() {
            LocalDateTime expiry = LocalDateTime.now().plusDays(30);
            FeatureFlag flag = FeatureFlag.builder()
                    .expiresAt(expiry)
                    .build();

            assertThat(flag.getExpiresAt()).isEqualTo(expiry);
        }

        @Test
        @DisplayName("Should get created by")
        void shouldGetCreatedBy() {
            FeatureFlag flag = FeatureFlag.builder()
                    .createdBy("admin")
                    .build();

            assertThat(flag.getCreatedBy()).isEqualTo("admin");
        }

        @Test
        @DisplayName("Should get updated by")
        void shouldGetUpdatedBy() {
            FeatureFlag flag = FeatureFlag.builder()
                    .updatedBy("user")
                    .build();

            assertThat(flag.getUpdatedBy()).isEqualTo("user");
        }

        @Test
        @DisplayName("Should get created at")
        void shouldGetCreatedAt() {
            LocalDateTime now = LocalDateTime.now();
            FeatureFlag flag = FeatureFlag.builder()
                    .createdAt(now)
                    .build();

            assertThat(flag.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should get updated at")
        void shouldGetUpdatedAt() {
            LocalDateTime now = LocalDateTime.now();
            FeatureFlag flag = FeatureFlag.builder()
                    .updatedAt(now)
                    .build();

            assertThat(flag.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should get conditions")
        void shouldGetConditions() {
            Set<FeatureFlagCondition> conditions = Set.of(FeatureFlagCondition.builder().build());
            FeatureFlag flag = FeatureFlag.builder()
                    .conditions(conditions)
                    .build();

            assertThat(flag.getConditions()).isEqualTo(conditions);
        }
    }
}
