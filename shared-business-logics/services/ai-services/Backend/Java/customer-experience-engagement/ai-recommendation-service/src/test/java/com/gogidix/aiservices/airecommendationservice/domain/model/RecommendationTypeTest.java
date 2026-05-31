package com.gogidix.aiservices.airecommendationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("RecommendationType Enum Tests")
class RecommendationTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(RecommendationType.class)
        @DisplayName("Should have all enum values with non-null properties")
        void shouldHaveAllEnumValuesWithNonNullProperties(RecommendationType type) {
            assertThat(type).isNotNull();
            assertThat(type.getValue()).isNotNull();
            assertThat(type.getDescription()).isNotNull();
        }

        @Test
        @DisplayName("Should have exactly 5 enum values")
        void shouldHaveExactlyFiveEnumValues() {
            assertThat(RecommendationType.values()).hasSize(5);
        }

        @Test
        @DisplayName("Should contain all expected recommendation types")
        void shouldContainAllExpectedTypes() {
            assertThat(RecommendationType.values()).containsExactlyInAnyOrder(
                    RecommendationType.COLLABORATIVE,
                    RecommendationType.CONTENT_BASED,
                    RecommendationType.HYBRID,
                    RecommendationType.POPULAR,
                    RecommendationType.PERSONALIZED
            );
        }
    }

    @Nested
    @DisplayName("getValue() Tests")
    class GetValueTests {

        @Test
        @DisplayName("COLLABORATIVE should have correct value")
        void collaborativeShouldHaveCorrectValue() {
            assertThat(RecommendationType.COLLABORATIVE.getValue()).isEqualTo("collaborative");
        }

        @Test
        @DisplayName("CONTENT_BASED should have correct value")
        void contentBasedShouldHaveCorrectValue() {
            assertThat(RecommendationType.CONTENT_BASED.getValue()).isEqualTo("content_based");
        }

        @Test
        @DisplayName("HYBRID should have correct value")
        void hybridShouldHaveCorrectValue() {
            assertThat(RecommendationType.HYBRID.getValue()).isEqualTo("hybrid");
        }

        @Test
        @DisplayName("POPULAR should have correct value")
        void popularShouldHaveCorrectValue() {
            assertThat(RecommendationType.POPULAR.getValue()).isEqualTo("popular");
        }

        @Test
        @DisplayName("PERSONALIZED should have correct value")
        void personalizedShouldHaveCorrectValue() {
            assertThat(RecommendationType.PERSONALIZED.getValue()).isEqualTo("personalized");
        }
    }

    @Nested
    @DisplayName("getDescription() Tests")
    class GetDescriptionTests {

        @Test
        @DisplayName("All descriptions should be non-empty")
        void allDescriptionsShouldBeNonEmpty() {
            for (RecommendationType type : RecommendationType.values()) {
                assertThat(type.getDescription()).isNotEmpty();
            }
        }
    }

    @Nested
    @DisplayName("toString() Tests")
    class ToStringTests {

        @ParameterizedTest
        @EnumSource(RecommendationType.class)
        @DisplayName("toString should return the value")
        void toStringShouldReturnValue(RecommendationType type) {
            assertThat(type.toString()).isEqualTo(type.getValue());
        }
    }

    @Nested
    @DisplayName("fromString() Tests")
    class FromStringTests {

        @ParameterizedTest
        @ValueSource(strings = {"collaborative", "COLLABORATIVE", "Collaborative", "CoLlAbOrAtIvE"})
        @DisplayName("Should parse collaborative case-insensitively")
        void shouldParseCollaborativeCaseInsensitively(String value) {
            assertThat(RecommendationType.fromString(value)).isEqualTo(RecommendationType.COLLABORATIVE);
        }

        @ParameterizedTest
        @ValueSource(strings = {"content_based", "CONTENT_BASED", "Content_Based"})
        @DisplayName("Should parse content_based case-insensitively")
        void shouldParseContentBasedCaseInsensitively(String value) {
            assertThat(RecommendationType.fromString(value)).isEqualTo(RecommendationType.CONTENT_BASED);
        }

        @ParameterizedTest
        @ValueSource(strings = {"hybrid", "HYBRID", "Hybrid"})
        @DisplayName("Should parse hybrid case-insensitively")
        void shouldParseHybridCaseInsensitively(String value) {
            assertThat(RecommendationType.fromString(value)).isEqualTo(RecommendationType.HYBRID);
        }

        @ParameterizedTest
        @ValueSource(strings = {"popular", "POPULAR", "Popular"})
        @DisplayName("Should parse popular case-insensitively")
        void shouldParsePopularCaseInsensitively(String value) {
            assertThat(RecommendationType.fromString(value)).isEqualTo(RecommendationType.POPULAR);
        }

        @ParameterizedTest
        @ValueSource(strings = {"personalized", "PERSONALIZED", "Personalized"})
        @DisplayName("Should parse personalized case-insensitively")
        void shouldParsePersonalizedCaseInsensitively(String value) {
            assertThat(RecommendationType.fromString(value)).isEqualTo(RecommendationType.PERSONALIZED);
        }

        @Test
        @DisplayName("Should throw exception for unknown type")
        void shouldThrowExceptionForUnknownType() {
            assertThatThrownBy(() -> RecommendationType.fromString("unknown_type"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Unknown recommendation type");
        }

        @Test
        @DisplayName("Should throw exception for null input")
        void shouldThrowExceptionForNullInput() {
            assertThatThrownBy(() -> RecommendationType.fromString(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should throw exception for empty string")
        void shouldThrowExceptionForEmptyString() {
            assertThatThrownBy(() -> RecommendationType.fromString(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Enum Value Of Tests")
    class EnumValueOfTests {

        @ParameterizedTest
        @EnumSource(RecommendationType.class)
        @DisplayName("valueOf should return correct enum for name")
        void valueOfShouldReturnCorrectEnum(RecommendationType type) {
            assertThat(RecommendationType.valueOf(type.name())).isEqualTo(type);
        }
    }
}
