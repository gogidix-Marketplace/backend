package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RecommendationStatus Enum Tests")
class RecommendationStatusTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(RecommendationStatus.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(RecommendationStatus value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have DRAFT enum value")
        void shouldHaveDraftValue() {
            assertThat(RecommendationStatus.valueOf("DRAFT")).isEqualTo(RecommendationStatus.DRAFT);
        }

        @Test
        @DisplayName("Should have ACTIVE enum value")
        void shouldHaveActiveValue() {
            assertThat(RecommendationStatus.valueOf("ACTIVE")).isEqualTo(RecommendationStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should have INACTIVE enum value")
        void shouldHaveInactiveValue() {
            assertThat(RecommendationStatus.valueOf("INACTIVE")).isEqualTo(RecommendationStatus.INACTIVE);
        }

        @Test
        @DisplayName("Should have ARCHIVED enum value")
        void shouldHaveArchivedValue() {
            assertThat(RecommendationStatus.valueOf("ARCHIVED")).isEqualTo(RecommendationStatus.ARCHIVED);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            RecommendationStatus[] values = RecommendationStatus.values();
            assertThat(values).hasSize(4);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            RecommendationStatus[] values = RecommendationStatus.values();
            assertThat(values).containsExactly(
                    RecommendationStatus.DRAFT,
                    RecommendationStatus.ACTIVE,
                    RecommendationStatus.INACTIVE,
                    RecommendationStatus.ARCHIVED
            );
        }
    }
}
