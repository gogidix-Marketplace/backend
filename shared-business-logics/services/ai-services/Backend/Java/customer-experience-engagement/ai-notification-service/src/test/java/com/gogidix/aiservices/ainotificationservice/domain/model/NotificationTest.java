package com.gogidix.aiservices.ainotificationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Notification Domain Model Tests")
class NotificationTest {

    private static final String RECIPIENT_ID = "user-123";

    @Nested
    @DisplayName("Notification Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create notification with valid parameters")
        void shouldCreateWithValidParameters() {
            Notification notification = Notification.create(
                    RECIPIENT_ID,
                    NotificationType.EMAIL,
                    "Test Subject",
                    "Test Content"
            );

            assertThat(notification).isNotNull();
            assertThat(notification.getRecipientId()).isEqualTo(RECIPIENT_ID);
            assertThat(notification.getType()).isEqualTo(NotificationType.EMAIL);
            assertThat(notification.getSubject()).isEqualTo("Test Subject");
            assertThat(notification.getContent()).isEqualTo("Test Content");
            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.PENDING);
        }

        @Test
        @DisplayName("Should generate unique notification ID")
        void shouldGenerateUniqueId() {
            Notification notification1 = Notification.create(RECIPIENT_ID, NotificationType.EMAIL, "Subj", "Content");
            Notification notification2 = Notification.create(RECIPIENT_ID, NotificationType.EMAIL, "Subj", "Content");

            assertThat(notification1.getNotificationId()).isNotEqualTo(notification2.getNotificationId());
        }

        @Test
        @DisplayName("Should reject null recipient ID")
        void shouldRejectNullRecipientId() {
            assertThatThrownBy(() -> Notification.builder()
                    .notificationId("n1")
                    .recipientId(null)
                    .type(NotificationType.EMAIL)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should reject null notification type")
        void shouldRejectNullType() {
            assertThatThrownBy(() -> Notification.builder()
                    .notificationId("n1")
                    .recipientId(RECIPIENT_ID)
                    .type(null)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Status Management Tests")
    class StatusTests {

        @Test
        @DisplayName("Should mark as sent")
        void shouldMarkAsSent() {
            Notification notification = Notification.create(
                    RECIPIENT_ID, NotificationType.EMAIL, "Subject", "Content"
            );

            notification.markAsSent();

            assertThat(notification.isSent()).isTrue();
            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
            assertThat(notification.getSentAt()).isNotNull();
        }

        @Test
        @DisplayName("Should mark as delivered")
        void shouldMarkAsDelivered() {
            Notification notification = Notification.create(
                    RECIPIENT_ID, NotificationType.EMAIL, "Subject", "Content"
            );

            notification.markAsDelivered();

            assertThat(notification.isDelivered()).isTrue();
            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.DELIVERED);
        }

        @Test
        @DisplayName("Should mark as failed")
        void shouldMarkAsFailed() {
            Notification notification = Notification.create(
                    RECIPIENT_ID, NotificationType.EMAIL, "Subject", "Content"
            );

            notification.markAsFailed("Delivery error");

            assertThat(notification.isFailed()).isTrue();
            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.FAILED);
        }

        @Test
        @DisplayName("Should identify pending status")
        void shouldIdentifyPending() {
            Notification notification = Notification.create(
                    RECIPIENT_ID, NotificationType.EMAIL, "Subject", "Content"
            );

            assertThat(notification.isPending()).isTrue();
            assertThat(notification.isSent()).isFalse();
        }
    }

    @Nested
    @DisplayName("Retry Logic Tests")
    class RetryTests {

        @Test
        @DisplayName("Should allow retry within limit")
        void shouldAllowRetryWithinLimit() {
            Notification notification = Notification.builder()
                    .notificationId("n1")
                    .recipientId(RECIPIENT_ID)
                    .type(NotificationType.EMAIL)
                    .subject("Subject")
                    .content("Content")
                    .status(NotificationStatus.FAILED)
                    .retryCount(2)
                    .build();

            assertThat(notification.canRetry()).isTrue();
        }

        @Test
        @DisplayName("Should not allow retry beyond limit")
        void shouldNotAllowRetryBeyondLimit() {
            Notification notification = Notification.builder()
                    .notificationId("n1")
                    .recipientId(RECIPIENT_ID)
                    .type(NotificationType.EMAIL)
                    .subject("Subject")
                    .content("Content")
                    .status(NotificationStatus.FAILED)
                    .retryCount(3)
                    .build();

            assertThat(notification.canRetry()).isFalse();
        }
    }

    @Nested
    @DisplayName("Scheduling Tests")
    class SchedulingTests {

        @Test
        @DisplayName("Should identify scheduled notification")
        void shouldIdentifyScheduled() {
            Notification notification = Notification.builder()
                    .notificationId("n1")
                    .recipientId(RECIPIENT_ID)
                    .type(NotificationType.EMAIL)
                    .subject("Subject")
                    .content("Content")
                    .scheduledFor(Instant.now().plusSeconds(60))
                    .build();

            assertThat(notification.isScheduled()).isTrue();
        }

        @Test
        @DisplayName("Should identify expired notification")
        void shouldIdentifyExpired() {
            Notification notification = Notification.builder()
                    .notificationId("n1")
                    .recipientId(RECIPIENT_ID)
                    .type(NotificationType.EMAIL)
                    .subject("Subject")
                    .content("Content")
                    .expiresAt(Instant.now().minusSeconds(60))
                    .build();

            assertThat(notification.isExpired()).isTrue();
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal based on notification ID")
        void shouldBeEqualBasedOnId() {
            Notification notification1 = Notification.builder()
                    .notificationId("n1")
                    .recipientId(RECIPIENT_ID)
                    .type(NotificationType.EMAIL)
                    .build();

            Notification notification2 = Notification.builder()
                    .notificationId("n1")
                    .recipientId("different-user")
                    .type(NotificationType.SMS)
                    .build();

            assertThat(notification1).isEqualTo(notification2);
        }
    }
}
