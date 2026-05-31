package com.gogidix.aiservices.aicustomersegmentationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SegmentType Enum Tests")
class SegmentTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(SegmentType.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(SegmentType value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have BEHAVIORAL enum value")
        void shouldHaveBehavioralValue() {
            assertThat(SegmentType.valueOf("BEHAVIORAL")).isEqualTo(SegmentType.BEHAVIORAL);
        }

        @Test
        @DisplayName("Should have DEMOGRAPHIC enum value")
        void shouldHaveDemographicValue() {
            assertThat(SegmentType.valueOf("DEMOGRAPHIC")).isEqualTo(SegmentType.DEMOGRAPHIC);
        }

        @Test
        @DisplayName("Should have TRANSACTIONAL enum value")
        void shouldHaveTransactionalValue() {
            assertThat(SegmentType.valueOf("TRANSACTIONAL")).isEqualTo(SegmentType.TRANSACTIONAL);
        }

        @Test
        @DisplayName("Should have CUSTOM enum value")
        void shouldHaveCustomValue() {
            assertThat(SegmentType.valueOf("CUSTOM")).isEqualTo(SegmentType.CUSTOM);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            SegmentType[] values = SegmentType.values();
            assertThat(values).hasSize(4);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            SegmentType[] values = SegmentType.values();
            assertThat(values).containsExactly(
                    SegmentType.BEHAVIORAL,
                    SegmentType.DEMOGRAPHIC,
                    SegmentType.TRANSACTIONAL,
                    SegmentType.CUSTOM
            );
        }
    }
}
