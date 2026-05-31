package com.gogidix.aiservices.aichurnpredictionservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PredictionStatus Enum Tests")
class PredictionStatusTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(PredictionStatus.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(PredictionStatus value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have DRAFT enum value")
        void shouldHaveDraftValue() {
            assertThat(PredictionStatus.valueOf("DRAFT")).isEqualTo(PredictionStatus.DRAFT);
        }

        @Test
        @DisplayName("Should have ACTIVE enum value")
        void shouldHaveActiveValue() {
            assertThat(PredictionStatus.valueOf("ACTIVE")).isEqualTo(PredictionStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should have INACTIVE enum value")
        void shouldHaveInactiveValue() {
            assertThat(PredictionStatus.valueOf("INACTIVE")).isEqualTo(PredictionStatus.INACTIVE);
        }

        @Test
        @DisplayName("Should have ARCHIVED enum value")
        void shouldHaveArchivedValue() {
            assertThat(PredictionStatus.valueOf("ARCHIVED")).isEqualTo(PredictionStatus.ARCHIVED);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            PredictionStatus[] values = PredictionStatus.values();
            assertThat(values).hasSize(4);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            PredictionStatus[] values = PredictionStatus.values();
            assertThat(values).containsExactly(
                    PredictionStatus.DRAFT,
                    PredictionStatus.ACTIVE,
                    PredictionStatus.INACTIVE,
                    PredictionStatus.ARCHIVED
            );
        }
    }
}
