package com.gogidix.aiservices.aisalesforecastingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ForecastStatus Enum Tests")
class ForecastStatusTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(ForecastStatus.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(ForecastStatus value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have DRAFT enum value")
        void shouldHaveDraftValue() {
            assertThat(ForecastStatus.valueOf("DRAFT")).isEqualTo(ForecastStatus.DRAFT);
        }

        @Test
        @DisplayName("Should have ACTIVE enum value")
        void shouldHaveActiveValue() {
            assertThat(ForecastStatus.valueOf("ACTIVE")).isEqualTo(ForecastStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should have INACTIVE enum value")
        void shouldHaveInactiveValue() {
            assertThat(ForecastStatus.valueOf("INACTIVE")).isEqualTo(ForecastStatus.INACTIVE);
        }

        @Test
        @DisplayName("Should have ARCHIVED enum value")
        void shouldHaveArchivedValue() {
            assertThat(ForecastStatus.valueOf("ARCHIVED")).isEqualTo(ForecastStatus.ARCHIVED);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            ForecastStatus[] values = ForecastStatus.values();
            assertThat(values).hasSize(4);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            ForecastStatus[] values = ForecastStatus.values();
            assertThat(values).containsExactly(
                    ForecastStatus.DRAFT,
                    ForecastStatus.ACTIVE,
                    ForecastStatus.INACTIVE,
                    ForecastStatus.ARCHIVED
            );
        }
    }
}
