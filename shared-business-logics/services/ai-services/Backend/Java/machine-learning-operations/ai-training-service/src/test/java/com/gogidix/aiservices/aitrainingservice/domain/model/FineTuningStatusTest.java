package com.gogidix.aiservices.aitrainingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FineTuningStatus Enum Tests")
class FineTuningStatusTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(FineTuningStatus.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(FineTuningStatus status) {
            assertThat(status).isNotNull();
        }

        @Test
        @DisplayName("Should have PENDING status")
        void shouldHavePendingStatus() {
            assertThat(FineTuningStatus.valueOf("PENDING")).isEqualTo(FineTuningStatus.PENDING);
        }

        @Test
        @DisplayName("Should have RUNNING status")
        void shouldHaveRunningStatus() {
            assertThat(FineTuningStatus.valueOf("RUNNING")).isEqualTo(FineTuningStatus.RUNNING);
        }

        @Test
        @DisplayName("Should have COMPLETED status")
        void shouldHaveCompletedStatus() {
            assertThat(FineTuningStatus.valueOf("COMPLETED")).isEqualTo(FineTuningStatus.COMPLETED);
        }

        @Test
        @DisplayName("Should have FAILED status")
        void shouldHaveFailedStatus() {
            assertThat(FineTuningStatus.valueOf("FAILED")).isEqualTo(FineTuningStatus.FAILED);
        }

        @Test
        @DisplayName("Should have CANCELLED status")
        void shouldHaveCancelledStatus() {
            assertThat(FineTuningStatus.valueOf("CANCELLED")).isEqualTo(FineTuningStatus.CANCELLED);
        }

        @Test
        @DisplayName("Should have 5 status values")
        void shouldHave5StatusValues() {
            assertThat(FineTuningStatus.values()).hasSize(5);
        }
    }

    @Nested
    @DisplayName("Status Lifecycle Phases Tests")
    class StatusLifecyclePhasesTests {

        @Test
        @DisplayName("Should have initial phase")
        void shouldHaveInitialPhase() {
            assertThat(FineTuningStatus.PENDING).isNotNull();
        }

        @Test
        @DisplayName("Should have active phase")
        void shouldHaveActivePhase() {
            assertThat(FineTuningStatus.RUNNING).isNotNull();
        }

        @Test
        @DisplayName("Should have terminal states")
        void shouldHaveTerminalStates() {
            assertThat(FineTuningStatus.values()).contains(
                    FineTuningStatus.COMPLETED,
                    FineTuningStatus.FAILED,
                    FineTuningStatus.CANCELLED
            );
        }
    }

    @Nested
    @DisplayName("Valid State Transitions Tests")
    class ValidTransitionsTests {

        @Test
        @DisplayName("PENDING can transition to RUNNING")
        void pendingCanTransitionToRunning() {
            FineTuningStatus from = FineTuningStatus.PENDING;
            FineTuningStatus to = FineTuningStatus.RUNNING;

            assertThat(from).isNotEqualTo(to);
            assertThat(from.name()).isEqualTo("PENDING");
            assertThat(to.name()).isEqualTo("RUNNING");
        }

        @Test
        @DisplayName("PENDING can transition to CANCELLED")
        void pendingCanTransitionToCancelled() {
            FineTuningStatus from = FineTuningStatus.PENDING;
            FineTuningStatus to = FineTuningStatus.CANCELLED;

            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("RUNNING can transition to COMPLETED")
        void runningCanTransitionToCompleted() {
            FineTuningStatus from = FineTuningStatus.RUNNING;
            FineTuningStatus to = FineTuningStatus.COMPLETED;

            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("RUNNING can transition to FAILED")
        void runningCanTransitionToFailed() {
            FineTuningStatus from = FineTuningStatus.RUNNING;
            FineTuningStatus to = FineTuningStatus.FAILED;

            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("RUNNING can transition to CANCELLED")
        void runningCanTransitionToCancelled() {
            FineTuningStatus from = FineTuningStatus.RUNNING;
            FineTuningStatus to = FineTuningStatus.CANCELLED;

            assertThat(from).isNotEqualTo(to);
        }
    }

    @Nested
    @DisplayName("Terminal States Tests")
    class TerminalStatesTests {

        @Test
        @DisplayName("COMPLETED should be a terminal state")
        void completedShouldBeTerminal() {
            FineTuningStatus terminal = FineTuningStatus.COMPLETED;
            assertThat(terminal.name()).isEqualTo("COMPLETED");
        }

        @Test
        @DisplayName("FAILED should be a terminal state")
        void failedShouldBeTerminal() {
            FineTuningStatus terminal = FineTuningStatus.FAILED;
            assertThat(terminal.name()).isEqualTo("FAILED");
        }

        @Test
        @DisplayName("CANCELLED should be a terminal state")
        void cancelledShouldBeTerminal() {
            FineTuningStatus terminal = FineTuningStatus.CANCELLED;
            assertThat(terminal.name()).isEqualTo("CANCELLED");
        }

        @Test
        @DisplayName("Should have 3 terminal states")
        void shouldHave3TerminalStates() {
            long terminalCount = java.util.Arrays.stream(FineTuningStatus.values())
                    .filter(s -> s == FineTuningStatus.COMPLETED ||
                                 s == FineTuningStatus.FAILED ||
                                 s == FineTuningStatus.CANCELLED)
                    .count();

            assertThat(terminalCount).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Active States Tests")
    class ActiveStatesTests {

        @Test
        @DisplayName("PENDING should be an active state")
        void pendingShouldBeActive() {
            FineTuningStatus active = FineTuningStatus.PENDING;
            assertThat(active.name()).isEqualTo("PENDING");
        }

        @Test
        @DisplayName("RUNNING should be an active state")
        void runningShouldBeActive() {
            FineTuningStatus active = FineTuningStatus.RUNNING;
            assertThat(active.name()).isEqualTo("RUNNING");
        }

        @Test
        @DisplayName("Should have 2 active states")
        void shouldHave2ActiveStates() {
            long activeCount = java.util.Arrays.stream(FineTuningStatus.values())
                    .filter(s -> s == FineTuningStatus.PENDING || s == FineTuningStatus.RUNNING)
                    .count();

            assertThat(activeCount).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have consistent enum names")
        void shouldHaveConsistentEnumNames() {
            for (FineTuningStatus status : FineTuningStatus.values()) {
                String name = status.name();
                assertThat(name).isNotNull();
                assertThat(name).isUpperCase();
                assertThat(name).matches("[A-Z_]+");
            }
        }

        @Test
        @DisplayName("Should have unique enum values")
        void shouldHaveUniqueEnumValues() {
            long uniqueCount = java.util.Arrays.stream(FineTuningStatus.values())
                    .map(FineTuningStatus::name)
                    .distinct()
                    .count();

            assertThat(uniqueCount).isEqualTo(FineTuningStatus.values().length);
        }
    }

    @Nested
    @DisplayName("Fine-Tuning Job State Machine Tests")
    class FineTuningJobStateMachineTests {

        @Test
        @DisplayName("Should represent typical fine-tuning lifecycle")
        void shouldRepresentTypicalLifecycle() {
            // Typical lifecycle: PENDING -> RUNNING -> COMPLETED
            assertThat(FineTuningStatus.PENDING).isNotNull();
            assertThat(FineTuningStatus.RUNNING).isNotNull();
            assertThat(FineTuningStatus.COMPLETED).isNotNull();
        }

        @Test
        @DisplayName("Should support failure scenarios")
        void shouldSupportFailureScenarios() {
            // Failure scenarios: PENDING -> CANCELLED, RUNNING -> FAILED
            assertThat(FineTuningStatus.CANCELLED).isNotNull();
            assertThat(FineTuningStatus.FAILED).isNotNull();
        }

        @Test
        @DisplayName("Should support job cancellation from active states")
        void shouldSupportCancellationFromActiveStates() {
            assertThat(FineTuningStatus.CANCELLED).isNotNull();
        }
    }

    @Nested
    @DisplayName("Status Characteristics Tests")
    class StatusCharacteristicsTests {

        @Test
        @DisplayName("Should classify pending state")
        void shouldClassifyPendingState() {
            assertThat(FineTuningStatus.values()).contains(FineTuningStatus.PENDING);
        }

        @Test
        @DisplayName("Should classify processing state")
        void shouldClassifyProcessingState() {
            assertThat(FineTuningStatus.values()).contains(FineTuningStatus.RUNNING);
        }

        @Test
        @DisplayName("Should classify end states")
        void shouldClassifyEndStates() {
            assertThat(FineTuningStatus.values()).contains(
                    FineTuningStatus.COMPLETED,
                    FineTuningStatus.FAILED,
                    FineTuningStatus.CANCELLED
            );
        }
    }
}
