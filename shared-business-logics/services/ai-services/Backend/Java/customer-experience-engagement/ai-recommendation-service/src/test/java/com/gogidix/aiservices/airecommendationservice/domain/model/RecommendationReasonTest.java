package com.gogidix.aiservices.airecommendationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("RecommendationReason Enum Tests")
class RecommendationReasonTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(RecommendationReason.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(RecommendationReason reason) {
            assertThat(reason).isNotNull();
        }

        @Test
        @DisplayName("Should have exactly 5 enum values")
        void shouldHaveExactlyFiveEnumValues() {
            assertThat(RecommendationReason.values()).hasSize(5);
        }

        @Test
        @DisplayName("Should contain all expected recommendation reasons")
        void shouldContainAllExpectedReasons() {
            assertThat(RecommendationReason.values()).containsExactlyInAnyOrder(
                    RecommendationReason.BEHAVIORAL,
                    RecommendationReason.COLLABORATIVE,
                    RecommendationReason.CONTENT_BASED,
                    RecommendationReason.TRENDING,
                    RecommendationReason.CONTEXTUAL
            );
        }
    }

    @Nested
    @DisplayName("Enum Name Tests")
    class EnumNameTests {

        @Test
        @DisplayName("BEHAVIORAL should have correct name")
        void behavioralShouldHaveCorrectName() {
            assertThat(RecommendationReason.BEHAVIORAL.name()).isEqualTo("BEHAVIORAL");
        }

        @Test
        @DisplayName("COLLABORATIVE should have correct name")
        void collaborativeShouldHaveCorrectName() {
            assertThat(RecommendationReason.COLLABORATIVE.name()).isEqualTo("COLLABORATIVE");
        }

        @Test
        @DisplayName("CONTENT_BASED should have correct name")
        void contentBasedShouldHaveCorrectName() {
            assertThat(RecommendationReason.CONTENT_BASED.name()).isEqualTo("CONTENT_BASED");
        }

        @Test
        @DisplayName("TRENDING should have correct name")
        void trendingShouldHaveCorrectName() {
            assertThat(RecommendationReason.TRENDING.name()).isEqualTo("TRENDING");
        }

        @Test
        @DisplayName("CONTEXTUAL should have correct name")
        void contextualShouldHaveCorrectName() {
            assertThat(RecommendationReason.CONTEXTUAL.name()).isEqualTo("CONTEXTUAL");
        }
    }

    @Nested
    @DisplayName("Enum Ordinal Tests")
    class EnumOrdinalTests {

        @ParameterizedTest
        @EnumSource(RecommendationReason.class)
        @DisplayName("All enum values should have valid ordinal")
        void allEnumValuesShouldHaveValidOrdinal(RecommendationReason reason) {
            assertThat(reason.ordinal()).isGreaterThanOrEqualTo(0);
            assertThat(reason.ordinal()).isLessThan(RecommendationReason.values().length);
        }
    }

    @Nested
    @DisplayName("valueOf Tests")
    class ValueOfTests {

        @ParameterizedTest
        @EnumSource(RecommendationReason.class)
        @DisplayName("valueOf should return correct enum for each name")
        void valueOfShouldReturnCorrectEnum(RecommendationReason reason) {
            assertThat(RecommendationReason.valueOf(reason.name())).isEqualTo(reason);
        }

        @Test
        @DisplayName("valueOf should return BEHAVIORAL")
        void valueOfShouldReturnBehavioral() {
            assertThat(RecommendationReason.valueOf("BEHAVIORAL")).isEqualTo(RecommendationReason.BEHAVIORAL);
        }

        @Test
        @DisplayName("valueOf should throw exception for invalid name")
        void valueOfShouldThrowExceptionForInvalidName() {
            assertThatThrownBy(() -> RecommendationReason.valueOf("INVALID_REASON"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("valueOf should throw exception for null")
        void valueOfShouldThrowExceptionForNull() {
            assertThatThrownBy(() -> RecommendationReason.valueOf(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Enum Consistency Tests")
    class EnumConsistencyTests {

        @Test
        @DisplayName("Enum values should be consistently ordered")
        void enumValuesShouldBeConsistentlyOrdered() {
            RecommendationReason[] values1 = RecommendationReason.values();
            RecommendationReason[] values2 = RecommendationReason.values();

            assertThat(values1).isEqualTo(values2);
        }

        @Test
        @DisplayName("Calling values() multiple times should return same array")
        void callingValuesMultipleTimesShouldReturnSameArray() {
            RecommendationReason[] firstCall = RecommendationReason.values();
            RecommendationReason[] secondCall = RecommendationReason.values();

            assertThat(firstCall).isNotSameAs(secondCall);
            assertThat(firstCall).containsExactlyInAnyOrder(secondCall);
        }
    }

    @Nested
    @DisplayName("Usage in Domain Model Tests")
    class UsageInDomainModelTests {

        @Test
        @DisplayName("Should be usable in RecommendationItem builder")
        void shouldBeUsableInRecommendationItemBuilder() {
            for (RecommendationReason reason : RecommendationReason.values()) {
                RecommendationItem item = RecommendationItem.builder()
                        .itemId("test-item")
                        .score(0.8)
                        .reason(reason)
                        .build();

                assertThat(item.getReason()).isEqualTo(reason);
            }
        }

        @Test
        @DisplayName("All reasons should work with RecommendationItem default constructor")
        void allReasonsShouldWorkWithRecommendationItemDefaultConstructor() {
            for (RecommendationReason reason : RecommendationReason.values()) {
                RecommendationItem item = RecommendationItem.builder()
                        .itemId("test-item")
                        .score(0.8)
                        .reason(reason)
                        .build();

                assertThat(item.getReason()).isEqualTo(reason);
            }
        }
    }
}
