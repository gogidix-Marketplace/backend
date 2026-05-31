package com.gogidix.aiservices.aicustomersegmentationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SegmentStatus Enum Tests")
class SegmentStatusTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(SegmentStatus.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(SegmentStatus value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have DRAFT enum value")
        void shouldHaveDraftValue() {
            assertThat(SegmentStatus.valueOf("DRAFT")).isEqualTo(SegmentStatus.DRAFT);
        }

        @Test
        @DisplayName("Should have ACTIVE enum value")
        void shouldHaveActiveValue() {
            assertThat(SegmentStatus.valueOf("ACTIVE")).isEqualTo(SegmentStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should have INACTIVE enum value")
        void shouldHaveInactiveValue() {
            assertThat(SegmentStatus.valueOf("INACTIVE")).isEqualTo(SegmentStatus.INACTIVE);
        }

        @Test
        @DisplayName("Should have ARCHIVED enum value")
        void shouldHaveArchivedValue() {
            assertThat(SegmentStatus.valueOf("ARCHIVED")).isEqualTo(SegmentStatus.ARCHIVED);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            SegmentStatus[] values = SegmentStatus.values();
            assertThat(values).hasSize(4);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            SegmentStatus[] values = SegmentStatus.values();
            assertThat(values).containsExactly(
                    SegmentStatus.DRAFT,
                    SegmentStatus.ACTIVE,
                    SegmentStatus.INACTIVE,
                    SegmentStatus.ARCHIVED
            );
        }
    }
}
