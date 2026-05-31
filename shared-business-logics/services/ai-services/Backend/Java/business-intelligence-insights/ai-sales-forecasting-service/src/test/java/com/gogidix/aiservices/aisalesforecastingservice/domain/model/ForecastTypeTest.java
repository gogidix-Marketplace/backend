package com.gogidix.aiservices.aisalesforecastingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ForecastType Enum Tests")
class ForecastTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(ForecastType.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(ForecastType value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have BEHAVIORAL enum value")
        void shouldHaveBehavioralValue() {
            assertThat(ForecastType.valueOf("BEHAVIORAL")).isEqualTo(ForecastType.BEHAVIORAL);
        }

        @Test
        @DisplayName("Should have DEMOGRAPHIC enum value")
        void shouldHaveDemographicValue() {
            assertThat(ForecastType.valueOf("DEMOGRAPHIC")).isEqualTo(ForecastType.DEMOGRAPHIC);
        }

        @Test
        @DisplayName("Should have TRANSACTIONAL enum value")
        void shouldHaveTransactionalValue() {
            assertThat(ForecastType.valueOf("TRANSACTIONAL")).isEqualTo(ForecastType.TRANSACTIONAL);
        }

        @Test
        @DisplayName("Should have CUSTOM enum value")
        void shouldHaveCustomValue() {
            assertThat(ForecastType.valueOf("CUSTOM")).isEqualTo(ForecastType.CUSTOM);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            ForecastType[] values = ForecastType.values();
            assertThat(values).hasSize(4);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            ForecastType[] values = ForecastType.values();
            assertThat(values).containsExactly(
                    ForecastType.BEHAVIORAL,
                    ForecastType.DEMOGRAPHIC,
                    ForecastType.TRANSACTIONAL,
                    ForecastType.CUSTOM
            );
        }
    }
}
