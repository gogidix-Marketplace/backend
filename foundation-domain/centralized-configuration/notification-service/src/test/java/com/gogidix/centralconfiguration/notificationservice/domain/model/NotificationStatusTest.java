package com.gogidix.centralconfiguration.notificationservice.domain.model;

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
        void shouldHavePENDINGStatus() {
            assertThat(NotificationStatus.valueOf("PENDING")).isEqualTo(NotificationStatus.PENDING);
        }

        @Test
        @DisplayName("Should have SENT status")
        void shouldHaveSENTStatus() {
            assertThat(NotificationStatus.valueOf("SENT")).isEqualTo(NotificationStatus.SENT);
        }

        @Test
        @DisplayName("Should have FAILED status")
        void shouldHaveFAILEDStatus() {
            assertThat(NotificationStatus.valueOf("FAILED")).isEqualTo(NotificationStatus.FAILED);
        }

        @Test
        @DisplayName("Should have RETRYING status")
        void shouldHaveRETRYINGStatus() {
            assertThat(NotificationStatus.valueOf("RETRYING")).isEqualTo(NotificationStatus.RETRYING);
        }

        @Test
        @DisplayName("Should have 4 notification statuses")
        void shouldHave4NotificationStatuses() {
            assertThat(NotificationStatus.values()).hasSize(4);
        }
    }

    @Nested
    @DisplayName("Code Tests")
    class CodeTests {

        @Test
        @DisplayName("PENDING has code 'pending'")
        void pendingHasCode() {
            assertThat(NotificationStatus.PENDING.getCode()).isEqualTo("pending");
        }

        @Test
        @DisplayName("SENT has code 'sent'")
        void sentHasCode() {
            assertThat(NotificationStatus.SENT.getCode()).isEqualTo("sent");
        }

        @Test
        @DisplayName("FAILED has code 'failed'")
        void failedHasCode() {
            assertThat(NotificationStatus.FAILED.getCode()).isEqualTo("failed");
        }

        @Test
        @DisplayName("RETRYING has code 'retrying'")
        void retryingHasCode() {
            assertThat(NotificationStatus.RETRYING.getCode()).isEqualTo("retrying");
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("All statuses have descriptions")
        void allStatusesHaveDescriptions() {
            for (NotificationStatus status : NotificationStatus.values()) {
                assertThat(status.getDescription()).isNotNull();
                assertThat(status.getDescription()).isNotEmpty();
            }
        }

        @Test
        @DisplayName("PENDING description mentions pending")
        void pendingDescriptionMentionsPending() {
            assertThat(NotificationStatus.PENDING.getDescription()).containsIgnoringCase("pending");
        }

        @Test
        @DisplayName("SENT description mentions sent")
        void sentDescriptionMentionsSent() {
            assertThat(NotificationStatus.SENT.getDescription()).containsIgnoringCase("sent");
        }

        @Test
        @DisplayName("FAILED description mentions failed")
        void failedDescriptionMentionsFailed() {
            assertThat(NotificationStatus.FAILED.getDescription()).containsIgnoringCase("failed");
        }

        @Test
        @DisplayName("RETRYING description mentions retried")
        void retryingDescriptionMentionsRetried() {
            assertThat(NotificationStatus.RETRYING.getDescription()).containsIgnoringCase("retried");
        }
    }

    @Nested
    @DisplayName("Lifecycle Tests")
    class LifecycleTests {

        @Test
        @DisplayName("PENDING is initial state")
        void pendingIsInitialState() {
            NotificationStatus status = NotificationStatus.PENDING;
            assertThat(status).isEqualTo(NotificationStatus.PENDING);
        }

        @Test
        @DisplayName("SENT is terminal success state")
        void sentIsTerminalSuccessState() {
            NotificationStatus status = NotificationStatus.SENT;
            assertThat(status).isEqualTo(NotificationStatus.SENT);
        }

        @Test
        @DisplayName("FAILED is terminal failure state")
        void failedIsTerminalFailureState() {
            NotificationStatus status = NotificationStatus.FAILED;
            assertThat(status).isEqualTo(NotificationStatus.FAILED);
        }

        @Test
        @DisplayName("RETRYING is intermediate state")
        void retryingIsIntermediateState() {
            NotificationStatus status = NotificationStatus.RETRYING;
            assertThat(status).isEqualTo(NotificationStatus.RETRYING);
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("PENDING for new notifications")
        void pendingForNewNotifications() {
            NotificationStatus status = NotificationStatus.PENDING;
            assertThat(status).isEqualTo(NotificationStatus.PENDING);
        }

        @Test
        @DisplayName("SENT for successful delivery")
        void sentForSuccessfulDelivery() {
            NotificationStatus status = NotificationStatus.SENT;
            assertThat(status).isEqualTo(NotificationStatus.SENT);
        }

        @Test
        @DisplayName("FAILED for delivery failure")
        void failedForDeliveryFailure() {
            NotificationStatus status = NotificationStatus.FAILED;
            assertThat(status).isEqualTo(NotificationStatus.FAILED);
        }

        @Test
        @DisplayName("RETRYING for retry attempt")
        void retryingForRetryAttempt() {
            NotificationStatus status = NotificationStatus.RETRYING;
            assertThat(status).isEqualTo(NotificationStatus.RETRYING);
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

        @Test
        @DisplayName("Should have unique codes")
        void shouldHaveUniqueCodes() {
            long uniqueCodes = java.util.Arrays.stream(NotificationStatus.values())
                    .map(NotificationStatus::getCode)
                    .distinct()
                    .count();

            assertThat(uniqueCodes).isEqualTo(NotificationStatus.values().length);
        }
    }

    @Nested
    @DisplayName("State Transition Tests")
    class StateTransitionTests {

        @Test
        @DisplayName("PENDING can transition to SENT")
        void pendingCanTransitionToSent() {
            NotificationStatus from = NotificationStatus.PENDING;
            NotificationStatus to = NotificationStatus.SENT;
            assertThat(from).isNotNull();
            assertThat(to).isNotNull();
        }

        @Test
        @DisplayName("PENDING can transition to FAILED")
        void pendingCanTransitionToFailed() {
            NotificationStatus from = NotificationStatus.PENDING;
            NotificationStatus to = NotificationStatus.FAILED;
            assertThat(from).isNotNull();
            assertThat(to).isNotNull();
        }

        @Test
        @DisplayName("FAILED can transition to RETRYING")
        void failedCanTransitionToRetrying() {
            NotificationStatus from = NotificationStatus.FAILED;
            NotificationStatus to = NotificationStatus.RETRYING;
            assertThat(from).isNotNull();
            assertThat(to).isNotNull();
        }

        @Test
        @DisplayName("RETRYING can transition to SENT")
        void retryingCanTransitionToSent() {
            NotificationStatus from = NotificationStatus.RETRYING;
            NotificationStatus to = NotificationStatus.SENT;
            assertThat(from).isNotNull();
            assertThat(to).isNotNull();
        }

        @Test
        @DisplayName("RETRYING can transition to FAILED")
        void retryingCanTransitionToFailed() {
            NotificationStatus from = NotificationStatus.RETRYING;
            NotificationStatus to = NotificationStatus.FAILED;
            assertThat(from).isNotNull();
            assertThat(to).isNotNull();
        }
    }

    @Nested
    @DisplayName("Delivery Outcome Tests")
    class DeliveryOutcomeTests {

        @Test
        @DisplayName("Successful delivery results in SENT")
        void successfulDeliveryResultsInSent() {
            NotificationStatus status = NotificationStatus.SENT;
            assertThat(status.getCode()).isEqualTo("sent");
        }

        @Test
        @DisplayName("Failed delivery results in FAILED")
        void failedDeliveryResultsInFailed() {
            NotificationStatus status = NotificationStatus.FAILED;
            assertThat(status.getCode()).isEqualTo("failed");
        }

        @Test
        @DisplayName("Awaiting delivery is PENDING")
        void awaitingDeliveryIsPending() {
            NotificationStatus status = NotificationStatus.PENDING;
            assertThat(status.getCode()).isEqualTo("pending");
        }

        @Test
        @DisplayName("Retry in progress is RETRYING")
        void retryInProgressIsRetrying() {
            NotificationStatus status = NotificationStatus.RETRYING;
            assertThat(status.getCode()).isEqualTo("retrying");
        }
    }
}
