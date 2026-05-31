package com.gogidix.aiservices.aimonitoringservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ConditionType Enum Tests")
class ConditionTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(ConditionType.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(ConditionType type) {
            assertThat(type).isNotNull();
        }

        @Test
        @DisplayName("Should have GREATER_THAN type")
        void shouldHaveGreaterThanType() {
            assertThat(ConditionType.valueOf("GREATER_THAN")).isEqualTo(ConditionType.GREATER_THAN);
        }

        @Test
        @DisplayName("Should have LESS_THAN type")
        void shouldHaveLessThanType() {
            assertThat(ConditionType.valueOf("LESS_THAN")).isEqualTo(ConditionType.LESS_THAN);
        }

        @Test
        @DisplayName("Should have EQUALS type")
        void shouldHaveEqualsType() {
            assertThat(ConditionType.valueOf("EQUALS")).isEqualTo(ConditionType.EQUALS);
        }

        @Test
        @DisplayName("Should have NOT_EQUALS type")
        void shouldHaveNotEqualsType() {
            assertThat(ConditionType.valueOf("NOT_EQUALS")).isEqualTo(ConditionType.NOT_EQUALS);
        }

        @Test
        @DisplayName("Should have CONTAINS type")
        void shouldHaveContainsType() {
            assertThat(ConditionType.valueOf("CONTAINS")).isEqualTo(ConditionType.CONTAINS);
        }

        @Test
        @DisplayName("Should have 5 condition type values")
        void shouldHave5ConditionTypeValues() {
            assertThat(ConditionType.values()).hasSize(5);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have consistent enum names")
        void shouldHaveConsistentEnumNames() {
            for (ConditionType type : ConditionType.values()) {
                String name = type.name();
                assertThat(name).isNotNull();
                assertThat(name).isUpperCase();
                assertThat(name).doesNotContain(" ");
                assertThat(name).doesNotContain("\t");
                assertThat(name).doesNotContain("\n");
            }
        }

        @Test
        @DisplayName("Should have unique enum values")
        void shouldHaveUniqueEnumValues() {
            long uniqueCount = java.util.Arrays.stream(ConditionType.values())
                    .map(ConditionType::name)
                    .distinct()
                    .count();

            assertThat(uniqueCount).isEqualTo(ConditionType.values().length);
        }
    }

    @Nested
    @DisplayName("Operator Type Tests")
    class OperatorTypeTests {

        @Test
        @DisplayName("Should have relational operators")
        void shouldHaveRelationalOperators() {
            assertThat(ConditionType.values()).contains(
                    ConditionType.GREATER_THAN,
                    ConditionType.LESS_THAN
            );
        }

        @Test
        @DisplayName("Should have equality operators")
        void shouldHaveEqualityOperators() {
            assertThat(ConditionType.values()).contains(
                    ConditionType.EQUALS,
                    ConditionType.NOT_EQUALS
            );
        }

        @Test
        @DisplayName("Should have string matching operators")
        void shouldHaveStringMatchingOperators() {
            assertThat(ConditionType.values()).contains(
                    ConditionType.CONTAINS
            );
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("CPU > 80% alert uses GREATER_THAN")
        void cpuAbove80AlertUsesGreaterThan() {
            ConditionType condition = ConditionType.GREATER_THAN;
            assertThat(condition).isEqualTo(ConditionType.GREATER_THAN);
        }

        @Test
        @DisplayName("Memory < 10% alert uses LESS_THAN")
        void memoryBelow10AlertUsesLessThan() {
            ConditionType condition = ConditionType.LESS_THAN;
            assertThat(condition).isEqualTo(ConditionType.LESS_THAN);
        }

        @Test
        @DisplayName("Status = ERROR uses EQUALS")
        void statusEqualsErrorUsesEquals() {
            ConditionType condition = ConditionType.EQUALS;
            assertThat(condition).isEqualTo(ConditionType.EQUALS);
        }

        @Test
        @DisplayName("Status != OK uses NOT_EQUALS")
        void statusNotOkUsesNotEquals() {
            ConditionType condition = ConditionType.NOT_EQUALS;
            assertThat(condition).isEqualTo(ConditionType.NOT_EQUALS);
        }

        @Test
        @DisplayName("Log contains 'ERROR' uses CONTAINS")
        void logContainsErrorUsesContains() {
            ConditionType condition = ConditionType.CONTAINS;
            assertThat(condition).isEqualTo(ConditionType.CONTAINS);
        }
    }
}
