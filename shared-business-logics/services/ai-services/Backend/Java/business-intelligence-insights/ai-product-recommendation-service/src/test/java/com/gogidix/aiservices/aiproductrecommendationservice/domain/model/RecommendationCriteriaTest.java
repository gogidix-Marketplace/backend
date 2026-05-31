package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("RecommendationCriteria Value Object Tests")
class RecommendationCriteriaTest {

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build criteria with required fields")
        void shouldBuildWithRequiredFields() {
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            assertThat(criteria.getType()).isEqualTo(RecommendationCriteria.CriteriaType.CUSTOM);
            assertThat(criteria.getOperator()).isEqualTo(RecommendationCriteria.CriteriaOperator.EQUALS);
            assertThat(criteria.getField()).isEqualTo("country");
            assertThat(criteria.getValue()).isEqualTo("USA");
        }

        @Test
        @DisplayName("Should build criteria with all fields")
        void shouldBuildWithAllFields() {
            RecommendationCriteria nested = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.DEMOGRAPHIC)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("age")
                    .value(25)
                    .build();

            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("composite")
                    .value("root")
                    .logicalOperator(RecommendationCriteria.LogicalOperator.OR)
                    .nestedCriteria(Arrays.asList(nested))
                    .build();

            assertThat(criteria.getLogicalOperator()).isEqualTo(RecommendationCriteria.LogicalOperator.OR);
            assertThat(criteria.getNestedCriteria()).hasSize(1);
            assertThat(criteria.hasNestedCriteria()).isTrue();
        }

        @Test
        @DisplayName("Should default to AND logical operator")
        void shouldDefaultToAndOperator() {
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .logicalOperator(null)
                    .build();

            assertThat(criteria.getLogicalOperator()).isEqualTo(RecommendationCriteria.LogicalOperator.AND);
        }

        @Test
        @DisplayName("Should default to empty nested criteria")
        void shouldDefaultToEmptyNestedCriteria() {
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .nestedCriteria(null)
                    .build();

            assertThat(criteria.getNestedCriteria()).isEmpty();
            assertThat(criteria.hasNestedCriteria()).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"field1", "field2", "customerField", "total_spent"})
        @DisplayName("Should accept various field names")
        void shouldAcceptVariousFieldNames(String fieldName) {
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field(fieldName)
                    .value("test")
                    .build();

            assertThat(criteria.getField()).isEqualTo(fieldName);
        }
    }

    @Nested
    @DisplayName("Value Type Tests")
    class ValueTypeTests {

        @Test
        @DisplayName("Should accept String value")
        void shouldAcceptStringValue() {
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            assertThat(criteria.getValue()).isEqualTo("USA");
        }

        @Test
        @DisplayName("Should accept Integer value")
        void shouldAcceptIntegerValue() {
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.GREATER_THAN)
                    .field("age")
                    .value(25)
                    .build();

            assertThat(criteria.getValue()).isEqualTo(25);
        }

        @Test
        @DisplayName("Should accept Double value")
        void shouldAcceptDoubleValue() {
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.GREATER_THAN)
                    .field("lifetimeValue")
                    .value(1000.50)
                    .build();

            assertThat(criteria.getValue()).isEqualTo(1000.50);
        }

        @Test
        @DisplayName("Should accept Boolean value")
        void shouldAcceptBooleanValue() {
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("isActive")
                    .value(Boolean.TRUE)
                    .build();

            assertThat(criteria.getValue()).isEqualTo(true);
        }

        @Test
        @DisplayName("Should accept List value")
        void shouldAcceptListValue() {
            List<String> values = Arrays.asList("USA", "Canada", "UK");
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.IN)
                    .field("country")
                    .value(values)
                    .build();

            assertThat(criteria.getValue()).isEqualTo(values);
        }

        @Test
        @DisplayName("Should accept null value")
        void shouldAcceptNullValue() {
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.IS_NULL)
                    .field("deletedAt")
                    .value((Object) null)
                    .build();

            assertThat(criteria.getValue()).isNull();
        }
    }

    @Nested
    @DisplayName("Builder Method Tests")
    class BuilderMethodTests {

        @Test
        @DisplayName("Should provide value(String) method")
        void shouldProvideStringValueMethod() {
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("name")
                    .value("John")
                    .build();

            assertThat(criteria.getValue()).isEqualTo("John");
        }

        @Test
        @DisplayName("Should provide value(Number) method")
        void shouldProvideNumberValueMethod() {
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.GREATER_THAN)
                    .field("age")
                    .value(30)
                    .build();

            assertThat(criteria.getValue()).isEqualTo(30);
        }

        @Test
        @DisplayName("Should provide value(Boolean) method")
        void shouldProvideBooleanValueMethod() {
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("verified")
                    .value(Boolean.TRUE)
                    .build();

            assertThat(criteria.getValue()).isEqualTo(true);
        }

        @Test
        @DisplayName("Should provide value(List) method")
        void shouldProvideListValueMethod() {
            List<String> regions = Arrays.asList("North", "South");
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.IN)
                    .field("region")
                    .value(regions)
                    .build();

            assertThat(criteria.getValue()).isEqualTo(regions);
        }
    }

    @Nested
    @DisplayName("Nested Criteria Tests")
    class NestedCriteriaTests {

        @Test
        @DisplayName("Should add nested criteria via addNestedCriteria")
        void shouldAddNestedCriteria() {
            RecommendationCriteria criteria1 = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.DEMOGRAPHIC)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            RecommendationCriteria criteria2 = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.BEHAVIOR)
                    .operator(RecommendationCriteria.CriteriaOperator.GREATER_THAN)
                    .field("purchases")
                    .value(5)
                    .build();

            RecommendationCriteria root = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("root")
                    .value(Boolean.TRUE)
                    .addNestedCriteria(criteria1)
                    .addNestedCriteria(criteria2)
                    .build();

            assertThat(root.getNestedCriteria()).hasSize(2);
            assertThat(root.hasNestedCriteria()).isTrue();
        }

        @Test
        @DisplayName("Should create new criteria with withNestedCriteria")
        void shouldCreateWithWithNestedCriteria() {
            RecommendationCriteria original = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("root")
                    .value(Boolean.TRUE)
                    .build();

            RecommendationCriteria nested = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.DEMOGRAPHIC)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            RecommendationCriteria withNested = original.withNestedCriteria(nested);

            assertThat(withNested.getNestedCriteria()).hasSize(1);
            assertThat(original.getNestedCriteria()).isEmpty();
        }

        @Test
        @DisplayName("Should return unmodifiable nested criteria")
        void shouldReturnUnmodifiableNestedCriteria() {
            RecommendationCriteria nested = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.DEMOGRAPHIC)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            RecommendationCriteria root = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("root")
                    .value(Boolean.TRUE)
                    .nestedCriteria(Arrays.asList(nested))
                    .build();

            List<RecommendationCriteria> unmodifiable = root.getNestedCriteria();

            assertThatThrownBy(() -> unmodifiable.add(nested))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            RecommendationCriteria criteria1 = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            RecommendationCriteria criteria2 = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            assertThat(criteria1).isEqualTo(criteria2);
            assertThat(criteria1.hashCode()).isEqualTo(criteria2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when type differs")
        void shouldNotBeEqualWhenTypeDiffers() {
            RecommendationCriteria criteria1 = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.DEMOGRAPHIC)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            RecommendationCriteria criteria2 = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.BEHAVIOR)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            assertThat(criteria1).isNotEqualTo(criteria2);
        }

        @Test
        @DisplayName("Should not be equal when operator differs")
        void shouldNotBeEqualWhenOperatorDiffers() {
            RecommendationCriteria criteria1 = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("age")
                    .value(25)
                    .build();

            RecommendationCriteria criteria2 = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.GREATER_THAN)
                    .field("age")
                    .value(25)
                    .build();

            assertThat(criteria1).isNotEqualTo(criteria2);
        }

        @Test
        @DisplayName("Should not be equal when field differs")
        void shouldNotBeEqualWhenFieldDiffers() {
            RecommendationCriteria criteria1 = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            RecommendationCriteria criteria2 = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("city")
                    .value("USA")
                    .build();

            assertThat(criteria1).isNotEqualTo(criteria2);
        }

        @Test
        @DisplayName("Should not be equal when value differs")
        void shouldNotBeEqualWhenValueDiffers() {
            RecommendationCriteria criteria1 = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            RecommendationCriteria criteria2 = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("Canada")
                    .build();

            assertThat(criteria1).isNotEqualTo(criteria2);
        }
    }

    @Nested
    @DisplayName("Enum Tests")
    class EnumTests {

        @ParameterizedTest
        @EnumSource(RecommendationCriteria.CriteriaType.class)
        @DisplayName("Should have all CriteriaType values")
        void shouldHaveAllCriteriaTypeValues(RecommendationCriteria.CriteriaType type) {
            assertThat(type).isNotNull();
        }

        @ParameterizedTest
        @EnumSource(RecommendationCriteria.CriteriaOperator.class)
        @DisplayName("Should have all CriteriaOperator values")
        void shouldHaveAllCriteriaOperatorValues(RecommendationCriteria.CriteriaOperator operator) {
            assertThat(operator).isNotNull();
        }

        @ParameterizedTest
        @EnumSource(RecommendationCriteria.LogicalOperator.class)
        @DisplayName("Should have all LogicalOperator values")
        void shouldHaveAllLogicalOperatorValues(RecommendationCriteria.LogicalOperator logicalOperator) {
            assertThat(logicalOperator).isNotNull();
        }

        @Test
        @DisplayName("CriteriaType should have 4 values")
        void criteriaTypeShouldHave4Values() {
            assertThat(RecommendationCriteria.CriteriaType.values()).hasSize(4);
        }

        @Test
        @DisplayName("CriteriaOperator should have 16 values")
        void criteriaOperatorShouldHave16Values() {
            assertThat(RecommendationCriteria.CriteriaOperator.values()).hasSize(16);
        }

        @Test
        @DisplayName("LogicalOperator should have 2 values")
        void logicalOperatorShouldHave2Values() {
            assertThat(RecommendationCriteria.LogicalOperator.values()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should include relevant fields in toString")
        void shouldContainRelevantFields() {
            RecommendationCriteria criteria = RecommendationCriteria.builder()
                    .type(RecommendationCriteria.CriteriaType.CUSTOM)
                    .operator(RecommendationCriteria.CriteriaOperator.GREATER_THAN)
                    .field("lifetimeValue")
                    .value(1000)
                    .build();

            String str = criteria.toString();

            assertThat(str).contains("CUSTOM");
            assertThat(str).contains("GREATER_THAN");
            assertThat(str).contains("lifetimeValue");
            assertThat(str).contains("1000");
        }
    }
}
