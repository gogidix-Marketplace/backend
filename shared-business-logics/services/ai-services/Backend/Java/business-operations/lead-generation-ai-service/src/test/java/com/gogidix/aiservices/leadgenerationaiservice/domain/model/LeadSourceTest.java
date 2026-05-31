package com.gogidix.aiservices.leadgenerationaiservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("LeadSource Domain Model Tests")
class LeadSourceTest {

    @Nested
    @DisplayName("Lead Source Creation Tests")
    class LeadSourceCreationTests {

        @Test
        @DisplayName("Should create lead source with valid parameters")
        void shouldCreateLeadSourceWithValidParameters() {
            LeadSource source = LeadSource.builder()
                    .name("Website Contact Form")
                    .channel(LeadChannel.WEBSITE)
                    .campaignId("campaign-123")
                    .build();

            assertThat(source).isNotNull();
            assertThat(source.getName()).isEqualTo("Website Contact Form");
            assertThat(source.getChannel()).isEqualTo(LeadChannel.WEBSITE);
            assertThat(source.getCampaignId()).isEqualTo("campaign-123");
        }

        @Test
        @DisplayName("Should reject null source name")
        void shouldRejectNullName() {
            assertThatThrownBy(() -> LeadSource.builder()
                    .name(null)
                    .channel(LeadChannel.EMAIL)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Source name cannot be null");
        }

        @Test
        @DisplayName("Should reject empty source name")
        void shouldRejectEmptyName() {
            assertThatThrownBy(() -> LeadSource.builder()
                    .name("")
                    .channel(LeadChannel.EMAIL)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Source name cannot be empty");
        }

        @Test
        @DisplayName("Should reject null channel")
        void shouldRejectNullChannel() {
            assertThatThrownBy(() -> LeadSource.builder()
                    .name("Test Source")
                    .channel(null)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Channel cannot be null");
        }
    }

    @Nested
    @DisplayName("Channel Quality Tests")
    class ChannelQualityTests {

        @Test
        @DisplayName("Should assign high quality to organic search")
        void shouldAssignHighQualityToOrganic() {
            LeadSource source = LeadSource.builder()
                    .name("Organic Search")
                    .channel(LeadChannel.ORGANIC_SEARCH)
                    .build();

            assertThat(source.getQualityScore()).isGreaterThan(70);
        }

        @Test
        @DisplayName("Should assign high quality to referrals")
        void shouldAssignHighQualityToReferrals() {
            LeadSource source = LeadSource.builder()
                    .name("Customer Referral")
                    .channel(LeadChannel.REFERRAL)
                    .build();

            assertThat(source.getQualityScore()).isGreaterThan(80);
        }

        @Test
        @DisplayName("Should assign medium quality to paid ads")
        void shouldAssignMediumQualityToPaidAds() {
            LeadSource source = LeadSource.builder()
                    .name("Google Ads")
                    .channel(LeadChannel.PAID_ADVERTISING)
                    .build();

            assertThat(source.getQualityScore()).isBetween(50.0, 70.0);
        }
    }

    @Nested
    @DisplayName("Source Attribution Tests")
    class SourceAttributionTests {

        @Test
        @DisplayName("Should track conversion rate")
        void shouldTrackConversionRate() {
            LeadSource source = LeadSource.builder()
                    .name("Email Campaign")
                    .channel(LeadChannel.EMAIL)
                    .build();

            LeadSource updated = source.withConversionData(100, 25);

            assertThat(updated.getConversionRate()).isEqualTo(0.25);
        }

        @Test
        @DisplayName("Should calculate cost per lead")
        void shouldCalculateCostPerLead() {
            LeadSource source = LeadSource.builder()
                    .name("LinkedIn Ads")
                    .channel(LeadChannel.SOCIAL_MEDIA)
                    .build();

            LeadSource updated = source.withCostData(5000.0, 100);

            assertThat(updated.getCostPerLead()).isEqualTo(50.0);
        }

        @Test
        @DisplayName("Should identify high performing source")
        void shouldIdentifyHighPerformingSource() {
            LeadSource source = LeadSource.builder()
                    .name("Referral Program")
                    .channel(LeadChannel.REFERRAL)
                    .conversionRate(0.35)
                    .build();

            assertThat(source.isHighPerforming()).isTrue();
        }
    }

    @Nested
    @DisplayName("Channel Classification Tests")
    class ChannelClassificationTests {

        @ParameterizedTest
        @EnumSource(value = LeadChannel.class, names = {"ORGANIC_SEARCH", "REFERRAL", "DIRECT"})
        @DisplayName("Should classify as organic channel")
        void shouldClassifyAsOrganic(LeadChannel channel) {
            assertThat(channel.isOrganic()).isTrue();
            assertThat(channel.isPaid()).isFalse();
        }

        @ParameterizedTest
        @EnumSource(value = LeadChannel.class, names = {"PAID_ADVERTISING", "SOCIAL_MEDIA", "DISPLAY_AD"})
        @DisplayName("Should classify as paid channel")
        void shouldClassifyAsPaid(LeadChannel channel) {
            assertThat(channel.isPaid()).isTrue();
            assertThat(channel.isOrganic()).isFalse();
        }
    }
}
