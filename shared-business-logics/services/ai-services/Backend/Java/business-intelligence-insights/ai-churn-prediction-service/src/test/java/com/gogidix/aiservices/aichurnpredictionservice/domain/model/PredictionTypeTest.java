package com.gogidix.aiservices.aichurnpredictionservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PredictionType Enum Tests")
class PredictionTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(PredictionType.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(PredictionType value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have BEHAVIORAL enum value")
        void shouldHaveBehavioralValue() {
            assertThat(PredictionType.valueOf("BEHAVIORAL")).isEqualTo(PredictionType.BEHAVIORAL);
        }

        @Test
        @DisplayName("Should have DEMOGRAPHIC enum value")
        void shouldHaveDemographicValue() {
            assertThat(PredictionType.valueOf("DEMOGRAPHIC")).isEqualTo(PredictionType.DEMOGRAPHIC);
        }

        @Test
        @DisplayName("Should have TRANSACTIONAL enum value")
        void shouldHaveTransactionalValue() {
            assertThat(PredictionType.valueOf("TRANSACTIONAL")).isEqualTo(PredictionType.TRANSACTIONAL);
        }

        @Test
        @DisplayName("Should have CUSTOM enum value")
        void shouldHaveCustomValue() {
            assertThat(PredictionType.valueOf("CUSTOM")).isEqualTo(PredictionType.CUSTOM);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            PredictionType[] values = PredictionType.values();
            assertThat(values).hasSize(4);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            PredictionType[] values = PredictionType.values();
            assertThat(values).containsExactly(
                    PredictionType.BEHAVIORAL,
                    PredictionType.DEMOGRAPHIC,
                    PredictionType.TRANSACTIONAL,
                    PredictionType.CUSTOM
            );
        }
    }
}
