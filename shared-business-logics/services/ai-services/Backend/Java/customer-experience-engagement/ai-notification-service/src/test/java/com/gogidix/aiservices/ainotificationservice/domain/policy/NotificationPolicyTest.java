package com.gogidix.aiservices.ainotificationservice.domain.policy;

import com.gogidix.aiservices.ainotificationservice.domain.model.Notification;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationPriority;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NotificationPolicy Domain Policy Tests")
class NotificationPolicyTest {

    private NotificationPolicy policy;
    private Notification notification;

    @BeforeEach
    void setUp() {
        policy = new NotificationPolicy();
        notification = Notification.create(
                "user-123",
                NotificationType.EMAIL,
                "Test Subject",
                "Test Content"
        );
    }

    @Nested
    @DisplayName("validateNotification() Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate valid notification")
        void shouldValidateValidNotification() {
            assertDoesNotThrow(() -> policy.validateNotification(notification));
        }

        @Test
        @DisplayName("Should throw exception when content exceeds max length")
        void shouldThrowExceptionWhenContentTooLong() {
            String longContent = "A".repeat(5001);
            Notification longNotification = Notification.create(
                    "user-123",
                    NotificationType.EMAIL,
                    "Subject",
                    longContent
            );

            assertThatThrownBy(() -> policy.validateNotification(longNotification))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Content exceeds maximum length");
        }

        @Test
        @DisplayName("Should throw exception when subject exceeds max length")
        void shouldThrowExceptionWhenSubjectTooLong() {
            Notification longSubjectNotification = Notification.create(
                    "user-123",
                    NotificationType.EMAIL,
                    "A".repeat(201),
                    "Content"
            );

            assertThatThrownBy(() -> policy.validateNotification(longSubjectNotification))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Subject exceeds maximum length");
        }

        @Test
        @DisplayName("Should allow content at maximum length")
        void shouldAllowContentAtMaxLength() {
            Notification maxContentNotification = Notification.create(
                    "user-123",
                    NotificationType.EMAIL,
                    "Subject",
                    "A".repeat(5000)
            );

            assertDoesNotThrow(() -> policy.validateNotification(maxContentNotification));
        }

        @Test
        @DisplayName("Should allow subject at maximum length")
        void shouldAllowSubjectAtMaxLength() {
            Notification maxSubjectNotification = Notification.create(
                    "user-123",
                    NotificationType.EMAIL,
                    "A".repeat(200),
                    "Content"
            );

            assertDoesNotThrow(() -> policy.validateNotification(maxSubjectNotification));
        }

        @Test
        @DisplayName("Should validate notification with null content")
        void shouldAllowNullContent() {
            Notification nullContentNotification = Notification.builder()
                    .notificationId("notif-1")
                    .recipientId("user-123")
                    .type(NotificationType.EMAIL)
                    .subject("Subject")
                    .content(null)
                    .build();

            assertDoesNotThrow(() -> policy.validateNotification(nullContentNotification));
        }
    }

    @Nested
    @DisplayName("canSendNow() Tests")
    class CanSendNowTests {

        @Test
        @DisplayName("Should allow sending when not scheduled and not expired")
        void shouldAllowSendingWhenNotScheduled() {
            assertThat(policy.canSendNow(notification)).isTrue();
        }

        @Test
        @DisplayName("Should not allow sending when scheduled for future")
        void shouldNotAllowSendingWhenScheduledForFuture() {
            Notification scheduledNotification = Notification.builder()
                    .notificationId("notif-1")
                    .recipientId("user-123")
                    .type(NotificationType.EMAIL)
                    .subject("Subject")
                    .content("Content")
                    .scheduledFor(Instant.now().plusSeconds(3600))
                    .build();

            assertThat(policy.canSendNow(scheduledNotification)).isFalse();
        }

        @Test
        @DisplayName("Should allow sending when scheduled time has passed")
        void shouldAllowSendingWhenScheduledTimePassed() {
            Notification pastScheduledNotification = Notification.builder()
                    .notificationId("notif-1")
                    .recipientId("user-123")
                    .type(NotificationType.EMAIL)
                    .subject("Subject")
                    .content("Content")
                    .scheduledFor(Instant.now().minusSeconds(60))
                    .build();

            assertThat(policy.canSendNow(pastScheduledNotification)).isTrue();
        }

        @Test
        @DisplayName("Should not allow sending when expired")
        void shouldNotAllowSendingWhenExpired() {
            Notification expiredNotification = Notification.builder()
                    .notificationId("notif-1")
                    .recipientId("user-123")
                    .type(NotificationType.EMAIL)
                    .subject("Subject")
                    .content("Content")
                    .expiresAt(Instant.now().minusSeconds(60))
                    .build();

            assertThat(policy.canSendNow(expiredNotification)).isFalse();
        }
    }

    @Nested
    @DisplayName("shouldRetry() Tests")
    class ShouldRetryTests {

        @Test
        @DisplayName("Should allow retry when failed and retry count below max")
        void shouldAllowRetry() {
            Notification failedNotification = Notification.builder()
                    .notificationId("notif-1")
                    .recipientId("user-123")
                    .type(NotificationType.EMAIL)
                    .subject("Subject")
                    .content("Content")
                    .status(com.gogidix.aiservices.ainotificationservice.domain.model.NotificationStatus.FAILED)
                    .retryCount(1)
                    .build();

            assertThat(policy.shouldRetry(failedNotification)).isTrue();
        }

        @Test
        @DisplayName("Should not allow retry when max retries exceeded")
        void shouldNotAllowRetryWhenMaxExceeded() {
            Notification maxRetriesNotification = Notification.builder()
                    .notificationId("notif-1")
                    .recipientId("user-123")
                    .type(NotificationType.EMAIL)
                    .subject("Subject")
                    .content("Content")
                    .status(com.gogidix.aiservices.ainotificationservice.domain.model.NotificationStatus.FAILED)
                    .retryCount(4)
                    .build();

            assertThat(policy.shouldRetry(maxRetriesNotification)).isFalse();
        }

        @Test
        @DisplayName("Should not allow retry when not failed")
        void shouldNotAllowRetryWhenNotFailed() {
            assertThat(policy.shouldRetry(notification)).isFalse();
        }
    }

    @Nested
    @DisplayName("calculateRetryDelay() Tests")
    class CalculateRetryDelayTests {

        @Test
        @DisplayName("Should calculate exponential backoff")
        void shouldCalculateExponentialBackoff() {
            assertThat(policy.calculateRetryDelay(0)).isEqualTo(5);
            assertThat(policy.calculateRetryDelay(1)).isEqualTo(10);
            assertThat(policy.calculateRetryDelay(2)).isEqualTo(20);
            assertThat(policy.calculateRetryDelay(3)).isEqualTo(40);
        }

        @Test
        @DisplayName("Should cap retry delay at maximum")
        void shouldCapRetryDelayAtMaximum() {
            int delay = policy.calculateRetryDelay(10);
            assertThat(delay).isEqualTo(300);
        }
    }

    @Nested
    @DisplayName("prioritizeNotifications() Tests")
    class PrioritizeNotificationsTests {

        @Test
        @DisplayName("Should sort by priority descending")
        void shouldSortByPriority() {
            Notification low = Notification.builder()
                    .notificationId("notif-1")
                    .recipientId("user-123")
                    .type(NotificationType.EMAIL)
                    .subject("Low")
                    .content("Content")
                    .priority(NotificationPriority.LOW)
                    .createdAt(Instant.now())
                    .build();

            Notification high = Notification.builder()
                    .notificationId("notif-2")
                    .recipientId("user-123")
                    .type(NotificationType.EMAIL)
                    .subject("High")
                    .content("Content")
                    .priority(NotificationPriority.HIGH)
                    .createdAt(Instant.now())
                    .build();

            List<Notification> result = policy.prioritizeNotifications(List.of(low, high));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getPriority()).isEqualTo(NotificationPriority.HIGH);
            assertThat(result.get(1).getPriority()).isEqualTo(NotificationPriority.LOW);
        }

        @Test
        @DisplayName("Should sort by creation time when priorities equal")
        void shouldSortByCreationTime() {
            Instant now = Instant.now();
            Notification older = Notification.builder()
                    .notificationId("notif-1")
                    .recipientId("user-123")
                    .type(NotificationType.EMAIL)
                    .subject("Older")
                    .content("Content")
                    .priority(NotificationPriority.NORMAL)
                    .createdAt(now.minusSeconds(100))
                    .build();

            Notification newer = Notification.builder()
                    .notificationId("notif-2")
                    .recipientId("user-123")
                    .type(NotificationType.EMAIL)
                    .subject("Newer")
                    .content("Content")
                    .priority(NotificationPriority.NORMAL)
                    .createdAt(now.minusSeconds(50))
                    .build();

            List<Notification> result = policy.prioritizeNotifications(List.of(newer, older));

            assertThat(result.get(0).getSubject()).isEqualTo("Older");
            assertThat(result.get(1).getSubject()).isEqualTo("Newer");
        }
    }

    @Nested
    @DisplayName("rateLimitAllowed() Tests")
    class RateLimitAllowedTests {

        @Test
        @DisplayName("Should allow sending when no previous send")
        void shouldAllowWhenNoPreviousSend() {
            assertThat(policy.rateLimitAllowed("user-123", null)).isTrue();
        }

        @Test
        @DisplayName("Should allow sending after minimum interval")
        void shouldAllowAfterMinInterval() {
            Instant sixSecondsAgo = Instant.now().minusSeconds(6);
            assertThat(policy.rateLimitAllowed("user-123", sixSecondsAgo)).isTrue();
        }

        @Test
        @DisplayName("Should not allow sending within minimum interval")
        void shouldNotAllowWithinMinInterval() {
            Instant threeSecondsAgo = Instant.now().minusSeconds(3);
            assertThat(policy.rateLimitAllowed("user-123", threeSecondsAgo)).isFalse();
        }
    }

    @Nested
    @DisplayName("requiresTemplate() Tests")
    class RequiresTemplateTests {

        @Test
        @DisplayName("Should require template for EMAIL type")
        void shouldRequireTemplateForEmail() {
            assertThat(policy.requiresTemplate(NotificationType.EMAIL)).isTrue();
        }

        @Test
        @DisplayName("Should not require template for SMS type")
        void shouldNotRequireTemplateForSms() {
            assertThat(policy.requiresTemplate(NotificationType.SMS)).isFalse();
        }

        @Test
        @DisplayName("Should not require template for PUSH type")
        void shouldNotRequireTemplateForPush() {
            assertThat(policy.requiresTemplate(NotificationType.PUSH)).isFalse();
        }
    }

    @Nested
    @DisplayName("getMaxRetries() Tests")
    class GetMaxRetriesTests {

        @Test
        @DisplayName("Should return max retries")
        void shouldReturnMaxRetries() {
            assertThat(policy.getMaxRetries()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("isHighPriority() Tests")
    class IsHighPriorityTests {

        @Test
        @DisplayName("Should return true for HIGH priority")
        void shouldReturnTrueForHighPriority() {
            Notification highNotification = Notification.builder()
                    .notificationId("notif-1")
                    .recipientId("user-123")
                    .type(NotificationType.EMAIL)
                    .subject("Subject")
                    .content("Content")
                    .priority(NotificationPriority.HIGH)
                    .build();

            assertThat(policy.isHighPriority(highNotification)).isTrue();
        }

        @Test
        @DisplayName("Should return true for URGENT priority")
        void shouldReturnTrueForUrgentPriority() {
            Notification urgentNotification = Notification.builder()
                    .notificationId("notif-1")
                    .recipientId("user-123")
                    .type(NotificationType.EMAIL)
                    .subject("Subject")
                    .content("Content")
                    .priority(NotificationPriority.URGENT)
                    .build();

            assertThat(policy.isHighPriority(urgentNotification)).isTrue();
        }

        @Test
        @DisplayName("Should return false for NORMAL priority")
        void shouldReturnFalseForNormalPriority() {
            assertThat(policy.isHighPriority(notification)).isFalse();
        }

        @Test
        @DisplayName("Should return false for LOW priority")
        void shouldReturnFalseForLowPriority() {
            Notification lowNotification = Notification.builder()
                    .notificationId("notif-1")
                    .recipientId("user-123")
                    .type(NotificationType.EMAIL)
                    .subject("Subject")
                    .content("Content")
                    .priority(NotificationPriority.LOW)
                    .build();

            assertThat(policy.isHighPriority(lowNotification)).isFalse();
        }
    }
}
