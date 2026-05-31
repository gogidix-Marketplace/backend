package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RecommendationType Enum Tests")
class RecommendationTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(RecommendationType.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(RecommendationType value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have BEHAVIORAL enum value")
        void shouldHaveBehavioralValue() {
            assertThat(RecommendationType.valueOf("BEHAVIORAL")).isEqualTo(RecommendationType.BEHAVIORAL);
        }

        @Test
        @DisplayName("Should have DEMOGRAPHIC enum value")
        void shouldHaveDemographicValue() {
            assertThat(RecommendationType.valueOf("DEMOGRAPHIC")).isEqualTo(RecommendationType.DEMOGRAPHIC);
        }

        @Test
        @DisplayName("Should have TRANSACTIONAL enum value")
        void shouldHaveTransactionalValue() {
            assertThat(RecommendationType.valueOf("TRANSACTIONAL")).isEqualTo(RecommendationType.TRANSACTIONAL);
        }

        @Test
        @DisplayName("Should have CUSTOM enum value")
        void shouldHaveCustomValue() {
            assertThat(RecommendationType.valueOf("CUSTOM")).isEqualTo(RecommendationType.CUSTOM);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            RecommendationType[] values = RecommendationType.values();
            assertThat(values).hasSize(10);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            RecommendationType[] values = RecommendationType.values();
            assertThat(values).containsExactly(
                    RecommendationType.BEHAVIORAL,
                    RecommendationType.DEMOGRAPHIC,
                    RecommendationType.TRANSACTIONAL,
                    RecommendationType.CUSTOM,
                    RecommendationType.COLLABORATIVE_FILTERING,
                    RecommendationType.PERSONALIZED_BASED_ON_HISTORY,
                    RecommendationType.TREND_BASED,
                    RecommendationType.CROSS_SELL,
                    RecommendationType.CONTENT_BASED,
                    RecommendationType.HYBRID
            );
        }
    }
}
