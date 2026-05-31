package com.gogidix.aiservices.intelligenceanalysisservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AnalysisType Enum Tests")
class AnalysisTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(AnalysisType.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(AnalysisType value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have BEHAVIORAL enum value")
        void shouldHaveBehavioralValue() {
            assertThat(AnalysisType.valueOf("BEHAVIORAL")).isEqualTo(AnalysisType.BEHAVIORAL);
        }

        @Test
        @DisplayName("Should have DEMOGRAPHIC enum value")
        void shouldHaveDemographicValue() {
            assertThat(AnalysisType.valueOf("DEMOGRAPHIC")).isEqualTo(AnalysisType.DEMOGRAPHIC);
        }

        @Test
        @DisplayName("Should have TRANSACTIONAL enum value")
        void shouldHaveTransactionalValue() {
            assertThat(AnalysisType.valueOf("TRANSACTIONAL")).isEqualTo(AnalysisType.TRANSACTIONAL);
        }

        @Test
        @DisplayName("Should have CUSTOM enum value")
        void shouldHaveCustomValue() {
            assertThat(AnalysisType.valueOf("CUSTOM")).isEqualTo(AnalysisType.CUSTOM);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            AnalysisType[] values = AnalysisType.values();
            assertThat(values).hasSize(4);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            AnalysisType[] values = AnalysisType.values();
            assertThat(values).containsExactly(
                    AnalysisType.BEHAVIORAL,
                    AnalysisType.DEMOGRAPHIC,
                    AnalysisType.TRANSACTIONAL,
                    AnalysisType.CUSTOM
            );
        }
    }
}
