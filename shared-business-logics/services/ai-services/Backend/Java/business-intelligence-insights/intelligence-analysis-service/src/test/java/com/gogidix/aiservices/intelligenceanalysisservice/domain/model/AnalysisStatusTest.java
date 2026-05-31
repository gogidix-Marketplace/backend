package com.gogidix.aiservices.intelligenceanalysisservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AnalysisStatus Enum Tests")
class AnalysisStatusTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(AnalysisStatus.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(AnalysisStatus value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have DRAFT enum value")
        void shouldHaveDraftValue() {
            assertThat(AnalysisStatus.valueOf("DRAFT")).isEqualTo(AnalysisStatus.DRAFT);
        }

        @Test
        @DisplayName("Should have ACTIVE enum value")
        void shouldHaveActiveValue() {
            assertThat(AnalysisStatus.valueOf("ACTIVE")).isEqualTo(AnalysisStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should have INACTIVE enum value")
        void shouldHaveInactiveValue() {
            assertThat(AnalysisStatus.valueOf("INACTIVE")).isEqualTo(AnalysisStatus.INACTIVE);
        }

        @Test
        @DisplayName("Should have ARCHIVED enum value")
        void shouldHaveArchivedValue() {
            assertThat(AnalysisStatus.valueOf("ARCHIVED")).isEqualTo(AnalysisStatus.ARCHIVED);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            AnalysisStatus[] values = AnalysisStatus.values();
            assertThat(values).hasSize(4);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            AnalysisStatus[] values = AnalysisStatus.values();
            assertThat(values).containsExactly(
                    AnalysisStatus.DRAFT,
                    AnalysisStatus.ACTIVE,
                    AnalysisStatus.INACTIVE,
                    AnalysisStatus.ARCHIVED
            );
        }
    }
}
