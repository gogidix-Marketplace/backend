package com.gogidix.aiservices.aimonitoringservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AlertRuleStatus Enum Tests")
class AlertRuleStatusTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(AlertRuleStatus.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(AlertRuleStatus status) {
            assertThat(status).isNotNull();
        }

        @Test
        @DisplayName("Should have ACTIVE status")
        void shouldHaveActiveStatus() {
            assertThat(AlertRuleStatus.valueOf("ACTIVE")).isEqualTo(AlertRuleStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should have INACTIVE status")
        void shouldHaveInactiveStatus() {
            assertThat(AlertRuleStatus.valueOf("INACTIVE")).isEqualTo(AlertRuleStatus.INACTIVE);
        }

        @Test
        @DisplayName("Should have PAUSED status")
        void shouldHavePausedStatus() {
            assertThat(AlertRuleStatus.valueOf("PAUSED")).isEqualTo(AlertRuleStatus.PAUSED);
        }

        @Test
        @DisplayName("Should have 3 status values")
        void shouldHave3StatusValues() {
            assertThat(AlertRuleStatus.values()).hasSize(3);
        }
    }

    @Nested
    @DisplayName("Status Lifecycle Tests")
    class StatusLifecycleTests {

        @Test
        @DisplayName("INACTIVE can transition to ACTIVE")
        void inactiveCanTransitionToActive() {
            AlertRuleStatus from = AlertRuleStatus.INACTIVE;
            AlertRuleStatus to = AlertRuleStatus.ACTIVE;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("ACTIVE can transition to PAUSED")
        void activeCanTransitionToPaused() {
            AlertRuleStatus from = AlertRuleStatus.ACTIVE;
            AlertRuleStatus to = AlertRuleStatus.PAUSED;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("ACTIVE can transition to INACTIVE")
        void activeCanTransitionToInactive() {
            AlertRuleStatus from = AlertRuleStatus.ACTIVE;
            AlertRuleStatus to = AlertRuleStatus.INACTIVE;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("PAUSED can transition to ACTIVE")
        void pausedCanTransitionToActive() {
            AlertRuleStatus from = AlertRuleStatus.PAUSED;
            AlertRuleStatus to = AlertRuleStatus.ACTIVE;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("PAUSED can transition to INACTIVE")
        void pausedCanTransitionToInactive() {
            AlertRuleStatus from = AlertRuleStatus.PAUSED;
            AlertRuleStatus to = AlertRuleStatus.INACTIVE;
            assertThat(from).isNotEqualTo(to);
        }
    }

    @Nested
    @DisplayName("State Characteristics Tests")
    class StateCharacteristicsTests {

        @Test
        @DisplayName("ACTIVE means rule is monitoring")
        void activeMeansRuleIsMonitoring() {
            AlertRuleStatus active = AlertRuleStatus.ACTIVE;
            assertThat(active.name()).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("INACTIVE means rule is not monitoring")
        void inactiveMeansRuleIsNotMonitoring() {
            AlertRuleStatus inactive = AlertRuleStatus.INACTIVE;
            assertThat(inactive.name()).isEqualTo("INACTIVE");
        }

        @Test
        @DisplayName("PAUSED means rule is temporarily suspended")
        void pausedMeansRuleIsTemporarilySuspended() {
            AlertRuleStatus paused = AlertRuleStatus.PAUSED;
            assertThat(paused.name()).isEqualTo("PAUSED");
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have consistent enum names")
        void shouldHaveConsistentEnumNames() {
            for (AlertRuleStatus status : AlertRuleStatus.values()) {
                String name = status.name();
                assertThat(name).isNotNull();
                assertThat(name).isUpperCase();
                assertThat(name).doesNotContain(" ");
                assertThat(name).doesNotContain("\t");
                assertThat(name).doesNotContain("\n");
            }
        }

        @Test
        @DisplayName("Should have unique enum values")
        void shouldHaveUniqueEnumValues() {
            long uniqueCount = java.util.Arrays.stream(AlertRuleStatus.values())
                    .map(AlertRuleStatus::name)
                    .distinct()
                    .count();

            assertThat(uniqueCount).isEqualTo(AlertRuleStatus.values().length);
        }
    }
}
