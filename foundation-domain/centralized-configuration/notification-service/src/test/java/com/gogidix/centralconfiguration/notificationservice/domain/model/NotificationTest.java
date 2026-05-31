package com.gogidix.centralconfiguration.notificationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Notification Domain Entity Tests")
class NotificationTest {

    private static final Long ID = 1L;
    private static final String TENANT_ID = "tenant-001";
    private static final String RECIPIENT = "user@example.com";
    private static final String SUBJECT = "Test Notification";
    private static final String MESSAGE = "This is a test message";
    private static final String METADATA = "{\"key\":\"value\"}";

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            LocalDateTime now = LocalDateTime.now();

            Notification notification = Notification.builder()
                    .id(ID)
                    .tenantId(TENANT_ID)
                    .notificationType(NotificationType.CONFIG_CREATED)
                    .channel(NotificationChannel.EMAIL)
                    .recipient(RECIPIENT)
                    .subject(SUBJECT)
                    .message(MESSAGE)
                    .metadata(METADATA)
                    .status(NotificationStatus.SENT)
                    .retryCount(0)
                    .maxRetries(3)
                    .sentAt(now)
                    .errorMessage(null)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            assertThat(notification.getId()).isEqualTo(ID);
            assertThat(notification.getTenantId()).isEqualTo(TENANT_ID);
            assertThat(notification.getNotificationType()).isEqualTo(NotificationType.CONFIG_CREATED);
            assertThat(notification.getChannel()).isEqualTo(NotificationChannel.EMAIL);
            assertThat(notification.getRecipient()).isEqualTo(RECIPIENT);
            assertThat(notification.getSubject()).isEqualTo(SUBJECT);
            assertThat(notification.getMessage()).isEqualTo(MESSAGE);
            assertThat(notification.getMetadata()).isEqualTo(METADATA);
            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
            assertThat(notification.getRetryCount()).isEqualTo(0);
            assertThat(notification.getMaxRetries()).isEqualTo(3);
            assertThat(notification.getSentAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should build with required fields only")
        void shouldBuildWithRequiredFieldsOnly() {
            Notification notification = Notification.builder()
                    .tenantId(TENANT_ID)
                    .notificationType(NotificationType.SYSTEM_ALERT)
                    .channel(NotificationChannel.EMAIL)
                    .recipient(RECIPIENT)
                    .build();

            assertThat(notification.getTenantId()).isEqualTo(TENANT_ID);
            assertThat(notification.getNotificationType()).isEqualTo(NotificationType.SYSTEM_ALERT);
            assertThat(notification.getChannel()).isEqualTo(NotificationChannel.EMAIL);
            assertThat(notification.getRecipient()).isEqualTo(RECIPIENT);
        }

        @Test
        @DisplayName("Should set default values")
        void shouldSetDefaultValues() {
            Notification notification = Notification.builder()
                    .tenantId(TENANT_ID)
                    .notificationType(NotificationType.SYSTEM_ALERT)
                    .channel(NotificationChannel.EMAIL)
                    .recipient(RECIPIENT)
                    .build();

            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.PENDING);
            assertThat(notification.getRetryCount()).isEqualTo(0);
            assertThat(notification.getMaxRetries()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Tenant ID Tests")
    class TenantIdTests {

        @ParameterizedTest
        @ValueSource(strings = {"tenant-001", "tenant-002", "default", "prod-tenant"})
        @DisplayName("Should accept various tenant IDs")
        void shouldAcceptVariousTenantIds(String tenantId) {
            Notification notification = Notification.builder()
                    .tenantId(tenantId)
                    .build();

            assertThat(notification.getTenantId()).isEqualTo(tenantId);
        }

        @Test
        @DisplayName("Should set and get tenant ID")
        void shouldSetAndGetTenantId() {
            Notification notification = Notification.builder()
                    .tenantId(TENANT_ID)
                    .build();

            assertThat(notification.getTenantId()).isEqualTo(TENANT_ID);
        }
    }

    @Nested
    @DisplayName("Notification Type Tests")
    class NotificationTypeTests {

        @ParameterizedTest
        @EnumSource(NotificationType.class)
        @DisplayName("Should accept all notification types")
        void shouldAcceptAllNotificationTypes(NotificationType type) {
            Notification notification = Notification.builder()
                    .notificationType(type)
                    .build();

            assertThat(notification.getNotificationType()).isEqualTo(type);
        }

        @Test
        @DisplayName("Should set config created type")
        void shouldSetConfigCreatedType() {
            Notification notification = Notification.builder()
                    .notificationType(NotificationType.CONFIG_CREATED)
                    .build();

            assertThat(notification.getNotificationType()).isEqualTo(NotificationType.CONFIG_CREATED);
        }
    }

    @Nested
    @DisplayName("Channel Tests")
    class ChannelTests {

        @ParameterizedTest
        @EnumSource(NotificationChannel.class)
        @DisplayName("Should accept all channels")
        void shouldAcceptAllChannels(NotificationChannel channel) {
            Notification notification = Notification.builder()
                    .channel(channel)
                    .build();

            assertThat(notification.getChannel()).isEqualTo(channel);
        }

        @Test
        @DisplayName("Should set email channel")
        void shouldSetEmailChannel() {
            Notification notification = Notification.builder()
                    .channel(NotificationChannel.EMAIL)
                    .build();

            assertThat(notification.getChannel()).isEqualTo(NotificationChannel.EMAIL);
        }

        @Test
        @DisplayName("Should set webhook channel")
        void shouldSetWebhookChannel() {
            Notification notification = Notification.builder()
                    .channel(NotificationChannel.WEBHOOK)
                    .build();

            assertThat(notification.getChannel()).isEqualTo(NotificationChannel.WEBHOOK);
        }
    }

    @Nested
    @DisplayName("Recipient Tests")
    class RecipientTests {

        @Test
        @DisplayName("Should set email recipient")
        void shouldSetEmailRecipient() {
            Notification notification = Notification.builder()
                    .recipient("user@example.com")
                    .build();

            assertThat(notification.getRecipient()).isEqualTo("user@example.com");
        }

        @Test
        @DisplayName("Should set webhook URL recipient")
        void shouldSetWebhookUrlRecipient() {
            Notification notification = Notification.builder()
                    .recipient("https://example.com/webhook")
                    .build();

            assertThat(notification.getRecipient()).isEqualTo("https://example.com/webhook");
        }

        @Test
        @DisplayName("Should set Slack channel recipient")
        void shouldSetSlackChannelRecipient() {
            Notification notification = Notification.builder()
                    .recipient("#alerts-channel")
                    .build();

            assertThat(notification.getRecipient()).isEqualTo("#alerts-channel");
        }

        @Test
        @DisplayName("Should set phone number recipient")
        void shouldSetPhoneNumberRecipient() {
            Notification notification = Notification.builder()
                    .recipient("+1234567890")
                    .build();

            assertThat(notification.getRecipient()).isEqualTo("+1234567890");
        }
    }

    @Nested
    @DisplayName("Status Tests")
    class StatusTests {

        @ParameterizedTest
        @EnumSource(NotificationStatus.class)
        @DisplayName("Should accept all statuses")
        void shouldAcceptAllStatuses(NotificationStatus status) {
            Notification notification = Notification.builder()
                    .status(status)
                    .build();

            assertThat(notification.getStatus()).isEqualTo(status);
        }

        @Test
        @DisplayName("Should default to PENDING")
        void shouldDefaultToPENDING() {
            Notification notification = Notification.builder()
                    .build();

            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.PENDING);
        }

        @Test
        @DisplayName("Should set SENT status")
        void shouldSetSENTStatus() {
            Notification notification = Notification.builder()
                    .status(NotificationStatus.SENT)
                    .build();

            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
        }
    }

    @Nested
    @DisplayName("Retry Tests")
    class RetryTests {

        @Test
        @DisplayName("Should set retry count")
        void shouldSetRetryCount() {
            Notification notification = Notification.builder()
                    .retryCount(2)
                    .build();

            assertThat(notification.getRetryCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should default retry count to 0")
        void shouldDefaultRetryCountTo0() {
            Notification notification = Notification.builder()
                    .build();

            assertThat(notification.getRetryCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should set max retries")
        void shouldSetMaxRetries() {
            Notification notification = Notification.builder()
                    .maxRetries(5)
                    .build();

            assertThat(notification.getMaxRetries()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should default max retries to 3")
        void shouldDefaultMaxRetriesTo3() {
            Notification notification = Notification.builder()
                    .build();

            assertThat(notification.getMaxRetries()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should increment retry count")
        void shouldIncrementRetryCount() {
            Notification notification = Notification.builder()
                    .retryCount(1)
                    .build();

            notification.incrementRetry();

            assertThat(notification.getRetryCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should check if can retry when eligible")
        void shouldCheckIfCanRetryWhenEligible() {
            Notification notification = Notification.builder()
                    .status(NotificationStatus.FAILED)
                    .retryCount(1)
                    .maxRetries(3)
                    .build();

            assertThat(notification.canRetry()).isTrue();
        }

        @Test
        @DisplayName("Should not retry when max retries reached")
        void shouldNotRetryWhenMaxRetriesReached() {
            Notification notification = Notification.builder()
                    .status(NotificationStatus.FAILED)
                    .retryCount(3)
                    .maxRetries(3)
                    .build();

            assertThat(notification.canRetry()).isFalse();
        }

        @Test
        @DisplayName("Should not retry when status is not FAILED")
        void shouldNotRetryWhenStatusIsNotFAILED() {
            Notification notification = Notification.builder()
                    .status(NotificationStatus.SENT)
                    .retryCount(0)
                    .maxRetries(3)
                    .build();

            assertThat(notification.canRetry()).isFalse();
        }
    }

    @Nested
    @DisplayName("State Transition Tests")
    class StateTransitionTests {

        @Test
        @DisplayName("Should mark as sent")
        void shouldMarkAsSent() {
            Notification notification = Notification.builder()
                    .status(NotificationStatus.PENDING)
                    .sentAt(null)
                    .build();

            notification.markAsSent();

            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
            assertThat(notification.getSentAt()).isNotNull();
        }

        @Test
        @DisplayName("Should mark as failed with error message")
        void shouldMarkAsFailedWithErrorMessage() {
            Notification notification = Notification.builder()
                    .status(NotificationStatus.PENDING)
                    .errorMessage(null)
                    .build();

            notification.markAsFailed("Connection timeout");

            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.FAILED);
            assertThat(notification.getErrorMessage()).isEqualTo("Connection timeout");
        }

        @Test
        @DisplayName("Should support retry flow")
        void shouldSupportRetryFlow() {
            Notification notification = Notification.builder()
                    .status(NotificationStatus.PENDING)
                    .build();

            // First attempt fails
            notification.markAsFailed("Temporary error");
            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.FAILED);
            assertThat(notification.canRetry()).isTrue();

            // Increment retry
            notification.incrementRetry();
            assertThat(notification.getRetryCount()).isEqualTo(1);

            // Second attempt succeeds
            notification.markAsSent();
            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
            assertThat(notification.canRetry()).isFalse();
        }
    }

    @Nested
    @DisplayName("Subject Tests")
    class SubjectTests {

        @Test
        @DisplayName("Should set subject")
        void shouldSetSubject() {
            Notification notification = Notification.builder()
                    .subject("Important Alert")
                    .build();

            assertThat(notification.getSubject()).isEqualTo("Important Alert");
        }

        @Test
        @DisplayName("Should accept null subject")
        void shouldAcceptNullSubject() {
            Notification notification = Notification.builder()
                    .subject(null)
                    .build();

            assertThat(notification.getSubject()).isNull();
        }

        @Test
        @DisplayName("Should accept empty subject")
        void shouldAcceptEmptySubject() {
            Notification notification = Notification.builder()
                    .subject("")
                    .build();

            assertThat(notification.getSubject()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Message Tests")
    class MessageTests {

        @Test
        @DisplayName("Should set message")
        void shouldSetMessage() {
            Notification notification = Notification.builder()
                    .message("This is a notification message")
                    .build();

            assertThat(notification.getMessage()).isEqualTo("This is a notification message");
        }

        @Test
        @DisplayName("Should accept long message")
        void shouldAcceptLongMessage() {
            String longMessage = "A".repeat(5000);
            Notification notification = Notification.builder()
                    .message(longMessage)
                    .build();

            assertThat(notification.getMessage()).isEqualTo(longMessage);
        }

        @Test
        @DisplayName("Should accept null message")
        void shouldAcceptNullMessage() {
            Notification notification = Notification.builder()
                    .message(null)
                    .build();

            assertThat(notification.getMessage()).isNull();
        }
    }

    @Nested
    @DisplayName("Metadata Tests")
    class MetadataTests {

        @Test
        @DisplayName("Should set metadata")
        void shouldSetMetadata() {
            Notification notification = Notification.builder()
                    .metadata("{\"source\":\"system\",\"priority\":\"high\"}")
                    .build();

            assertThat(notification.getMetadata()).isEqualTo("{\"source\":\"system\",\"priority\":\"high\"}");
        }

        @Test
        @DisplayName("Should accept null metadata")
        void shouldAcceptNullMetadata() {
            Notification notification = Notification.builder()
                    .metadata(null)
                    .build();

            assertThat(notification.getMetadata()).isNull();
        }

        @Test
        @DisplayName("Should accept empty metadata")
        void shouldAcceptEmptyMetadata() {
            Notification notification = Notification.builder()
                    .metadata("")
                    .build();

            assertThat(notification.getMetadata()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Error Message Tests")
    class ErrorMessageTests {

        @Test
        @DisplayName("Should set error message")
        void shouldSetErrorMessage() {
            Notification notification = Notification.builder()
                    .errorMessage("SMTP connection failed")
                    .build();

            assertThat(notification.getErrorMessage()).isEqualTo("SMTP connection failed");
        }

        @Test
        @DisplayName("Should accept null error message")
        void shouldAcceptNullErrorMessage() {
            Notification notification = Notification.builder()
                    .errorMessage(null)
                    .build();

            assertThat(notification.getErrorMessage()).isNull();
        }
    }

    @Nested
    @DisplayName("Sent At Tests")
    class SentAtTests {

        @Test
        @DisplayName("Should set sent at timestamp")
        void shouldSetSentAtTimestamp() {
            LocalDateTime now = LocalDateTime.now();
            Notification notification = Notification.builder()
                    .sentAt(now)
                    .build();

            assertThat(notification.getSentAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should accept null sent at")
        void shouldAcceptNullSentAt() {
            Notification notification = Notification.builder()
                    .sentAt(null)
                    .build();

            assertThat(notification.getSentAt()).isNull();
        }
    }

    @Nested
    @DisplayName("Timestamp Tests")
    class TimestampTests {

        @Test
        @DisplayName("Should set created at")
        void shouldSetCreatedAt() {
            LocalDateTime now = LocalDateTime.now();
            Notification notification = Notification.builder()
                    .createdAt(now)
                    .build();

            assertThat(notification.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should set updated at")
        void shouldSetUpdatedAt() {
            LocalDateTime now = LocalDateTime.now();
            Notification notification = Notification.builder()
                    .updatedAt(now)
                    .build();

            assertThat(notification.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should accept null created at")
        void shouldAcceptNullCreatedAt() {
            Notification notification = Notification.builder()
                    .createdAt(null)
                    .build();

            assertThat(notification.getCreatedAt()).isNull();
        }

        @Test
        @DisplayName("Should accept null updated at")
        void shouldAcceptNullUpdatedAt() {
            Notification notification = Notification.builder()
                    .updatedAt(null)
                    .build();

            assertThat(notification.getUpdatedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("Lombok Data Tests")
    class LombokDataTests {

        @Test
        @DisplayName("Should generate equals")
        void shouldGenerateEquals() {
            Notification notification1 = Notification.builder()
                    .tenantId(TENANT_ID)
                    .notificationType(NotificationType.SYSTEM_ALERT)
                    .channel(NotificationChannel.EMAIL)
                    .recipient(RECIPIENT)
                    .build();

            Notification notification2 = Notification.builder()
                    .tenantId(TENANT_ID)
                    .notificationType(NotificationType.SYSTEM_ALERT)
                    .channel(NotificationChannel.EMAIL)
                    .recipient(RECIPIENT)
                    .build();

            assertThat(notification1).isEqualTo(notification2);
        }

        @Test
        @DisplayName("Should generate hashCode")
        void shouldGenerateHashCode() {
            Notification notification1 = Notification.builder()
                    .recipient(RECIPIENT)
                    .subject(SUBJECT)
                    .build();

            Notification notification2 = Notification.builder()
                    .recipient(RECIPIENT)
                    .subject(SUBJECT)
                    .build();

            assertThat(notification1.hashCode()).isEqualTo(notification2.hashCode());
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            Notification notification = Notification.builder()
                    .recipient(RECIPIENT)
                    .subject(SUBJECT)
                    .build();

            String toString = notification.toString();

            assertThat(toString).contains(RECIPIENT);
        }

        @Test
        @DisplayName("Should generate setters")
        void shouldGenerateSetters() {
            Notification notification = Notification.builder()
                    .subject("Old Subject")
                    .status(NotificationStatus.PENDING)
                    .build();

            notification.setSubject("New Subject");
            notification.setStatus(NotificationStatus.SENT);

            assertThat(notification.getSubject()).isEqualTo("New Subject");
            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("Should represent email notification")
        void shouldRepresentEmailNotification() {
            Notification notification = Notification.builder()
                    .notificationType(NotificationType.CONFIG_CREATED)
                    .channel(NotificationChannel.EMAIL)
                    .recipient("admin@example.com")
                    .subject("Configuration Created")
                    .message("A new configuration has been created")
                    .build();

            assertThat(notification.getChannel()).isEqualTo(NotificationChannel.EMAIL);
            assertThat(notification.getRecipient()).contains("@");
        }

        @Test
        @DisplayName("Should represent webhook notification")
        void shouldRepresentWebhookNotification() {
            Notification notification = Notification.builder()
                    .notificationType(NotificationType.SECURITY_ALERT)
                    .channel(NotificationChannel.WEBHOOK)
                    .recipient("https://api.example.com/webhooks")
                    .subject("Security Alert")
                    .build();

            assertThat(notification.getChannel()).isEqualTo(NotificationChannel.WEBHOOK);
            assertThat(notification.getRecipient()).startsWith("https://");
        }

        @Test
        @DisplayName("Should represent Slack notification")
        void shouldRepresentSlackNotification() {
            Notification notification = Notification.builder()
                    .notificationType(NotificationType.FEATURE_FLAG_TOGGLED)
                    .channel(NotificationChannel.SLACK)
                    .recipient("#devops-alerts")
                    .message("Feature flag 'new-ui' has been enabled")
                    .build();

            assertThat(notification.getChannel()).isEqualTo(NotificationChannel.SLACK);
            assertThat(notification.getRecipient()).startsWith("#");
        }

        @Test
        @DisplayName("Should represent SMS notification")
        void shouldRepresentSmsNotification() {
            Notification notification = Notification.builder()
                    .notificationType(NotificationType.SYSTEM_ALERT)
                    .channel(NotificationChannel.SMS)
                    .recipient("+1234567890")
                    .message("System alert: High CPU usage detected")
                    .build();

            assertThat(notification.getChannel()).isEqualTo(NotificationChannel.SMS);
            assertThat(notification.getRecipient()).startsWith("+");
        }

        @Test
        @DisplayName("Should represent retry scenario")
        void shouldRepresentRetryScenario() {
            Notification notification = Notification.builder()
                    .notificationType(NotificationType.CONFIG_UPDATED)
                    .channel(NotificationChannel.WEBHOOK)
                    .recipient("https://example.com/hook")
                    .status(NotificationStatus.FAILED)
                    .retryCount(2)
                    .maxRetries(3)
                    .errorMessage("Connection timeout")
                    .build();

            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.FAILED);
            assertThat(notification.canRetry()).isTrue();
            assertThat(notification.getErrorMessage()).isEqualTo("Connection timeout");
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should get id")
        void shouldGetId() {
            Notification notification = Notification.builder()
                    .id(ID)
                    .build();

            assertThat(notification.getId()).isEqualTo(ID);
        }

        @Test
        @DisplayName("Should get tenant ID")
        void shouldGetTenantId() {
            Notification notification = Notification.builder()
                    .tenantId(TENANT_ID)
                    .build();

            assertThat(notification.getTenantId()).isEqualTo(TENANT_ID);
        }

        @Test
        @DisplayName("Should get notification type")
        void shouldGetNotificationType() {
            Notification notification = Notification.builder()
                    .notificationType(NotificationType.CONFIG_CREATED)
                    .build();

            assertThat(notification.getNotificationType()).isEqualTo(NotificationType.CONFIG_CREATED);
        }

        @Test
        @DisplayName("Should get channel")
        void shouldGetChannel() {
            Notification notification = Notification.builder()
                    .channel(NotificationChannel.EMAIL)
                    .build();

            assertThat(notification.getChannel()).isEqualTo(NotificationChannel.EMAIL);
        }

        @Test
        @DisplayName("Should get recipient")
        void shouldGetRecipient() {
            Notification notification = Notification.builder()
                    .recipient(RECIPIENT)
                    .build();

            assertThat(notification.getRecipient()).isEqualTo(RECIPIENT);
        }

        @Test
        @DisplayName("Should get subject")
        void shouldGetSubject() {
            Notification notification = Notification.builder()
                    .subject(SUBJECT)
                    .build();

            assertThat(notification.getSubject()).isEqualTo(SUBJECT);
        }

        @Test
        @DisplayName("Should get message")
        void shouldGetMessage() {
            Notification notification = Notification.builder()
                    .message(MESSAGE)
                    .build();

            assertThat(notification.getMessage()).isEqualTo(MESSAGE);
        }

        @Test
        @DisplayName("Should get metadata")
        void shouldGetMetadata() {
            Notification notification = Notification.builder()
                    .metadata(METADATA)
                    .build();

            assertThat(notification.getMetadata()).isEqualTo(METADATA);
        }

        @Test
        @DisplayName("Should get status")
        void shouldGetStatus() {
            Notification notification = Notification.builder()
                    .status(NotificationStatus.SENT)
                    .build();

            assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
        }

        @Test
        @DisplayName("Should get retry count")
        void shouldGetRetryCount() {
            Notification notification = Notification.builder()
                    .retryCount(2)
                    .build();

            assertThat(notification.getRetryCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should get max retries")
        void shouldGetMaxRetries() {
            Notification notification = Notification.builder()
                    .maxRetries(5)
                    .build();

            assertThat(notification.getMaxRetries()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should get sent at")
        void shouldGetSentAt() {
            LocalDateTime now = LocalDateTime.now();
            Notification notification = Notification.builder()
                    .sentAt(now)
                    .build();

            assertThat(notification.getSentAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should get error message")
        void shouldGetErrorMessage() {
            Notification notification = Notification.builder()
                    .errorMessage("Error occurred")
                    .build();

            assertThat(notification.getErrorMessage()).isEqualTo("Error occurred");
        }

        @Test
        @DisplayName("Should get created at")
        void shouldGetCreatedAt() {
            LocalDateTime now = LocalDateTime.now();
            Notification notification = Notification.builder()
                    .createdAt(now)
                    .build();

            assertThat(notification.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should get updated at")
        void shouldGetUpdatedAt() {
            LocalDateTime now = LocalDateTime.now();
            Notification notification = Notification.builder()
                    .updatedAt(now)
                    .build();

            assertThat(notification.getUpdatedAt()).isEqualTo(now);
        }
    }
}
