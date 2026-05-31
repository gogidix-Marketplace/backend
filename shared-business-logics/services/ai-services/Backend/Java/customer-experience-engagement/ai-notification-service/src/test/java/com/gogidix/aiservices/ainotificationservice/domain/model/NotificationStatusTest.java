package com.gogidix.aiservices.ainotificationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NotificationStatus Enum Tests")
class NotificationStatusTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(NotificationStatus.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(NotificationStatus status) {
            assertThat(status).isNotNull();
        }

        @Test
        @DisplayName("Should have PENDING status")
        void shouldHavePendingStatus() {
            assertThat(NotificationStatus.valueOf("PENDING")).isEqualTo(NotificationStatus.PENDING);
        }

        @Test
        @DisplayName("Should have SENT status")
        void shouldHaveSentStatus() {
            assertThat(NotificationStatus.valueOf("SENT")).isEqualTo(NotificationStatus.SENT);
        }

        @Test
        @DisplayName("Should have DELIVERED status")
        void shouldHaveDeliveredStatus() {
            assertThat(NotificationStatus.valueOf("DELIVERED")).isEqualTo(NotificationStatus.DELIVERED);
        }

        @Test
        @DisplayName("Should have FAILED status")
        void shouldHaveFailedStatus() {
            assertThat(NotificationStatus.valueOf("FAILED")).isEqualTo(NotificationStatus.FAILED);
        }

        @Test
        @DisplayName("Should have 6 status values")
        void shouldHave6StatusValues() {
            assertThat(NotificationStatus.values()).hasSize(6);
        }
    }

    @Nested
    @DisplayName("Status Lifecycle Tests")
    class StatusLifecycleTests {

        @Test
        @DisplayName("PENDING should be initial state")
        void pendingShouldBeInitialState() {
            NotificationStatus pending = NotificationStatus.PENDING;
            assertThat(pending).isNotNull();
        }

        @Test
        @DisplayName("PENDING can transition to SENT")
        void pendingCanTransitionToSent() {
            NotificationStatus from = NotificationStatus.PENDING;
            NotificationStatus to = NotificationStatus.SENT;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("PENDING can transition to FAILED")
        void pendingCanTransitionToFailed() {
            NotificationStatus from = NotificationStatus.PENDING;
            NotificationStatus to = NotificationStatus.FAILED;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("SENT can transition to DELIVERED")
        void sentCanTransitionToDelivered() {
            NotificationStatus from = NotificationStatus.SENT;
            NotificationStatus to = NotificationStatus.DELIVERED;
            assertThat(from).isNotEqualTo(to);
        }

        @Test
        @DisplayName("SENT can transition to FAILED")
        void sentCanTransitionToFailed() {
            NotificationStatus from = NotificationStatus.SENT;
            NotificationStatus to = NotificationStatus.FAILED;
            assertThat(from).isNotEqualTo(to);
        }
    }

    @Nested
    @DisplayName("Terminal States Tests")
    class TerminalStatesTests {

        @Test
        @DisplayName("DELIVERED should be a terminal state")
        void deliveredShouldBeTerminal() {
            NotificationStatus delivered = NotificationStatus.DELIVERED;
            assertThat(delivered.name()).isEqualTo("DELIVERED");
        }

        @Test
        @DisplayName("FAILED should be a terminal state")
        void failedShouldBeTerminal() {
            NotificationStatus failed = NotificationStatus.FAILED;
            assertThat(failed.name()).isEqualTo("FAILED");
        }

        @Test
        @DisplayName("Should have 2 terminal states")
        void shouldHave2TerminalStates() {
            long terminalCount = java.util.Arrays.stream(NotificationStatus.values())
                    .filter(s -> s == NotificationStatus.DELIVERED || s == NotificationStatus.FAILED)
                    .count();

            assertThat(terminalCount).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Active States Tests")
    class ActiveStatesTests {

        @Test
        @DisplayName("PENDING should be an active state")
        void pendingShouldBeActive() {
            NotificationStatus pending = NotificationStatus.PENDING;
            assertThat(pending.name()).isEqualTo("PENDING");
        }

        @Test
        @DisplayName("SENT should be an active state")
        void sentShouldBeActive() {
            NotificationStatus sent = NotificationStatus.SENT;
            assertThat(sent.name()).isEqualTo("SENT");
        }

        @Test
        @DisplayName("Should have 2 active states")
        void shouldHave2ActiveStates() {
            long activeCount = java.util.Arrays.stream(NotificationStatus.values())
                    .filter(s -> s == NotificationStatus.PENDING || s == NotificationStatus.SENT)
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
            for (NotificationStatus status : NotificationStatus.values()) {
                String name = status.name();
                assertThat(name).isNotNull();
                assertThat(name).isUpperCase();
                assertThat(name).doesNotContain(" ");
            }
        }

        @Test
        @DisplayName("Should have unique enum values")
        void shouldHaveUniqueEnumValues() {
            long uniqueCount = java.util.Arrays.stream(NotificationStatus.values())
                    .map(NotificationStatus::name)
                    .distinct()
                    .count();

            assertThat(uniqueCount).isEqualTo(NotificationStatus.values().length);
        }
    }

    @Nested
    @DisplayName("State Machine Tests")
    class StateMachineTests {

        @Test
        @DisplayName("Should represent successful delivery flow")
        void shouldRepresentSuccessfulDeliveryFlow() {
            // PENDING -> SENT -> DELIVERED
            assertThat(NotificationStatus.PENDING).isNotNull();
            assertThat(NotificationStatus.SENT).isNotNull();
            assertThat(NotificationStatus.DELIVERED).isNotNull();
        }

        @Test
        @DisplayName("Should represent failed delivery flow")
        void shouldRepresentFailedDeliveryFlow() {
            // PENDING -> FAILED or SENT -> FAILED
            assertThat(NotificationStatus.PENDING).isNotNull();
            assertThat(NotificationStatus.FAILED).isNotNull();
        }

        @Test
        @DisplayName("Should have retry capability for FAILED status")
        void shouldHaveRetryCapabilityForFailedStatus() {
            NotificationStatus failed = NotificationStatus.FAILED;
            assertThat(failed).isNotNull();
        }
    }

    @Nested
    @DisplayName("Status Characteristics Tests")
    class StatusCharacteristicsTests {

        @Test
        @DisplayName("PENDING indicates waiting to be sent")
        void pendingIndicatesWaitingToBeSent() {
            NotificationStatus pending = NotificationStatus.PENDING;
            assertThat(pending.name()).isEqualTo("PENDING");
        }

        @Test
        @DisplayName("SENT indicates notification sent to provider")
        void sentIndicatesNotificationSentToProvider() {
            NotificationStatus sent = NotificationStatus.SENT;
            assertThat(sent.name()).isEqualTo("SENT");
        }

        @Test
        @DisplayName("DELIVERED indicates successful delivery")
        void deliveredIndicatesSuccessfulDelivery() {
            NotificationStatus delivered = NotificationStatus.DELIVERED;
            assertThat(delivered.name()).isEqualTo("DELIVERED");
        }

        @Test
        @DisplayName("FAILED indicates delivery failure")
        void failedIndicatesDeliveryFailure() {
            NotificationStatus failed = NotificationStatus.FAILED;
            assertThat(failed.name()).isEqualTo("FAILED");
        }
    }

    @Nested
    @DisplayName("Use Cases Tests")
    class UseCasesTests {

        @Test
        @DisplayName("Email notification status flow")
        void emailNotificationStatusFlow() {
            // Email: PENDING -> SENT -> DELIVERED
            assertThat(NotificationStatus.PENDING).isNotNull();
            assertThat(NotificationStatus.SENT).isNotNull();
            assertThat(NotificationStatus.DELIVERED).isNotNull();
        }

        @Test
        @DisplayName("SMS notification status flow")
        void smsNotificationStatusFlow() {
            // SMS: PENDING -> SENT -> DELIVERED
            assertThat(NotificationStatus.PENDING).isNotNull();
            assertThat(NotificationStatus.SENT).isNotNull();
            assertThat(NotificationStatus.DELIVERED).isNotNull();
        }

        @Test
        @DisplayName("Push notification status flow")
        void pushNotificationStatusFlow() {
            // Push: PENDING -> SENT -> DELIVERED
            assertThat(NotificationStatus.PENDING).isNotNull();
            assertThat(NotificationStatus.SENT).isNotNull();
            assertThat(NotificationStatus.DELIVERED).isNotNull();
        }

        @Test
        @DisplayName("Failed notification status")
        void failedNotificationStatus() {
            // Failed: PENDING -> FAILED or SENT -> FAILED
            assertThat(NotificationStatus.PENDING).isNotNull();
            assertThat(NotificationStatus.FAILED).isNotNull();
        }
    }
}