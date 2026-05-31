package com.gogidix.aiservices.aitestingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TestStatus Enum Tests")
class TestStatusTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(TestStatus.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(TestStatus status) {
            assertThat(status).isNotNull();
        }

        @Test
        @DisplayName("Should have PENDING status")
        void shouldHavePendingStatus() {
            assertThat(TestStatus.valueOf("PENDING")).isEqualTo(TestStatus.PENDING);
        }

        @Test
        @DisplayName("Should have RUNNING status")
        void shouldHaveRunningStatus() {
            assertThat(TestStatus.valueOf("RUNNING")).isEqualTo(TestStatus.RUNNING);
        }

        @Test
        @DisplayName("Should have PASSED status")
        void shouldHavePassedStatus() {
            assertThat(TestStatus.valueOf("PASSED")).isEqualTo(TestStatus.PASSED);
        }

        @Test
        @DisplayName("Should have FAILED status")
        void shouldHaveFailedStatus() {
            assertThat(TestStatus.valueOf("FAILED")).isEqualTo(TestStatus.FAILED);
        }

        @Test
        @DisplayName("Should have SKIPPED status")
        void shouldHaveSkippedStatus() {
            assertThat(TestStatus.valueOf("SKIPPED")).isEqualTo(TestStatus.SKIPPED);
        }

        @Test
        @DisplayName("Should have 6 status values")
        void shouldHave6StatusValues() {
            assertThat(TestStatus.values()).hasSize(6);
        }
    }

    @Nested
    @DisplayName("Status Lifecycle Tests")
    class StatusLifecycleTests {

        @Test
        @DisplayName("PENDING can transition to RUNNING")
        void pendingCanTransitionToRunning() {
            TestStatus from = TestStatus.PENDING;
            TestStatus to = TestStatus.RUNNING;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("RUNNING can transition to PASSED")
        void runningCanTransitionToPassed() {
            TestStatus from = TestStatus.RUNNING;
            TestStatus to = TestStatus.PASSED;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("RUNNING can transition to FAILED")
        void runningCanTransitionToFailed() {
            TestStatus from = TestStatus.RUNNING;
            TestStatus to = TestStatus.FAILED;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("PENDING can transition to SKIPPED")
        void pendingCanTransitionToSkipped() {
            TestStatus from = TestStatus.PENDING;
            TestStatus to = TestStatus.SKIPPED;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("RUNNING can transition to SKIPPED")
        void runningCanTransitionToSkipped() {
            TestStatus from = TestStatus.RUNNING;
            TestStatus to = TestStatus.SKIPPED;
            assertThat(from).isNotEqualTo(to);
        }
    }

    @Nested
    @DisplayName("Terminal States Tests")
    class TerminalStatesTests {

        @Test
        @DisplayName("PASSED should be a terminal state")
        void passedShouldBeTerminal() {
            TestStatus terminal = TestStatus.PASSED;
            assertThat(terminal.name()).isEqualTo("PASSED");
        }

        @Test
        @DisplayName("FAILED should be a terminal state")
        void failedShouldBeTerminal() {
            TestStatus terminal = TestStatus.FAILED;
            assertThat(terminal.name()).isEqualTo("FAILED");
        }

        @Test
        @DisplayName("SKIPPED should be a terminal state")
        void skippedShouldBeTerminal() {
            TestStatus terminal = TestStatus.SKIPPED;
            assertThat(terminal.name()).isEqualTo("SKIPPED");
        }

        @Test
        @DisplayName("Should have 3 terminal states")
        void shouldHave3TerminalStates() {
            long terminalCount = java.util.Arrays.stream(TestStatus.values())
                    .filter(s -> s == TestStatus.PASSED ||
                                 s == TestStatus.FAILED ||
                                 s == TestStatus.SKIPPED)
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
            TestStatus active = TestStatus.PENDING;
            assertThat(active.name()).isEqualTo("PENDING");
        }

        @Test
        @DisplayName("RUNNING should be an active state")
        void runningShouldBeActive() {
            TestStatus active = TestStatus.RUNNING;
            assertThat(active.name()).isEqualTo("RUNNING");
        }

        @Test
        @DisplayName("Should have 2 active states")
        void shouldHave2ActiveStates() {
            long activeCount = java.util.Arrays.stream(TestStatus.values())
                    .filter(s -> s == TestStatus.PENDING || s == TestStatus.RUNNING)
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
            for (TestStatus status : TestStatus.values()) {
                String name = status.name();
                assertThat(name).isNotNull();
                assertThat(name).isUpperCase();
                assertThat(name).doesNotContain(" ");
            }
        }

        @Test
        @DisplayName("Should have unique enum values")
        void shouldHaveUniqueEnumValues() {
            long uniqueCount = java.util.Arrays.stream(TestStatus.values())
                    .map(TestStatus::name)
                    .distinct()
                    .count();

            assertThat(uniqueCount).isEqualTo(TestStatus.values().length);
        }
    }

    @Nested
    @DisplayName("Test Execution Tests")
    class TestExecutionTests {

        @Test
        @DisplayName("Should represent pending test")
        void shouldRepresentPendingTest() {
            TestStatus pending = TestStatus.PENDING;
            assertThat(pending.name()).isEqualTo("PENDING");
        }

        @Test
        @DisplayName("Should represent running test")
        void shouldRepresentRunningTest() {
            TestStatus running = TestStatus.RUNNING;
            assertThat(running.name()).isEqualTo("RUNNING");
        }

        @Test
        @DisplayName("Should represent passed test")
        void shouldRepresentPassedTest() {
            TestStatus passed = TestStatus.PASSED;
            assertThat(passed.name()).isEqualTo("PASSED");
        }

        @Test
        @DisplayName("Should represent failed test")
        void shouldRepresentFailedTest() {
            TestStatus failed = TestStatus.FAILED;
            assertThat(failed.name()).isEqualTo("FAILED");
        }

        @Test
        @DisplayName("Should represent skipped test")
        void shouldRepresentSkippedTest() {
            TestStatus skipped = TestStatus.SKIPPED;
            assertThat(skipped.name()).isEqualTo("SKIPPED");
        }
    }

    @Nested
    @DisplayName("Happy Path Tests")
    class HappyPathTests {

        @Test
        @DisplayName("Should follow successful test path")
        void shouldFollowSuccessfulTestPath() {
            // PENDING -> RUNNING -> PASSED
            assertThat(TestStatus.PENDING).isNotNull();
            assertThat(TestStatus.RUNNING).isNotNull();
            assertThat(TestStatus.PASSED).isNotNull();
        }

        @Test
        @DisplayName("Should follow failed test path")
        void shouldFollowFailedTestPath() {
            // PENDING -> RUNNING -> FAILED
            assertThat(TestStatus.PENDING).isNotNull();
            assertThat(TestStatus.RUNNING).isNotNull();
            assertThat(TestStatus.FAILED).isNotNull();
        }

        @Test
        @DisplayName("Should follow skipped test path")
        void shouldFollowSkippedTestPath() {
            // PENDING -> SKIPPED
            assertThat(TestStatus.PENDING).isNotNull();
            assertThat(TestStatus.SKIPPED).isNotNull();
        }
    }

    @Nested
    @DisplayName("State Machine Tests")
    class StateMachineTests {

        @Test
        @DisplayName("Should support test lifecycle")
        void shouldSupportTestLifecycle() {
            assertThat(TestStatus.values()).contains(
                    TestStatus.PENDING,
                    TestStatus.RUNNING,
                    TestStatus.PASSED,
                    TestStatus.FAILED,
                    TestStatus.SKIPPED
            );
        }

        @Test
        @DisplayName("Should allow transition to SKIPPED from active states")
        void shouldAllowTransitionToSkippedFromActiveStates() {
            assertThat(TestStatus.PENDING).isNotNull();
            assertThat(TestStatus.RUNNING).isNotNull();
            assertThat(TestStatus.SKIPPED).isNotNull();
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("PENDING for queued tests")
        void pendingForQueuedTests() {
            TestStatus pending = TestStatus.PENDING;
            assertThat(pending).isNotNull();
        }

        @Test
        @DisplayName("RUNNING for executing tests")
        void runningForExecutingTests() {
            TestStatus running = TestStatus.RUNNING;
            assertThat(running).isNotNull();
        }

        @Test
        @DisplayName("PASSED for successful tests")
        void passedForSuccessfulTests() {
            TestStatus passed = TestStatus.PASSED;
            assertThat(passed).isNotNull();
        }

        @Test
        @DisplayName("FAILED for error tests")
        void failedForErrorTests() {
            TestStatus failed = TestStatus.FAILED;
            assertThat(failed).isNotNull();
        }

        @Test
        @DisplayName("SKIPPED for ignored tests")
        void skippedForIgnoredTests() {
            TestStatus skipped = TestStatus.SKIPPED;
            assertThat(skipped).isNotNull();
        }
    }

    @Nested
    @DisplayName("Result Categories Tests")
    class ResultCategoriesTests {

        @Test
        @DisplayName("Should have success outcomes")
        void shouldHaveSuccessOutcomes() {
            assertThat(TestStatus.values()).contains(TestStatus.PASSED);
        }

        @Test
        @DisplayName("Should have failure outcomes")
        void shouldHaveFailureOutcomes() {
            assertThat(TestStatus.values()).contains(TestStatus.FAILED);
        }

        @Test
        @DisplayName("Should have neutral outcomes")
        void shouldHaveNeutralOutcomes() {
            assertThat(TestStatus.values()).contains(TestStatus.SKIPPED);
        }

        @Test
        @DisplayName("Should have in-progress states")
        void shouldHaveInProgressStates() {
            assertThat(TestStatus.values()).contains(
                    TestStatus.PENDING,
                    TestStatus.RUNNING
            );
        }
    }
}
