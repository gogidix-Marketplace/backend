package com.gogidix.aiservices.aisearchservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SearchType Enum Tests")
class SearchTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(SearchType.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(SearchType type) {
            assertThat(type).isNotNull();
        }

        @Test
        @DisplayName("Should have exactly 5 enum values")
        void shouldHaveExactlyFiveEnumValues() {
            assertThat(SearchType.values()).hasSize(5);
        }

        @Test
        @DisplayName("Should contain all expected search types")
        void shouldContainAllExpectedTypes() {
            assertThat(SearchType.values()).containsExactlyInAnyOrder(
                    SearchType.FULLTEXT,
                    SearchType.SEMANTIC,
                    SearchType.FUZZY,
                    SearchType.FACETED,
                    SearchType.HYBRID
            );
        }
    }

    @Nested
    @DisplayName("Enum Name Tests")
    class EnumNameTests {

        @Test
        @DisplayName("FULLTEXT should have correct name")
        void fulltextShouldHaveCorrectName() {
            assertThat(SearchType.FULLTEXT.name()).isEqualTo("FULLTEXT");
        }

        @Test
        @DisplayName("SEMANTIC should have correct name")
        void semanticShouldHaveCorrectName() {
            assertThat(SearchType.SEMANTIC.name()).isEqualTo("SEMANTIC");
        }

        @Test
        @DisplayName("FUZZY should have correct name")
        void fuzzyShouldHaveCorrectName() {
            assertThat(SearchType.FUZZY.name()).isEqualTo("FUZZY");
        }

        @Test
        @DisplayName("FACETED should have correct name")
        void facetedShouldHaveCorrectName() {
            assertThat(SearchType.FACETED.name()).isEqualTo("FACETED");
        }

        @Test
        @DisplayName("HYBRID should have correct name")
        void hybridShouldHaveCorrectName() {
            assertThat(SearchType.HYBRID.name()).isEqualTo("HYBRID");
        }
    }

    @Nested
    @DisplayName("Enum Ordinal Tests")
    class EnumOrdinalTests {

        @ParameterizedTest
        @EnumSource(SearchType.class)
        @DisplayName("All enum values should have valid ordinal")
        void allEnumValuesShouldHaveValidOrdinal(SearchType type) {
            assertThat(type.ordinal()).isGreaterThanOrEqualTo(0);
            assertThat(type.ordinal()).isLessThan(SearchType.values().length);
        }

        @Test
        @DisplayName("Enum ordinals should be sequential")
        void enumOrdinalsShouldBeSequential() {
            for (int i = 0; i < SearchType.values().length; i++) {
                assertThat(SearchType.values()[i].ordinal()).isEqualTo(i);
            }
        }
    }

    @Nested
    @DisplayName("valueOf Tests")
    class ValueOfTests {

        @ParameterizedTest
        @EnumSource(SearchType.class)
        @DisplayName("valueOf should return correct enum for each name")
        void valueOfShouldReturnCorrectEnum(SearchType type) {
            assertThat(SearchType.valueOf(type.name())).isEqualTo(type);
        }

        @Test
        @DisplayName("valueOf should throw exception for invalid name")
        void valueOfShouldThrowExceptionForInvalidName() {
            assertThatThrownBy(() -> SearchType.valueOf("INVALID_TYPE"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("valueOf should throw exception for null")
        void valueOfShouldThrowExceptionForNull() {
            assertThatThrownBy(() -> SearchType.valueOf(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Enum Consistency Tests")
    class EnumConsistencyTests {

        @Test
        @DisplayName("Enum values should be consistently ordered")
        void enumValuesShouldBeConsistentlyOrdered() {
            SearchType[] values1 = SearchType.values();
            SearchType[] values2 = SearchType.values();

            assertThat(values1).isEqualTo(values2);
        }

        @Test
        @DisplayName("Calling values() multiple times should return same array")
        void callingValuesMultipleTimesShouldReturnSameArray() {
            SearchType[] firstCall = SearchType.values();
            SearchType[] secondCall = SearchType.values();

            assertThat(firstCall).isNotSameAs(secondCall);
            assertThat(firstCall).containsExactlyInAnyOrder(secondCall);
        }
    }

    @Nested
    @DisplayName("Usage in Domain Model Tests")
    class UsageInDomainModelTests {

        @Test
        @DisplayName("Should be usable in search operations")
        void shouldBeUsableInSearchOperations() {
            SearchType type = SearchType.FULLTEXT;
            assertThat(type).isNotNull();
            assertThat(type.name()).isEqualTo("FULLTEXT");
        }
    }
}
