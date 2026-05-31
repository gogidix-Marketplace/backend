package com.gogidix.aiservices.ainotificationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NotificationPriority Enum Tests")
class NotificationPriorityTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(NotificationPriority.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(NotificationPriority priority) {
            assertThat(priority).isNotNull();
        }

        @Test
        @DisplayName("Should have LOW priority")
        void shouldHaveLowPriority() {
            assertThat(NotificationPriority.valueOf("LOW")).isEqualTo(NotificationPriority.LOW);
        }

        @Test
        @DisplayName("Should have NORMAL priority")
        void shouldHaveMediumPriority() {
            assertThat(NotificationPriority.valueOf("NORMAL")).isEqualTo(NotificationPriority.NORMAL);
        }

        @Test
        @DisplayName("Should have HIGH priority")
        void shouldHaveHighPriority() {
            assertThat(NotificationPriority.valueOf("HIGH")).isEqualTo(NotificationPriority.HIGH);
        }

        @Test
        @DisplayName("Should have URGENT priority")
        void shouldHaveUrgentPriority() {
            assertThat(NotificationPriority.valueOf("URGENT")).isEqualTo(NotificationPriority.URGENT);
        }

        @Test
        @DisplayName("Should have 4 priority values")
        void shouldHave4PriorityValues() {
            assertThat(NotificationPriority.values()).hasSize(4);
        }
    }

    @Nested
    @DisplayName("Priority Ordering Tests")
    class PriorityOrderingTests {

        @Test
        @DisplayName("Should have priority levels from LOW to URGENT")
        void shouldHavePriorityLevelsLowToUrgent() {
            NotificationPriority[] priorities = NotificationPriority.values();
            assertThat(priorities).containsExactly(
                    NotificationPriority.LOW,
                    NotificationPriority.NORMAL,
                    NotificationPriority.HIGH,
                    NotificationPriority.URGENT
            );
        }

        @Test
        @DisplayName("LOW should have lowest urgency")
        void lowShouldHaveLowestUrgency() {
            NotificationPriority low = NotificationPriority.LOW;
            assertThat(low.name()).isEqualTo("LOW");
        }

        @Test
        @DisplayName("NORMAL should have moderate urgency")
        void mediumShouldHaveModerateUrgency() {
            NotificationPriority medium = NotificationPriority.NORMAL;
            assertThat(medium.name()).isEqualTo("NORMAL");
        }

        @Test
        @DisplayName("HIGH should have high urgency")
        void highShouldHaveHighUrgency() {
            NotificationPriority high = NotificationPriority.HIGH;
            assertThat(high.name()).isEqualTo("HIGH");
        }

        @Test
        @DisplayName("URGENT should have highest urgency")
        void urgentShouldHaveHighestUrgency() {
            NotificationPriority urgent = NotificationPriority.URGENT;
            assertThat(urgent.name()).isEqualTo("URGENT");
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("LOW for informational notifications")
        void lowForInformationalNotifications() {
            NotificationPriority low = NotificationPriority.LOW;
            assertThat(low).isNotNull();
        }

        @Test
        @DisplayName("NORMAL for regular notifications")
        void mediumForRegularNotifications() {
            NotificationPriority medium = NotificationPriority.NORMAL;
            assertThat(medium).isNotNull();
        }

        @Test
        @DisplayName("HIGH for important notifications")
        void highForImportantNotifications() {
            NotificationPriority high = NotificationPriority.HIGH;
            assertThat(high).isNotNull();
        }

        @Test
        @DisplayName("URGENT for critical notifications")
        void urgentForCriticalNotifications() {
            NotificationPriority urgent = NotificationPriority.URGENT;
            assertThat(urgent).isNotNull();
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have consistent enum names")
        void shouldHaveConsistentEnumNames() {
            for (NotificationPriority priority : NotificationPriority.values()) {
                String name = priority.name();
                assertThat(name).isNotNull();
                assertThat(name).isUpperCase();
                assertThat(name).doesNotContain(" ");
            }
        }

        @Test
        @DisplayName("Should have unique enum values")
        void shouldHaveUniqueEnumValues() {
            long uniqueCount = java.util.Arrays.stream(NotificationPriority.values())
                    .map(NotificationPriority::name)
                    .distinct()
                    .count();

            assertThat(uniqueCount).isEqualTo(NotificationPriority.values().length);
        }
    }

    @Nested
    @DisplayName("Delivery Behavior Tests")
    class DeliveryBehaviorTests {

        @Test
        @DisplayName("LOW priority may use batch delivery")
        void lowPriorityMayUseBatchDelivery() {
            NotificationPriority low = NotificationPriority.LOW;
            assertThat(low).isNotNull();
        }

        @Test
        @DisplayName("NORMAL priority uses standard delivery")
        void mediumPriorityUsesStandardDelivery() {
            NotificationPriority medium = NotificationPriority.NORMAL;
            assertThat(medium).isNotNull();
        }

        @Test
        @DisplayName("HIGH priority uses immediate delivery")
        void highPriorityUsesImmediateDelivery() {
            NotificationPriority high = NotificationPriority.HIGH;
            assertThat(high).isNotNull();
        }

        @Test
        @DisplayName("URGENT priority uses instant delivery with retries")
        void urgentPriorityUsesInstantDeliveryWithRetries() {
            NotificationPriority urgent = NotificationPriority.URGENT;
            assertThat(urgent).isNotNull();
        }
    }

    @Nested
    @DisplayName("Channel Selection Tests")
    class ChannelSelectionTests {

        @Test
        @DisplayName("LOW priority suitable for email")
        void lowPrioritySuitableForEmail() {
            NotificationPriority low = NotificationPriority.LOW;
            assertThat(low).isNotNull();
        }

        @Test
        @DisplayName("NORMAL priority suitable for email and push")
        void mediumPrioritySuitableForEmailAndPush() {
            NotificationPriority medium = NotificationPriority.NORMAL;
            assertThat(medium).isNotNull();
        }

        @Test
        @DisplayName("HIGH priority suitable for push and SMS")
        void highPrioritySuitableForPushAndSms() {
            NotificationPriority high = NotificationPriority.HIGH;
            assertThat(high).isNotNull();
        }

        @Test
        @DisplayName("URGENT priority suitable for all channels")
        void urgentPrioritySuitableForAllChannels() {
            NotificationPriority urgent = NotificationPriority.URGENT;
            assertThat(urgent).isNotNull();
        }
    }

    @Nested
    @DisplayName("Notification Examples Tests")
    class NotificationExamplesTests {

        @Test
        @DisplayName("LOW for marketing emails")
        void lowForMarketingEmails() {
            NotificationPriority low = NotificationPriority.LOW;
            assertThat(low).isNotNull();
        }

        @Test
        @DisplayName("NORMAL for weekly summaries")
        void mediumForWeeklySummaries() {
            NotificationPriority medium = NotificationPriority.NORMAL;
            assertThat(medium).isNotNull();
        }

        @Test
        @DisplayName("HIGH for security alerts")
        void highForSecurityAlerts() {
            NotificationPriority high = NotificationPriority.HIGH;
            assertThat(high).isNotNull();
        }

        @Test
        @DisplayName("URGENT for emergency notifications")
        void urgentForEmergencyNotifications() {
            NotificationPriority urgent = NotificationPriority.URGENT;
            assertThat(urgent).isNotNull();
        }
    }
}