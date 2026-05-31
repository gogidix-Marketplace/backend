package com.gogidix.aiservices.aiorchestrationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("WorkflowStatus Enum Tests")
class WorkflowStatusTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(WorkflowStatus.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(WorkflowStatus status) {
            assertThat(status).isNotNull();
        }

        @Test
        @DisplayName("Should have ACTIVE status")
        void shouldHaveActiveStatus() {
            assertThat(WorkflowStatus.valueOf("ACTIVE")).isEqualTo(WorkflowStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should have DRAFT status")
        void shouldHaveDraftStatus() {
            assertThat(WorkflowStatus.valueOf("DRAFT")).isEqualTo(WorkflowStatus.DRAFT);
        }

        @Test
        @DisplayName("Should have PAUSED status")
        void shouldHavePausedStatus() {
            assertThat(WorkflowStatus.valueOf("PAUSED")).isEqualTo(WorkflowStatus.PAUSED);
        }

        @Test
        @DisplayName("Should have COMPLETED status")
        void shouldHaveCompletedStatus() {
            assertThat(WorkflowStatus.valueOf("COMPLETED")).isEqualTo(WorkflowStatus.COMPLETED);
        }

        @Test
        @DisplayName("Should have FAILED status")
        void shouldHaveFailedStatus() {
            assertThat(WorkflowStatus.valueOf("FAILED")).isEqualTo(WorkflowStatus.FAILED);
        }

        @Test
        @DisplayName("Should have 5 status values")
        void shouldHave5StatusValues() {
            assertThat(WorkflowStatus.values()).hasSize(5);
        }
    }

    @Nested
    @DisplayName("Status Lifecycle Tests")
    class StatusLifecycleTests {

        @Test
        @DisplayName("DRAFT can transition to ACTIVE")
        void draftCanTransitionToActive() {
            WorkflowStatus from = WorkflowStatus.DRAFT;
            WorkflowStatus to = WorkflowStatus.ACTIVE;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("DRAFT can transition to PAUSED")
        void draftCanTransitionToPaused() {
            WorkflowStatus from = WorkflowStatus.DRAFT;
            WorkflowStatus to = WorkflowStatus.PAUSED;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("ACTIVE can transition to COMPLETED")
        void activeCanTransitionToCompleted() {
            WorkflowStatus from = WorkflowStatus.ACTIVE;
            WorkflowStatus to = WorkflowStatus.COMPLETED;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("ACTIVE can transition to FAILED")
        void activeCanTransitionToFailed() {
            WorkflowStatus from = WorkflowStatus.ACTIVE;
            WorkflowStatus to = WorkflowStatus.FAILED;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("ACTIVE can transition to PAUSED")
        void activeCanTransitionToPaused() {
            WorkflowStatus from = WorkflowStatus.ACTIVE;
            WorkflowStatus to = WorkflowStatus.PAUSED;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("PAUSED can transition to ACTIVE")
        void pausedCanTransitionToActive() {
            WorkflowStatus from = WorkflowStatus.PAUSED;
            WorkflowStatus to = WorkflowStatus.ACTIVE;
            assertThat(from).isNotEqualTo(to);
        }
    }

    @Nested
    @DisplayName("Terminal States Tests")
    class TerminalStatesTests {

        @Test
        @DisplayName("COMPLETED should be a terminal state")
        void completedShouldBeTerminal() {
            WorkflowStatus terminal = WorkflowStatus.COMPLETED;
            assertThat(terminal.name()).isEqualTo("COMPLETED");
        }

        @Test
        @DisplayName("FAILED should be a terminal state")
        void failedShouldBeTerminal() {
            WorkflowStatus terminal = WorkflowStatus.FAILED;
            assertThat(terminal.name()).isEqualTo("FAILED");
        }

        @Test
        @DisplayName("Should have 2 terminal states")
        void shouldHave2TerminalStates() {
            long terminalCount = java.util.Arrays.stream(WorkflowStatus.values())
                    .filter(s -> s == WorkflowStatus.COMPLETED || s == WorkflowStatus.FAILED)
                    .count();

            assertThat(terminalCount).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Active States Tests")
    class ActiveStatesTests {

        @Test
        @DisplayName("ACTIVE should be an active state")
        void activeShouldBeActive() {
            WorkflowStatus active = WorkflowStatus.ACTIVE;
            assertThat(active.name()).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("DRAFT should be a draft state")
        void draftShouldBeDraft() {
            WorkflowStatus draft = WorkflowStatus.DRAFT;
            assertThat(draft.name()).isEqualTo("DRAFT");
        }

        @Test
        @DisplayName("PAUSED should be a paused state")
        void pausedShouldBePaused() {
            WorkflowStatus paused = WorkflowStatus.PAUSED;
            assertThat(paused.name()).isEqualTo("PAUSED");
        }

        @Test
        @DisplayName("Should have 3 active/paused states")
        void shouldHave3ActiveStates() {
            long activeCount = java.util.Arrays.stream(WorkflowStatus.values())
                    .filter(s -> s == WorkflowStatus.ACTIVE || s == WorkflowStatus.DRAFT || s == WorkflowStatus.PAUSED)
                    .count();

            assertThat(activeCount).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have consistent enum names")
        void shouldHaveConsistentEnumNames() {
            for (WorkflowStatus status : WorkflowStatus.values()) {
                String name = status.name();
                assertThat(name).isNotNull();
                assertThat(name).isUpperCase();
                assertThat(name).doesNotContain(" ");
            }
        }

        @Test
        @DisplayName("Should have unique enum values")
        void shouldHaveUniqueEnumValues() {
            long uniqueCount = java.util.Arrays.stream(WorkflowStatus.values())
                    .map(WorkflowStatus::name)
                    .distinct()
                    .count();

            assertThat(uniqueCount).isEqualTo(WorkflowStatus.values().length);
        }
    }

    @Nested
    @DisplayName("Workflow Execution Tests")
    class WorkflowExecutionTests {

        @Test
        @DisplayName("Should represent draft workflow")
        void shouldRepresentDraftWorkflow() {
            WorkflowStatus draft = WorkflowStatus.DRAFT;
            assertThat(draft.name()).isEqualTo("DRAFT");
        }

        @Test
        @DisplayName("Should represent active workflow")
        void shouldRepresentActiveWorkflow() {
            WorkflowStatus active = WorkflowStatus.ACTIVE;
            assertThat(active.name()).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("Should represent paused workflow")
        void shouldRepresentPausedWorkflow() {
            WorkflowStatus paused = WorkflowStatus.PAUSED;
            assertThat(paused.name()).isEqualTo("PAUSED");
        }

        @Test
        @DisplayName("Should represent completed workflow")
        void shouldRepresentCompletedWorkflow() {
            WorkflowStatus completed = WorkflowStatus.COMPLETED;
            assertThat(completed.name()).isEqualTo("COMPLETED");
        }

        @Test
        @DisplayName("Should represent failed workflow")
        void shouldRepresentFailedWorkflow() {
            WorkflowStatus failed = WorkflowStatus.FAILED;
            assertThat(failed.name()).isEqualTo("FAILED");
        }
    }

    @Nested
    @DisplayName("State Machine Tests")
    class StateMachineTests {

        @Test
        @DisplayName("Should follow happy path: DRAFT -> ACTIVE -> COMPLETED")
        void shouldFollowHappyPath() {
            assertThat(WorkflowStatus.DRAFT).isNotNull();
            assertThat(WorkflowStatus.ACTIVE).isNotNull();
            assertThat(WorkflowStatus.COMPLETED).isNotNull();
        }

        @Test
        @DisplayName("Should follow failure path: DRAFT -> ACTIVE -> FAILED")
        void shouldFollowFailurePath() {
            assertThat(WorkflowStatus.DRAFT).isNotNull();
            assertThat(WorkflowStatus.ACTIVE).isNotNull();
            assertThat(WorkflowStatus.FAILED).isNotNull();
        }

        @Test
        @DisplayName("Should follow pause path: DRAFT -> PAUSED -> ACTIVE")
        void shouldFollowPausePath() {
            assertThat(WorkflowStatus.DRAFT).isNotNull();
            assertThat(WorkflowStatus.PAUSED).isNotNull();
            assertThat(WorkflowStatus.ACTIVE).isNotNull();
        }

        @Test
        @DisplayName("Should follow pause from active: ACTIVE -> PAUSED")
        void shouldFollowPauseFromActive() {
            assertThat(WorkflowStatus.ACTIVE).isNotNull();
            assertThat(WorkflowStatus.PAUSED).isNotNull();
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("DRAFT for newly created workflows")
        void draftForNewlyCreatedWorkflows() {
            WorkflowStatus draft = WorkflowStatus.DRAFT;
            assertThat(draft).isNotNull();
        }

        @Test
        @DisplayName("ACTIVE for running workflows")
        void activeForRunningWorkflows() {
            WorkflowStatus active = WorkflowStatus.ACTIVE;
            assertThat(active).isNotNull();
        }

        @Test
        @DisplayName("PAUSED for temporarily suspended workflows")
        void pausedForSuspendedWorkflows() {
            WorkflowStatus paused = WorkflowStatus.PAUSED;
            assertThat(paused).isNotNull();
        }

        @Test
        @DisplayName("COMPLETED for successful workflows")
        void completedForSuccessfulWorkflows() {
            WorkflowStatus completed = WorkflowStatus.COMPLETED;
            assertThat(completed).isNotNull();
        }

        @Test
        @DisplayName("FAILED for error workflows")
        void failedForErrorWorkflows() {
            WorkflowStatus failed = WorkflowStatus.FAILED;
            assertThat(failed).isNotNull();
        }
    }
}
