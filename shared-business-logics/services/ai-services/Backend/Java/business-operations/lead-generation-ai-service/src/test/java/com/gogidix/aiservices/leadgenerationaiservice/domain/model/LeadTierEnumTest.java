package com.gogidix.aiservices.leadgenerationaiservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("LeadTier Enum Tests")
class LeadTierEnumTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(LeadTier.class)
        @DisplayName("Should have all LeadTier values")
        void shouldHaveAllValues(LeadTier tier) {
            assertThat(tier).isNotNull();
        }

        @Test
        @DisplayName("Should have exactly 4 LeadTier values")
        void shouldHave4Values() {
            assertThat(LeadTier.values()).hasSize(4);
        }

        @Test
        @DisplayName("Should contain HOT_LEAD")
        void shouldContainHotLead() {
            assertThat(LeadTier.valueOf("HOT_LEAD")).isEqualTo(LeadTier.HOT_LEAD);
        }

        @Test
        @DisplayName("Should contain HIGH_QUALITY")
        void shouldContainHighQuality() {
            assertThat(LeadTier.valueOf("HIGH_QUALITY")).isEqualTo(LeadTier.HIGH_QUALITY);
        }

        @Test
        @DisplayName("Should contain MEDIUM_QUALITY")
        void shouldContainMediumQuality() {
            assertThat(LeadTier.valueOf("MEDIUM_QUALITY")).isEqualTo(LeadTier.MEDIUM_QUALITY);
        }

        @Test
        @DisplayName("Should contain LOW_QUALITY")
        void shouldContainLowQuality() {
            assertThat(LeadTier.valueOf("LOW_QUALITY")).isEqualTo(LeadTier.LOW_QUALITY);
        }
    }

    @Nested
    @DisplayName("fromScore() Tests")
    class FromScoreTests {

        @Test
        @DisplayName("Should return HOT_LEAD for score 90+")
        void shouldReturnHotLeadFor90Plus() {
            assertThat(LeadTier.fromScore(90)).isEqualTo(LeadTier.HOT_LEAD);
            assertThat(LeadTier.fromScore(95)).isEqualTo(LeadTier.HOT_LEAD);
            assertThat(LeadTier.fromScore(100)).isEqualTo(LeadTier.HOT_LEAD);
        }

        @Test
        @DisplayName("Should return HIGH_QUALITY for score 70-89")
        void shouldReturnHighQualityFor70to89() {
            assertThat(LeadTier.fromScore(70)).isEqualTo(LeadTier.HIGH_QUALITY);
            assertThat(LeadTier.fromScore(80)).isEqualTo(LeadTier.HIGH_QUALITY);
            assertThat(LeadTier.fromScore(89)).isEqualTo(LeadTier.HIGH_QUALITY);
        }

        @Test
        @DisplayName("Should return MEDIUM_QUALITY for score 50-69")
        void shouldReturnMediumQualityFor50to69() {
            assertThat(LeadTier.fromScore(50)).isEqualTo(LeadTier.MEDIUM_QUALITY);
            assertThat(LeadTier.fromScore(60)).isEqualTo(LeadTier.MEDIUM_QUALITY);
            assertThat(LeadTier.fromScore(69)).isEqualTo(LeadTier.MEDIUM_QUALITY);
        }

        @Test
        @DisplayName("Should return LOW_QUALITY for score below 50")
        void shouldReturnLowQualityForBelow50() {
            assertThat(LeadTier.fromScore(0)).isEqualTo(LeadTier.LOW_QUALITY);
            assertThat(LeadTier.fromScore(25)).isEqualTo(LeadTier.LOW_QUALITY);
            assertThat(LeadTier.fromScore(49)).isEqualTo(LeadTier.LOW_QUALITY);
        }

        @Test
        @DisplayName("Should handle boundary values")
        void shouldHandleBoundaryValues() {
            assertThat(LeadTier.fromScore(90)).isEqualTo(LeadTier.HOT_LEAD);
            assertThat(LeadTier.fromScore(89)).isEqualTo(LeadTier.HIGH_QUALITY);
            assertThat(LeadTier.fromScore(70)).isEqualTo(LeadTier.HIGH_QUALITY);
            assertThat(LeadTier.fromScore(69)).isEqualTo(LeadTier.MEDIUM_QUALITY);
            assertThat(LeadTier.fromScore(50)).isEqualTo(LeadTier.MEDIUM_QUALITY);
            assertThat(LeadTier.fromScore(49)).isEqualTo(LeadTier.LOW_QUALITY);
        }

        @Test
        @DisplayName("Should handle negative scores")
        void shouldHandleNegativeScores() {
            assertThat(LeadTier.fromScore(-10)).isEqualTo(LeadTier.LOW_QUALITY);
        }
    }

    @Nested
    @DisplayName("fromString() Tests")
    class FromStringTests {

        @Test
        @DisplayName("Should parse HOT_LEAD from string")
        void shouldParseHotLead() {
            assertThat(LeadTier.fromString("hot_lead")).isEqualTo(LeadTier.HOT_LEAD);
        }

        @Test
        @DisplayName("Should parse case insensitive")
        void shouldParseCaseInsensitive() {
            assertThat(LeadTier.fromString("HOT_LEAD")).isEqualTo(LeadTier.HOT_LEAD);
            assertThat(LeadTier.fromString("Hot_Lead")).isEqualTo(LeadTier.HOT_LEAD);
            assertThat(LeadTier.fromString("hOt_lEaD")).isEqualTo(LeadTier.HOT_LEAD);
        }

        @ParameterizedTest
        @ValueSource(strings = {"hot_lead", "high_quality", "medium_quality", "low_quality"})
        @DisplayName("Should parse all valid values")
        void shouldParseAllValidValues(String value) {
            assertThat(LeadTier.fromString(value)).isNotNull();
        }

        @Test
        @DisplayName("Should throw exception for invalid value")
        void shouldThrowForInvalidValue() {
            assertThatThrownBy(() -> LeadTier.fromString("invalid"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Unknown lead tier");
        }
    }

    @Nested
    @DisplayName("getValue() Tests")
    class GetValueTests {

        @Test
        @DisplayName("Should return correct values for each tier")
        void shouldReturnCorrectValues() {
            assertThat(LeadTier.HOT_LEAD.getValue()).isEqualTo("hot_lead");
            assertThat(LeadTier.HIGH_QUALITY.getValue()).isEqualTo("high_quality");
            assertThat(LeadTier.MEDIUM_QUALITY.getValue()).isEqualTo("medium_quality");
            assertThat(LeadTier.LOW_QUALITY.getValue()).isEqualTo("low_quality");
        }
    }

    @Nested
    @DisplayName("getMinScore() Tests")
    class GetMinScoreTests {

        @Test
        @DisplayName("Should return correct minimum scores")
        void shouldReturnCorrectMinScores() {
            assertThat(LeadTier.HOT_LEAD.getMinScore()).isEqualTo(90);
            assertThat(LeadTier.HIGH_QUALITY.getMinScore()).isEqualTo(70);
            assertThat(LeadTier.MEDIUM_QUALITY.getMinScore()).isEqualTo(50);
            assertThat(LeadTier.LOW_QUALITY.getMinScore()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("toString() Tests")
    class ToStringTests {

        @ParameterizedTest
        @EnumSource(LeadTier.class)
        @DisplayName("Should return value string for all tiers")
        void shouldReturnValueString(LeadTier tier) {
            assertThat(tier.toString()).isEqualTo(tier.getValue());
        }
    }
}
