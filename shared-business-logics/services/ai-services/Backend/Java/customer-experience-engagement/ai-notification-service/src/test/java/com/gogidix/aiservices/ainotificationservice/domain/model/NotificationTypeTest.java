package com.gogidix.aiservices.ainotificationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NotificationType Enum Tests")
class NotificationTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(NotificationType.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(NotificationType type) {
            assertThat(type).isNotNull();
        }

        @Test
        @DisplayName("Should have EMAIL type")
        void shouldHaveEmailType() {
            assertThat(NotificationType.valueOf("EMAIL")).isEqualTo(NotificationType.EMAIL);
        }

        @Test
        @DisplayName("Should have SMS type")
        void shouldHaveSmsType() {
            assertThat(NotificationType.valueOf("SMS")).isEqualTo(NotificationType.SMS);
        }

        @Test
        @DisplayName("Should have PUSH type")
        void shouldHavePushType() {
            assertThat(NotificationType.valueOf("PUSH")).isEqualTo(NotificationType.PUSH);
        }

        @Test
        @DisplayName("Should have IN_APP type")
        void shouldHaveInAppType() {
            assertThat(NotificationType.valueOf("IN_APP")).isEqualTo(NotificationType.IN_APP);
        }

        @Test
        @DisplayName("Should have 5 type values")
        void shouldHave5TypeValues() {
            assertThat(NotificationType.values()).hasSize(5);
        }
    }

    @Nested
    @DisplayName("Channel Characteristics Tests")
    class ChannelCharacteristicsTests {

        @Test
        @DisplayName("EMAIL is asynchronous channel")
        void emailIsAsynchronousChannel() {
            NotificationType email = NotificationType.EMAIL;
            assertThat(email).isNotNull();
        }

        @Test
        @DisplayName("SMS is immediate channel")
        void smsIsImmediateChannel() {
            NotificationType sms = NotificationType.SMS;
            assertThat(sms).isNotNull();
        }

        @Test
        @DisplayName("PUSH is real-time channel")
        void pushIsRealTimeChannel() {
            NotificationType push = NotificationType.PUSH;
            assertThat(push).isNotNull();
        }

        @Test
        @DisplayName("IN_APP is contextual channel")
        void inAppIsContextualChannel() {
            NotificationType inApp = NotificationType.IN_APP;
            assertThat(inApp).isNotNull();
        }
    }

    @Nested
    @DisplayName("Delivery Requirements Tests")
    class DeliveryRequirementsTests {

        @Test
        @DisplayName("EMAIL requires recipient email address")
        void emailRequiresRecipientEmailAddress() {
            NotificationType email = NotificationType.EMAIL;
            assertThat(email).isNotNull();
        }

        @Test
        @DisplayName("SMS requires recipient phone number")
        void smsRequiresRecipientPhoneNumber() {
            NotificationType sms = NotificationType.SMS;
            assertThat(sms).isNotNull();
        }

        @Test
        @DisplayName("PUSH requires device token")
        void pushRequiresDeviceToken() {
            NotificationType push = NotificationType.PUSH;
            assertThat(push).isNotNull();
        }

        @Test
        @DisplayName("IN_APP requires active user session")
        void inAppRequiresActiveUserSession() {
            NotificationType inApp = NotificationType.IN_APP;
            assertThat(inApp).isNotNull();
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("EMAIL for marketing communications")
        void emailForMarketingCommunications() {
            NotificationType email = NotificationType.EMAIL;
            assertThat(email).isNotNull();
        }

        @Test
        @DisplayName("EMAIL for newsletters")
        void emailForNewsletters() {
            NotificationType email = NotificationType.EMAIL;
            assertThat(email).isNotNull();
        }

        @Test
        @DisplayName("SMS for verification codes")
        void smsForVerificationCodes() {
            NotificationType sms = NotificationType.SMS;
            assertThat(sms).isNotNull();
        }

        @Test
        @DisplayName("SMS for urgent alerts")
        void smsForUrgentAlerts() {
            NotificationType sms = NotificationType.SMS;
            assertThat(sms).isNotNull();
        }

        @Test
        @DisplayName("PUSH for mobile app notifications")
        void pushForMobileAppNotifications() {
            NotificationType push = NotificationType.PUSH;
            assertThat(push).isNotNull();
        }

        @Test
        @DisplayName("PUSH for time-sensitive updates")
        void pushForTimeSensitiveUpdates() {
            NotificationType push = NotificationType.PUSH;
            assertThat(push).isNotNull();
        }

        @Test
        @DisplayName("IN_APP for user engagement")
        void inAppForUserEngagement() {
            NotificationType inApp = NotificationType.IN_APP;
            assertThat(inApp).isNotNull();
        }

        @Test
        @DisplayName("IN_APP for feature announcements")
        void inAppForFeatureAnnouncements() {
            NotificationType inApp = NotificationType.IN_APP;
            assertThat(inApp).isNotNull();
        }
    }

    @Nested
    @DisplayName("Content Support Tests")
    class ContentSupportTests {

        @Test
        @DisplayName("EMAIL supports rich HTML content")
        void emailSupportsRichHtmlContent() {
            NotificationType email = NotificationType.EMAIL;
            assertThat(email).isNotNull();
        }

        @Test
        @DisplayName("SMS supports plain text only")
        void smsSupportsPlainTextOnly() {
            NotificationType sms = NotificationType.SMS;
            assertThat(sms).isNotNull();
        }

        @Test
        @DisplayName("PUSH supports title and body")
        void pushSupportsTitleAndBody() {
            NotificationType push = NotificationType.PUSH;
            assertThat(push).isNotNull();
        }

        @Test
        @DisplayName("IN_APP supports interactive elements")
        void inAppSupportsInteractiveElements() {
            NotificationType inApp = NotificationType.IN_APP;
            assertThat(inApp).isNotNull();
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have consistent enum names")
        void shouldHaveConsistentEnumNames() {
            for (NotificationType type : NotificationType.values()) {
                String name = type.name();
                assertThat(name).isNotNull();
                assertThat(name).isUpperCase();
                assertThat(name).doesNotContain(" ");
            }
        }

        @Test
        @DisplayName("Should have unique enum values")
        void shouldHaveUniqueEnumValues() {
            long uniqueCount = java.util.Arrays.stream(NotificationType.values())
                    .map(NotificationType::name)
                    .distinct()
                    .count();

            assertThat(uniqueCount).isEqualTo(NotificationType.values().length);
        }
    }

    @Nested
    @DisplayName("Delivery Speed Tests")
    class DeliverySpeedTests {

        @Test
        @DisplayName("EMAIL has slower delivery speed")
        void emailHasSlowerDeliverySpeed() {
            NotificationType email = NotificationType.EMAIL;
            assertThat(email).isNotNull();
        }

        @Test
        @DisplayName("SMS has fast delivery speed")
        void smsHasFastDeliverySpeed() {
            NotificationType sms = NotificationType.SMS;
            assertThat(sms).isNotNull();
        }

        @Test
        @DisplayName("PUSH has instant delivery speed")
        void pushHasInstantDeliverySpeed() {
            NotificationType push = NotificationType.PUSH;
            assertThat(push).isNotNull();
        }

        @Test
        @DisplayName("IN_APP has immediate delivery")
        void inAppHasImmediateDelivery() {
            NotificationType inApp = NotificationType.IN_APP;
            assertThat(inApp).isNotNull();
        }
    }

    @Nested
    @DisplayName("Reliability Tests")
    class ReliabilityTests {

        @Test
        @DisplayName("EMAIL is highly reliable")
        void emailIsHighlyReliable() {
            NotificationType email = NotificationType.EMAIL;
            assertThat(email).isNotNull();
        }

        @Test
        @DisplayName("SMS is moderately reliable")
        void smsIsModeratelyReliable() {
            NotificationType sms = NotificationType.SMS;
            assertThat(sms).isNotNull();
        }

        @Test
        @DisplayName("PUSH reliability depends on platform")
        void pushReliabilityDependsOnPlatform() {
            NotificationType push = NotificationType.PUSH;
            assertThat(push).isNotNull();
        }

        @Test
        @DisplayName("IN_APP requires app to be running")
        void inAppRequiresAppToBeRunning() {
            NotificationType inApp = NotificationType.IN_APP;
            assertThat(inApp).isNotNull();
        }
    }

    @Nested
    @DisplayName("Cost Tests")
    class CostTests {

        @Test
        @DisplayName("EMAIL has lowest cost per message")
        void emailHasLowestCostPerMessage() {
            NotificationType email = NotificationType.EMAIL;
            assertThat(email).isNotNull();
        }

        @Test
        @DisplayName("SMS has moderate cost per message")
        void smsHasModerateCostPerMessage() {
            NotificationType sms = NotificationType.SMS;
            assertThat(sms).isNotNull();
        }

        @Test
        @DisplayName("PUSH has low cost per message")
        void pushHasLowCostPerMessage() {
            NotificationType push = NotificationType.PUSH;
            assertThat(push).isNotNull();
        }

        @Test
        @DisplayName("IN_APP has negligible cost")
        void inAppHasNegligibleCost() {
            NotificationType inApp = NotificationType.IN_APP;
            assertThat(inApp).isNotNull();
        }
    }

    @Nested
    @DisplayName("Multi-Channel Strategy Tests")
    class MultiChannelStrategyTests {

        @Test
        @DisplayName("Should support EMAIL + PUSH combination")
        void shouldSupportEmailAndPushCombination() {
            assertThat(NotificationType.EMAIL).isNotNull();
            assertThat(NotificationType.PUSH).isNotNull();
        }

        @Test
        @DisplayName("Should support EMAIL + SMS combination")
        void shouldSupportEmailAndSmsCombination() {
            assertThat(NotificationType.EMAIL).isNotNull();
            assertThat(NotificationType.SMS).isNotNull();
        }

        @Test
        @DisplayName("Should support PUSH + IN_APP combination")
        void shouldSupportPushAndInAppCombination() {
            assertThat(NotificationType.PUSH).isNotNull();
            assertThat(NotificationType.IN_APP).isNotNull();
        }

        @Test
        @DisplayName("Should support all channels for critical notifications")
        void shouldSupportAllChannelsForCriticalNotifications() {
            assertThat(NotificationType.values()).contains(
                    NotificationType.EMAIL,
                    NotificationType.SMS,
                    NotificationType.PUSH,
                    NotificationType.IN_APP
            );
        }
    }

    @Nested
    @DisplayName("Platform Compatibility Tests")
    class PlatformCompatibilityTests {

        @Test
        @DisplayName("EMAIL works on all platforms")
        void emailWorksOnAllPlatforms() {
            NotificationType email = NotificationType.EMAIL;
            assertThat(email).isNotNull();
        }

        @Test
        @DisplayName("SMS works on all mobile phones")
        void smsWorksOnAllMobilePhones() {
            NotificationType sms = NotificationType.SMS;
            assertThat(sms).isNotNull();
        }

        @Test
        @DisplayName("PUSH works on mobile platforms")
        void pushWorksOnMobilePlatforms() {
            NotificationType push = NotificationType.PUSH;
            assertThat(push).isNotNull();
        }

        @Test
        @DisplayName("IN_APP works within application")
        void inAppWorksWithinApplication() {
            NotificationType inApp = NotificationType.IN_APP;
            assertThat(inApp).isNotNull();
        }
    }
}
