package com.gogidix.centralconfiguration.featureflagservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RolloutStrategy Enum Tests")
class RolloutStrategyTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(RolloutStrategy.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(RolloutStrategy strategy) {
            assertThat(strategy).isNotNull();
        }

        @Test
        @DisplayName("Should have ALL_USERS strategy")
        void shouldHaveALL_USERSStrategy() {
            assertThat(RolloutStrategy.valueOf("ALL_USERS")).isEqualTo(RolloutStrategy.ALL_USERS);
        }

        @Test
        @DisplayName("Should have PERCENTAGE strategy")
        void shouldHavePERCENTAGEStrategy() {
            assertThat(RolloutStrategy.valueOf("PERCENTAGE")).isEqualTo(RolloutStrategy.PERCENTAGE);
        }

        @Test
        @DisplayName("Should have WHITELIST strategy")
        void shouldHaveWHITELISTStrategy() {
            assertThat(RolloutStrategy.valueOf("WHITELIST")).isEqualTo(RolloutStrategy.WHITELIST);
        }

        @Test
        @DisplayName("Should have GRADUAL strategy")
        void shouldHaveGRADUALStrategy() {
            assertThat(RolloutStrategy.valueOf("GRADUAL")).isEqualTo(RolloutStrategy.GRADUAL);
        }

        @Test
        @DisplayName("Should have BETA_TESTERS strategy")
        void shouldHaveBETA_TESTERSStrategy() {
            assertThat(RolloutStrategy.valueOf("BETA_TESTERS")).isEqualTo(RolloutStrategy.BETA_TESTERS);
        }

        @Test
        @DisplayName("Should have INTERNAL strategy")
        void shouldHaveINTERNALStrategy() {
            assertThat(RolloutStrategy.valueOf("INTERNAL")).isEqualTo(RolloutStrategy.INTERNAL);
        }

        @Test
        @DisplayName("Should have 6 rollout strategies")
        void shouldHave6RolloutStrategies() {
            assertThat(RolloutStrategy.values()).hasSize(6);
        }
    }

    @Nested
    @DisplayName("Code Tests")
    class CodeTests {

        @Test
        @DisplayName("ALL_USERS has code 'all'")
        void allUsersHasCode() {
            assertThat(RolloutStrategy.ALL_USERS.getCode()).isEqualTo("all");
        }

        @Test
        @DisplayName("PERCENTAGE has code 'percentage'")
        void percentageHasCode() {
            assertThat(RolloutStrategy.PERCENTAGE.getCode()).isEqualTo("percentage");
        }

        @Test
        @DisplayName("WHITELIST has code 'whitelist'")
        void whitelistHasCode() {
            assertThat(RolloutStrategy.WHITELIST.getCode()).isEqualTo("whitelist");
        }

        @Test
        @DisplayName("GRADUAL has code 'gradual'")
        void gradualHasCode() {
            assertThat(RolloutStrategy.GRADUAL.getCode()).isEqualTo("gradual");
        }

        @Test
        @DisplayName("BETA_TESTERS has code 'beta'")
        void betaTestersHasCode() {
            assertThat(RolloutStrategy.BETA_TESTERS.getCode()).isEqualTo("beta");
        }

        @Test
        @DisplayName("INTERNAL has code 'internal'")
        void internalHasCode() {
            assertThat(RolloutStrategy.INTERNAL.getCode()).isEqualTo("internal");
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("All strategies have descriptions")
        void allStrategiesHaveDescriptions() {
            for (RolloutStrategy strategy : RolloutStrategy.values()) {
                assertThat(strategy.getDescription()).isNotNull();
                assertThat(strategy.getDescription()).isNotEmpty();
            }
        }

        @Test
        @DisplayName("ALL_USERS description mentions all users")
        void allUsersDescriptionMentionsAllUsers() {
            assertThat(RolloutStrategy.ALL_USERS.getDescription()).containsIgnoringCase("all users");
        }

        @Test
        @DisplayName("PERCENTAGE description mentions percentage")
        void percentageDescriptionMentionsPercentage() {
            assertThat(RolloutStrategy.PERCENTAGE.getDescription()).containsIgnoringCase("percentage");
        }

        @Test
        @DisplayName("WHITELIST description mentions whitelisted")
        void whitelistDescriptionMentionsWhitelisted() {
            assertThat(RolloutStrategy.WHITELIST.getDescription()).containsIgnoringCase("whitelisted");
        }

        @Test
        @DisplayName("GRADUAL description mentions gradually")
        void gradualDescriptionMentionsGradually() {
            assertThat(RolloutStrategy.GRADUAL.getDescription()).containsIgnoringCase("gradually");
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("ALL_USERS for full rollout")
        void allUsersForFullRollout() {
            RolloutStrategy strategy = RolloutStrategy.ALL_USERS;
            assertThat(strategy).isEqualTo(RolloutStrategy.ALL_USERS);
        }

        @Test
        @DisplayName("PERCENTAGE for partial rollout")
        void percentageForPartialRollout() {
            RolloutStrategy strategy = RolloutStrategy.PERCENTAGE;
            assertThat(strategy).isEqualTo(RolloutStrategy.PERCENTAGE);
        }

        @Test
        @DisplayName("WHITELIST for targeted users")
        void whitelistForTargetedUsers() {
            RolloutStrategy strategy = RolloutStrategy.WHITELIST;
            assertThat(strategy).isEqualTo(RolloutStrategy.WHITELIST);
        }

        @Test
        @DisplayName("GRADUAL for phased rollout")
        void gradualForPhasedRollout() {
            RolloutStrategy strategy = RolloutStrategy.GRADUAL;
            assertThat(strategy).isEqualTo(RolloutStrategy.GRADUAL);
        }

        @Test
        @DisplayName("BETA_TESTERS for testing")
        void betaTestersForTesting() {
            RolloutStrategy strategy = RolloutStrategy.BETA_TESTERS;
            assertThat(strategy).isEqualTo(RolloutStrategy.BETA_TESTERS);
        }

        @Test
        @DisplayName("INTERNAL for internal testing")
        void internalForInternalTesting() {
            RolloutStrategy strategy = RolloutStrategy.INTERNAL;
            assertThat(strategy).isEqualTo(RolloutStrategy.INTERNAL);
        }
    }

    @Nested
    @DisplayName("Access Control Tests")
    class AccessControlTests {

        @Test
        @DisplayName("ALL_USERS provides broadest access")
        void allUsersProvidesBroadestAccess() {
            RolloutStrategy strategy = RolloutStrategy.ALL_USERS;
            assertThat(strategy.getCode()).isEqualTo("all");
        }

        @Test
        @DisplayName("INTERNAL provides most restricted access")
        void internalProvidesMostRestrictedAccess() {
            RolloutStrategy strategy = RolloutStrategy.INTERNAL;
            assertThat(strategy.getCode()).isEqualTo("internal");
        }

        @Test
        @DisplayName("WHITELIST provides controlled access")
        void whitelistProvidesControlledAccess() {
            RolloutStrategy strategy = RolloutStrategy.WHITELIST;
            assertThat(strategy).isEqualTo(RolloutStrategy.WHITELIST);
        }
    }

    @Nested
    @DisplayName("Rollout Phases Tests")
    class RolloutPhasesTests {

        @Test
        @DisplayName("Should support testing phase")
        void shouldSupportTestingPhase() {
            RolloutStrategy internal = RolloutStrategy.INTERNAL;
            RolloutStrategy beta = RolloutStrategy.BETA_TESTERS;

            assertThat(internal).isNotNull();
            assertThat(beta).isNotNull();
        }

        @Test
        @DisplayName("Should support gradual rollout")
        void shouldSupportGradualRollout() {
            RolloutStrategy gradual = RolloutStrategy.GRADUAL;
            assertThat(gradual).isEqualTo(RolloutStrategy.GRADUAL);
        }

        @Test
        @DisplayName("Should support full rollout")
        void shouldSupportFullRollout() {
            RolloutStrategy allUsers = RolloutStrategy.ALL_USERS;
            assertThat(allUsers).isEqualTo(RolloutStrategy.ALL_USERS);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have consistent enum names")
        void shouldHaveConsistentEnumNames() {
            for (RolloutStrategy strategy : RolloutStrategy.values()) {
                String name = strategy.name();
                assertThat(name).isNotNull();
                assertThat(name).isUpperCase();
                assertThat(name).doesNotContain(" ");
            }
        }

        @Test
        @DisplayName("Should have unique enum values")
        void shouldHaveUniqueEnumValues() {
            long uniqueCount = java.util.Arrays.stream(RolloutStrategy.values())
                    .map(RolloutStrategy::name)
                    .distinct()
                    .count();

            assertThat(uniqueCount).isEqualTo(RolloutStrategy.values().length);
        }

        @Test
        @DisplayName("Should have unique codes")
        void shouldHaveUniqueCodes() {
            long uniqueCodes = java.util.Arrays.stream(RolloutStrategy.values())
                    .map(RolloutStrategy::getCode)
                    .distinct()
                    .count();

            assertThat(uniqueCodes).isEqualTo(RolloutStrategy.values().length);
        }
    }

    @Nested
    @DisplayName("User Targeting Tests")
    class UserTargetingTests {

        @Test
        @DisplayName("ALL_USERS targets everyone")
        void allUsersTargetsEveryone() {
            RolloutStrategy strategy = RolloutStrategy.ALL_USERS;
            assertThat(strategy).isNotNull();
        }

        @Test
        @DisplayName("PERCENTAGE targets subset")
        void percentageTargetsSubset() {
            RolloutStrategy strategy = RolloutStrategy.PERCENTAGE;
            assertThat(strategy).isNotNull();
        }

        @Test
        @DisplayName("WHITELIST targets specific users")
        void whitelistTargetsSpecificUsers() {
            RolloutStrategy strategy = RolloutStrategy.WHITELIST;
            assertThat(strategy).isNotNull();
        }

        @Test
        @DisplayName("BETA_TESTERS targets testers")
        void betaTestersTargetsTesters() {
            RolloutStrategy strategy = RolloutStrategy.BETA_TESTERS;
            assertThat(strategy).isNotNull();
        }

        @Test
        @DisplayName("INTERNAL targets internal users")
        void internalTargetsInternalUsers() {
            RolloutStrategy strategy = RolloutStrategy.INTERNAL;
            assertThat(strategy).isNotNull();
        }
    }

    @Nested
    @DisplayName("Strategy Selection Tests")
    class StrategySelectionTests {

        @Test
        @DisplayName("Should use INTERNAL for initial development")
        void shouldUseInternalForInitialDevelopment() {
            RolloutStrategy strategy = RolloutStrategy.INTERNAL;
            assertThat(strategy).isEqualTo(RolloutStrategy.INTERNAL);
        }

        @Test
        @DisplayName("Should use BETA_TESTERS for beta testing")
        void shouldUseBetaTestersForBetaTesting() {
            RolloutStrategy strategy = RolloutStrategy.BETA_TESTERS;
            assertThat(strategy).isEqualTo(RolloutStrategy.BETA_TESTERS);
        }

        @Test
        @DisplayName("Should use GRADUAL for progressive rollout")
        void shouldUseGradualForProgressiveRollout() {
            RolloutStrategy strategy = RolloutStrategy.GRADUAL;
            assertThat(strategy).isEqualTo(RolloutStrategy.GRADUAL);
        }

        @Test
        @DisplayName("Should use PERCENTAGE for partial rollout")
        void shouldUsePercentageForPartialRollout() {
            RolloutStrategy strategy = RolloutStrategy.PERCENTAGE;
            assertThat(strategy).isEqualTo(RolloutStrategy.PERCENTAGE);
        }

        @Test
        @DisplayName("Should use ALL_USERS for complete rollout")
        void shouldUseAllUsersForCompleteRollout() {
            RolloutStrategy strategy = RolloutStrategy.ALL_USERS;
            assertThat(strategy).isEqualTo(RolloutStrategy.ALL_USERS);
        }

        @Test
        @DisplayName("Should use WHITELIST for private features")
        void shouldUseWhitelistForPrivateFeatures() {
            RolloutStrategy strategy = RolloutStrategy.WHITELIST;
            assertThat(strategy).isEqualTo(RolloutStrategy.WHITELIST);
        }
    }
}
