package com.gogidix.aiservices.aiuserprofilingservice.domain.model;

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

@DisplayName("ProfileCriteria Value Object Tests")
class ProfileCriteriaTest {

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build criteria with required fields")
        void shouldBuildWithRequiredFields() {
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            assertThat(criteria.getType()).isEqualTo(ProfileCriteria.CriteriaType.CUSTOM);
            assertThat(criteria.getOperator()).isEqualTo(ProfileCriteria.CriteriaOperator.EQUALS);
            assertThat(criteria.getField()).isEqualTo("country");
            assertThat(criteria.getValue()).isEqualTo("USA");
        }

        @Test
        @DisplayName("Should build criteria with all fields")
        void shouldBuildWithAllFields() {
            ProfileCriteria nested = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.DEMOGRAPHIC)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("age")
                    .value(25)
                    .build();

            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("composite")
                    .value("root")
                    .logicalOperator(ProfileCriteria.LogicalOperator.OR)
                    .nestedCriteria(Arrays.asList(nested))
                    .build();

            assertThat(criteria.getLogicalOperator()).isEqualTo(ProfileCriteria.LogicalOperator.OR);
            assertThat(criteria.getNestedCriteria()).hasSize(1);
            assertThat(criteria.hasNestedCriteria()).isTrue();
        }

        @Test
        @DisplayName("Should default to AND logical operator")
        void shouldDefaultToAndOperator() {
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .logicalOperator(null)
                    .build();

            assertThat(criteria.getLogicalOperator()).isEqualTo(ProfileCriteria.LogicalOperator.AND);
        }

        @Test
        @DisplayName("Should default to empty nested criteria")
        void shouldDefaultToEmptyNestedCriteria() {
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
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
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
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
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            assertThat(criteria.getValue()).isEqualTo("USA");
        }

        @Test
        @DisplayName("Should accept Integer value")
        void shouldAcceptIntegerValue() {
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.GREATER_THAN)
                    .field("age")
                    .value(25)
                    .build();

            assertThat(criteria.getValue()).isEqualTo(25);
        }

        @Test
        @DisplayName("Should accept Double value")
        void shouldAcceptDoubleValue() {
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.GREATER_THAN)
                    .field("lifetimeValue")
                    .value(1000.50)
                    .build();

            assertThat(criteria.getValue()).isEqualTo(1000.50);
        }

        @Test
        @DisplayName("Should accept Boolean value")
        void shouldAcceptBooleanValue() {
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("isActive")
                    .value(Boolean.TRUE)
                    .build();

            assertThat(criteria.getValue()).isEqualTo(true);
        }

        @Test
        @DisplayName("Should accept List value")
        void shouldAcceptListValue() {
            List<String> values = Arrays.asList("USA", "Canada", "UK");
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.IN)
                    .field("country")
                    .value(values)
                    .build();

            assertThat(criteria.getValue()).isEqualTo(values);
        }

        @Test
        @DisplayName("Should accept null value")
        void shouldAcceptNullValue() {
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.IS_NULL)
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
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("name")
                    .value("John")
                    .build();

            assertThat(criteria.getValue()).isEqualTo("John");
        }

        @Test
        @DisplayName("Should provide value(Number) method")
        void shouldProvideNumberValueMethod() {
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.GREATER_THAN)
                    .field("age")
                    .value(30)
                    .build();

            assertThat(criteria.getValue()).isEqualTo(30);
        }

        @Test
        @DisplayName("Should provide value(Boolean) method")
        void shouldProvideBooleanValueMethod() {
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("verified")
                    .value(Boolean.TRUE)
                    .build();

            assertThat(criteria.getValue()).isEqualTo(true);
        }

        @Test
        @DisplayName("Should provide value(List) method")
        void shouldProvideListValueMethod() {
            List<String> regions = Arrays.asList("North", "South");
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.IN)
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
            ProfileCriteria criteria1 = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.DEMOGRAPHIC)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            ProfileCriteria criteria2 = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.BEHAVIOR)
                    .operator(ProfileCriteria.CriteriaOperator.GREATER_THAN)
                    .field("purchases")
                    .value(5)
                    .build();

            ProfileCriteria root = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
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
            ProfileCriteria original = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("root")
                    .value(Boolean.TRUE)
                    .build();

            ProfileCriteria nested = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.DEMOGRAPHIC)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            ProfileCriteria withNested = original.withNestedCriteria(nested);

            assertThat(withNested.getNestedCriteria()).hasSize(1);
            assertThat(original.getNestedCriteria()).isEmpty();
        }

        @Test
        @DisplayName("Should return unmodifiable nested criteria")
        void shouldReturnUnmodifiableNestedCriteria() {
            ProfileCriteria nested = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.DEMOGRAPHIC)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            ProfileCriteria root = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("root")
                    .value(Boolean.TRUE)
                    .nestedCriteria(Arrays.asList(nested))
                    .build();

            List<ProfileCriteria> unmodifiable = root.getNestedCriteria();

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
            ProfileCriteria criteria1 = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            ProfileCriteria criteria2 = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            assertThat(criteria1).isEqualTo(criteria2);
            assertThat(criteria1.hashCode()).isEqualTo(criteria2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when type differs")
        void shouldNotBeEqualWhenTypeDiffers() {
            ProfileCriteria criteria1 = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.DEMOGRAPHIC)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            ProfileCriteria criteria2 = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.BEHAVIOR)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            assertThat(criteria1).isNotEqualTo(criteria2);
        }

        @Test
        @DisplayName("Should not be equal when operator differs")
        void shouldNotBeEqualWhenOperatorDiffers() {
            ProfileCriteria criteria1 = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("age")
                    .value(25)
                    .build();

            ProfileCriteria criteria2 = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.GREATER_THAN)
                    .field("age")
                    .value(25)
                    .build();

            assertThat(criteria1).isNotEqualTo(criteria2);
        }

        @Test
        @DisplayName("Should not be equal when field differs")
        void shouldNotBeEqualWhenFieldDiffers() {
            ProfileCriteria criteria1 = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            ProfileCriteria criteria2 = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("city")
                    .value("USA")
                    .build();

            assertThat(criteria1).isNotEqualTo(criteria2);
        }

        @Test
        @DisplayName("Should not be equal when value differs")
        void shouldNotBeEqualWhenValueDiffers() {
            ProfileCriteria criteria1 = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
                    .field("country")
                    .value("USA")
                    .build();

            ProfileCriteria criteria2 = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.EQUALS)
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
        @EnumSource(ProfileCriteria.CriteriaType.class)
        @DisplayName("Should have all CriteriaType values")
        void shouldHaveAllCriteriaTypeValues(ProfileCriteria.CriteriaType type) {
            assertThat(type).isNotNull();
        }

        @ParameterizedTest
        @EnumSource(ProfileCriteria.CriteriaOperator.class)
        @DisplayName("Should have all CriteriaOperator values")
        void shouldHaveAllCriteriaOperatorValues(ProfileCriteria.CriteriaOperator operator) {
            assertThat(operator).isNotNull();
        }

        @ParameterizedTest
        @EnumSource(ProfileCriteria.LogicalOperator.class)
        @DisplayName("Should have all LogicalOperator values")
        void shouldHaveAllLogicalOperatorValues(ProfileCriteria.LogicalOperator logicalOperator) {
            assertThat(logicalOperator).isNotNull();
        }

        @Test
        @DisplayName("CriteriaType should have 4 values")
        void criteriaTypeShouldHave4Values() {
            assertThat(ProfileCriteria.CriteriaType.values()).hasSize(4);
        }

        @Test
        @DisplayName("CriteriaOperator should have 16 values")
        void criteriaOperatorShouldHave16Values() {
            assertThat(ProfileCriteria.CriteriaOperator.values()).hasSize(16);
        }

        @Test
        @DisplayName("LogicalOperator should have 2 values")
        void logicalOperatorShouldHave2Values() {
            assertThat(ProfileCriteria.LogicalOperator.values()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should include relevant fields in toString")
        void shouldContainRelevantFields() {
            ProfileCriteria criteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.GREATER_THAN)
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
