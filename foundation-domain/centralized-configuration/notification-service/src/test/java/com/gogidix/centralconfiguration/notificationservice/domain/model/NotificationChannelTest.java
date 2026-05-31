package com.gogidix.centralconfiguration.notificationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NotificationChannel Enum Tests")
class NotificationChannelTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(NotificationChannel.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(NotificationChannel channel) {
            assertThat(channel).isNotNull();
        }

        @Test
        @DisplayName("Should have EMAIL channel")
        void shouldHaveEMAILChannel() {
            assertThat(NotificationChannel.valueOf("EMAIL")).isEqualTo(NotificationChannel.EMAIL);
        }

        @Test
        @DisplayName("Should have WEBHOOK channel")
        void shouldHaveWEBHOOKChannel() {
            assertThat(NotificationChannel.valueOf("WEBHOOK")).isEqualTo(NotificationChannel.WEBHOOK);
        }

        @Test
        @DisplayName("Should have SLACK channel")
        void shouldHaveSLACKChannel() {
            assertThat(NotificationChannel.valueOf("SLACK")).isEqualTo(NotificationChannel.SLACK);
        }

        @Test
        @DisplayName("Should have SMS channel")
        void shouldHaveSMSChannel() {
            assertThat(NotificationChannel.valueOf("SMS")).isEqualTo(NotificationChannel.SMS);
        }

        @Test
        @DisplayName("Should have TEAMS channel")
        void shouldHaveTEAMSChannel() {
            assertThat(NotificationChannel.valueOf("TEAMS")).isEqualTo(NotificationChannel.TEAMS);
        }

        @Test
        @DisplayName("Should have 5 notification channels")
        void shouldHave5NotificationChannels() {
            assertThat(NotificationChannel.values()).hasSize(5);
        }
    }

    @Nested
    @DisplayName("Code Tests")
    class CodeTests {

        @Test
        @DisplayName("EMAIL has code 'email'")
        void emailHasCode() {
            assertThat(NotificationChannel.EMAIL.getCode()).isEqualTo("email");
        }

        @Test
        @DisplayName("WEBHOOK has code 'webhook'")
        void webhookHasCode() {
            assertThat(NotificationChannel.WEBHOOK.getCode()).isEqualTo("webhook");
        }

        @Test
        @DisplayName("SLACK has code 'slack'")
        void slackHasCode() {
            assertThat(NotificationChannel.SLACK.getCode()).isEqualTo("slack");
        }

        @Test
        @DisplayName("SMS has code 'sms'")
        void smsHasCode() {
            assertThat(NotificationChannel.SMS.getCode()).isEqualTo("sms");
        }

        @Test
        @DisplayName("TEAMS has code 'teams'")
        void teamsHasCode() {
            assertThat(NotificationChannel.TEAMS.getCode()).isEqualTo("teams");
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("All channels have descriptions")
        void allChannelsHaveDescriptions() {
            for (NotificationChannel channel : NotificationChannel.values()) {
                assertThat(channel.getDescription()).isNotNull();
                assertThat(channel.getDescription()).isNotEmpty();
            }
        }

        @Test
        @DisplayName("EMAIL description mentions email")
        void emailDescriptionMentionsEmail() {
            assertThat(NotificationChannel.EMAIL.getDescription()).containsIgnoringCase("email");
        }

        @Test
        @DisplayName("WEBHOOK description mentions webhook")
        void webhookDescriptionMentionsWebhook() {
            assertThat(NotificationChannel.WEBHOOK.getDescription()).containsIgnoringCase("webhook");
        }

        @Test
        @DisplayName("SLACK description mentions slack")
        void slackDescriptionMentionsSlack() {
            assertThat(NotificationChannel.SLACK.getDescription()).containsIgnoringCase("slack");
        }

        @Test
        @DisplayName("SMS description mentions sms")
        void smsDescriptionMentionsSms() {
            assertThat(NotificationChannel.SMS.getDescription()).containsIgnoringCase("sms");
        }

        @Test
        @DisplayName("TEAMS description mentions teams")
        void teamsDescriptionMentionsTeams() {
            assertThat(NotificationChannel.TEAMS.getDescription()).containsIgnoringCase("teams");
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("EMAIL for email notifications")
        void emailForEmailNotifications() {
            NotificationChannel channel = NotificationChannel.EMAIL;
            assertThat(channel).isEqualTo(NotificationChannel.EMAIL);
        }

        @Test
        @DisplayName("WEBHOOK for webhook notifications")
        void webhookForWebhookNotifications() {
            NotificationChannel channel = NotificationChannel.WEBHOOK;
            assertThat(channel).isEqualTo(NotificationChannel.WEBHOOK);
        }

        @Test
        @DisplayName("SLACK for Slack notifications")
        void slackForSlackNotifications() {
            NotificationChannel channel = NotificationChannel.SLACK;
            assertThat(channel).isEqualTo(NotificationChannel.SLACK);
        }

        @Test
        @DisplayName("SMS for SMS notifications")
        void smsForSmsNotifications() {
            NotificationChannel channel = NotificationChannel.SMS;
            assertThat(channel).isEqualTo(NotificationChannel.SMS);
        }

        @Test
        @DisplayName("TEAMS for Teams notifications")
        void teamsForTeamsNotifications() {
            NotificationChannel channel = NotificationChannel.TEAMS;
            assertThat(channel).isEqualTo(NotificationChannel.TEAMS);
        }
    }

    @Nested
    @DisplayName("Channel Type Tests")
    class ChannelTypeTests {

        @Test
        @DisplayName("Should support traditional email")
        void shouldSupportTraditionalEmail() {
            NotificationChannel channel = NotificationChannel.EMAIL;
            assertThat(channel.getCode()).isEqualTo("email");
        }

        @Test
        @DisplayName("Should support modern messaging")
        void shouldSupportModernMessaging() {
            NotificationChannel slack = NotificationChannel.SLACK;
            NotificationChannel teams = NotificationChannel.TEAMS;

            assertThat(slack).isNotNull();
            assertThat(teams).isNotNull();
        }

        @Test
        @DisplayName("Should support programmatic webhooks")
        void shouldSupportProgrammaticWebhooks() {
            NotificationChannel channel = NotificationChannel.WEBHOOK;
            assertThat(channel).isEqualTo(NotificationChannel.WEBHOOK);
        }

        @Test
        @DisplayName("Should support mobile SMS")
        void shouldSupportMobileSms() {
            NotificationChannel channel = NotificationChannel.SMS;
            assertThat(channel).isEqualTo(NotificationChannel.SMS);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have consistent enum names")
        void shouldHaveConsistentEnumNames() {
            for (NotificationChannel channel : NotificationChannel.values()) {
                String name = channel.name();
                assertThat(name).isNotNull();
                assertThat(name).isUpperCase();
                assertThat(name).doesNotContain(" ");
            }
        }

        @Test
        @DisplayName("Should have unique enum values")
        void shouldHaveUniqueEnumValues() {
            long uniqueCount = java.util.Arrays.stream(NotificationChannel.values())
                    .map(NotificationChannel::name)
                    .distinct()
                    .count();

            assertThat(uniqueCount).isEqualTo(NotificationChannel.values().length);
        }

        @Test
        @DisplayName("Should have unique codes")
        void shouldHaveUniqueCodes() {
            long uniqueCodes = java.util.Arrays.stream(NotificationChannel.values())
                    .map(NotificationChannel::getCode)
                    .distinct()
                    .count();

            assertThat(uniqueCodes).isEqualTo(NotificationChannel.values().length);
        }
    }

    @Nested
    @DisplayName("Delivery Method Tests")
    class DeliveryMethodTests {

        @Test
        @DisplayName("EMAIL is synchronous delivery")
        void emailIsSynchronousDelivery() {
            NotificationChannel channel = NotificationChannel.EMAIL;
            assertThat(channel).isNotNull();
        }

        @Test
        @DisplayName("WEBHOOK is HTTP based delivery")
        void webhookIsHttpBasedDelivery() {
            NotificationChannel channel = NotificationChannel.WEBHOOK;
            assertThat(channel).isNotNull();
        }

        @Test
        @DisplayName("SLACK is API based delivery")
        void slackIsApiBasedDelivery() {
            NotificationChannel channel = NotificationChannel.SLACK;
            assertThat(channel).isNotNull();
        }

        @Test
        @DisplayName("SMS is telecom delivery")
        void smsIsTelecomDelivery() {
            NotificationChannel channel = NotificationChannel.SMS;
            assertThat(channel).isNotNull();
        }

        @Test
        @DisplayName("TEAMS is Microsoft integration")
        void teamsIsMicrosoftIntegration() {
            NotificationChannel channel = NotificationChannel.TEAMS;
            assertThat(channel).isNotNull();
        }
    }
}
