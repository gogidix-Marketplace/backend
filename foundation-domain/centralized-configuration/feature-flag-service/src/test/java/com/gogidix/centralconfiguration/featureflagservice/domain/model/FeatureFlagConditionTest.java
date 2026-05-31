package com.gogidix.centralconfiguration.featureflagservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FeatureFlagCondition Domain Entity Tests")
class FeatureFlagConditionTest {

    private static final Long ID = 1L;
    private static final Long FEATURE_FLAG_ID = 100L;
    private static final String ATTRIBUTE_NAME = "country";
    private static final String OPERATOR = "eq";
    private static final String ATTRIBUTE_VALUE = "US";

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            LocalDateTime now = LocalDateTime.now();
            FeatureFlag flag = FeatureFlag.builder().id(FEATURE_FLAG_ID).build();

            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .id(ID)
                    .featureFlagId(FEATURE_FLAG_ID)
                    .featureFlag(flag)
                    .attributeName(ATTRIBUTE_NAME)
                    .operator(OPERATOR)
                    .attributeValue(ATTRIBUTE_VALUE)
                    .priority(5)
                    .createdAt(now)
                    .build();

            assertThat(condition.getId()).isEqualTo(ID);
            assertThat(condition.getFeatureFlagId()).isEqualTo(FEATURE_FLAG_ID);
            assertThat(condition.getFeatureFlag()).isNotNull();
            assertThat(condition.getFeatureFlag().getId()).isEqualTo(FEATURE_FLAG_ID);
            assertThat(condition.getAttributeName()).isEqualTo(ATTRIBUTE_NAME);
            assertThat(condition.getOperator()).isEqualTo(OPERATOR);
            assertThat(condition.getAttributeValue()).isEqualTo(ATTRIBUTE_VALUE);
            assertThat(condition.getPriority()).isEqualTo(5);
            assertThat(condition.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should build with required fields only")
        void shouldBuildWithRequiredFieldsOnly() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .featureFlagId(FEATURE_FLAG_ID)
                    .attributeName(ATTRIBUTE_NAME)
                    .operator(OPERATOR)
                    .build();

            assertThat(condition.getFeatureFlagId()).isEqualTo(FEATURE_FLAG_ID);
            assertThat(condition.getAttributeName()).isEqualTo(ATTRIBUTE_NAME);
            assertThat(condition.getOperator()).isEqualTo(OPERATOR);
        }

        @Test
        @DisplayName("Should set default priority to 0")
        void shouldSetDefaultPriorityTo0() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .featureFlagId(FEATURE_FLAG_ID)
                    .attributeName(ATTRIBUTE_NAME)
                    .operator(OPERATOR)
                    .build();

            assertThat(condition.getPriority()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Attribute Name Tests")
    class AttributeNameTests {

        @ParameterizedTest
        @ValueSource(strings = {"country", "role", "tier", "age", "subscriptionType", "region"})
        @DisplayName("Should accept various attribute names")
        void shouldAcceptVariousAttributeNames(String attributeName) {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeName(attributeName)
                    .build();

            assertThat(condition.getAttributeName()).isEqualTo(attributeName);
        }

        @Test
        @DisplayName("Should set and get attribute name")
        void shouldSetAndGetAttributeName() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeName("subscriptionType")
                    .build();

            assertThat(condition.getAttributeName()).isEqualTo("subscriptionType");
        }
    }

    @Nested
    @DisplayName("Operator Tests")
    class OperatorTests {

        @ParameterizedTest
        @ValueSource(strings = {"eq", "neq", "contains", "gt", "lt", "gte", "lte", "in", "not_in"})
        @DisplayName("Should accept various operators")
        void shouldAcceptVariousOperators(String operator) {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .operator(operator)
                    .build();

            assertThat(condition.getOperator()).isEqualTo(operator);
        }

        @Test
        @DisplayName("Should set equality operator")
        void shouldSetEqualityOperator() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .operator("eq")
                    .build();

            assertThat(condition.getOperator()).isEqualTo("eq");
        }

        @Test
        @DisplayName("Should set contains operator")
        void shouldSetContainsOperator() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .operator("contains")
                    .build();

            assertThat(condition.getOperator()).isEqualTo("contains");
        }
    }

    @Nested
    @DisplayName("Attribute Value Tests")
    class AttributeValueTests {

        @Test
        @DisplayName("Should set string attribute value")
        void shouldSetStringAttributeValue() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeValue("US")
                    .build();

            assertThat(condition.getAttributeValue()).isEqualTo("US");
        }

        @Test
        @DisplayName("Should accept numeric attribute value")
        void shouldAcceptNumericAttributeValue() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeValue("25")
                    .build();

            assertThat(condition.getAttributeValue()).isEqualTo("25");
        }

        @Test
        @DisplayName("Should accept null attribute value")
        void shouldAcceptNullAttributeValue() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeValue(null)
                    .build();

            assertThat(condition.getAttributeValue()).isNull();
        }

        @Test
        @DisplayName("Should accept empty attribute value")
        void shouldAcceptEmptyAttributeValue() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeValue("")
                    .build();

            assertThat(condition.getAttributeValue()).isEmpty();
        }

        @Test
        @DisplayName("Should accept comma separated values")
        void shouldAcceptCommaSeparatedValues() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeValue("gold,platinum,diamond")
                    .build();

            assertThat(condition.getAttributeValue()).isEqualTo("gold,platinum,diamond");
        }
    }

    @Nested
    @DisplayName("Priority Tests")
    class PriorityTests {

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 5, 10, 100})
        @DisplayName("Should accept various priorities")
        void shouldAcceptVariousPriorities(int priority) {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .priority(priority)
                    .build();

            assertThat(condition.getPriority()).isEqualTo(priority);
        }

        @Test
        @DisplayName("Should default priority to 0")
        void shouldDefaultPriorityTo0() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .build();

            assertThat(condition.getPriority()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should set high priority")
        void shouldSetHighPriority() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .priority(100)
                    .build();

            assertThat(condition.getPriority()).isEqualTo(100);
        }

        @Test
        @DisplayName("Should set low priority")
        void shouldSetLowPriority() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .priority(1)
                    .build();

            assertThat(condition.getPriority()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Feature Flag Relationship Tests")
    class FeatureFlagRelationshipTests {

        @Test
        @DisplayName("Should set feature flag ID")
        void shouldSetFeatureFlagId() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .featureFlagId(500L)
                    .build();

            assertThat(condition.getFeatureFlagId()).isEqualTo(500L);
        }

        @Test
        @DisplayName("Should associate with feature flag")
        void shouldAssociateWithFeatureFlag() {
            FeatureFlag flag = FeatureFlag.builder()
                    .id(200L)
                    .flagKey("test-flag")
                    .build();

            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .featureFlagId(200L)
                    .featureFlag(flag)
                    .build();

            assertThat(condition.getFeatureFlag()).isNotNull();
            assertThat(condition.getFeatureFlag().getId()).isEqualTo(200L);
            assertThat(condition.getFeatureFlag().getFlagKey()).isEqualTo("test-flag");
        }

        @Test
        @DisplayName("Should accept null feature flag")
        void shouldAcceptNullFeatureFlag() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .featureFlag(null)
                    .build();

            assertThat(condition.getFeatureFlag()).isNull();
        }
    }

    @Nested
    @DisplayName("ID Tests")
    class IdTests {

        @Test
        @DisplayName("Should set and get id")
        void shouldSetAndGetId() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .id(123L)
                    .build();

            assertThat(condition.getId()).isEqualTo(123L);
        }

        @ParameterizedTest
        @ValueSource(longs = {1L, 100L, 1000L, Long.MAX_VALUE})
        @DisplayName("Should accept various id values")
        void shouldAcceptVariousIdValues(Long id) {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .id(id)
                    .build();

            assertThat(condition.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should accept null id before persistence")
        void shouldAcceptNullIdBeforePersistence() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .build();

            assertThat(condition.getId()).isNull();
        }
    }

    @Nested
    @DisplayName("Timestamp Tests")
    class TimestampTests {

        @Test
        @DisplayName("Should set created at")
        void shouldSetCreatedAt() {
            LocalDateTime now = LocalDateTime.now();
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .createdAt(now)
                    .build();

            assertThat(condition.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should accept null created at")
        void shouldAcceptNullCreatedAt() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .createdAt(null)
                    .build();

            assertThat(condition.getCreatedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("Lombok Data Tests")
    class LombokDataTests {

        @Test
        @DisplayName("Should generate equals")
        void shouldGenerateEquals() {
            FeatureFlagCondition condition1 = FeatureFlagCondition.builder()
                    .featureFlagId(FEATURE_FLAG_ID)
                    .attributeName(ATTRIBUTE_NAME)
                    .operator(OPERATOR)
                    .attributeValue(ATTRIBUTE_VALUE)
                    .build();

            FeatureFlagCondition condition2 = FeatureFlagCondition.builder()
                    .featureFlagId(FEATURE_FLAG_ID)
                    .attributeName(ATTRIBUTE_NAME)
                    .operator(OPERATOR)
                    .attributeValue(ATTRIBUTE_VALUE)
                    .build();

            assertThat(condition1).isEqualTo(condition2);
        }

        @Test
        @DisplayName("Should generate hashCode")
        void shouldGenerateHashCode() {
            FeatureFlagCondition condition1 = FeatureFlagCondition.builder()
                    .attributeName(ATTRIBUTE_NAME)
                    .operator(OPERATOR)
                    .build();

            FeatureFlagCondition condition2 = FeatureFlagCondition.builder()
                    .attributeName(ATTRIBUTE_NAME)
                    .operator(OPERATOR)
                    .build();

            assertThat(condition1.hashCode()).isEqualTo(condition2.hashCode());
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeName(ATTRIBUTE_NAME)
                    .operator(OPERATOR)
                    .build();

            String toString = condition.toString();

            assertThat(toString).contains(ATTRIBUTE_NAME);
            assertThat(toString).contains(OPERATOR);
        }

        @Test
        @DisplayName("Should generate setters")
        void shouldGenerateSetters() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeName("old-attr")
                    .operator("eq")
                    .priority(1)
                    .build();

            condition.setAttributeName("new-attr");
            condition.setOperator("neq");
            condition.setPriority(10);
            condition.setAttributeValue("new-value");

            assertThat(condition.getAttributeName()).isEqualTo("new-attr");
            assertThat(condition.getOperator()).isEqualTo("neq");
            assertThat(condition.getPriority()).isEqualTo(10);
            assertThat(condition.getAttributeValue()).isEqualTo("new-value");
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("Should represent country condition")
        void shouldRepresentCountryCondition() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeName("country")
                    .operator("eq")
                    .attributeValue("US")
                    .priority(1)
                    .build();

            assertThat(condition.getAttributeName()).isEqualTo("country");
            assertThat(condition.getOperator()).isEqualTo("eq");
            assertThat(condition.getAttributeValue()).isEqualTo("US");
        }

        @Test
        @DisplayName("Should represent age condition")
        void shouldRepresentAgeCondition() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeName("age")
                    .operator("gte")
                    .attributeValue("18")
                    .build();

            assertThat(condition.getAttributeName()).isEqualTo("age");
            assertThat(condition.getOperator()).isEqualTo("gte");
            assertThat(condition.getAttributeValue()).isEqualTo("18");
        }

        @Test
        @DisplayName("Should represent role condition")
        void shouldRepresentRoleCondition() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeName("role")
                    .operator("in")
                    .attributeValue("admin,moderator")
                    .build();

            assertThat(condition.getAttributeName()).isEqualTo("role");
            assertThat(condition.getOperator()).isEqualTo("in");
            assertThat(condition.getAttributeValue()).isEqualTo("admin,moderator");
        }

        @Test
        @DisplayName("Should represent subscription tier condition")
        void shouldRepresentSubscriptionTierCondition() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeName("tier")
                    .operator("eq")
                    .attributeValue("premium")
                    .priority(5)
                    .build();

            assertThat(condition.getAttributeName()).isEqualTo("tier");
            assertThat(condition.getOperator()).isEqualTo("eq");
            assertThat(condition.getAttributeValue()).isEqualTo("premium");
            assertThat(condition.getPriority()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should represent region contains condition")
        void shouldRepresentRegionContainsCondition() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeName("region")
                    .operator("contains")
                    .attributeValue("Europe")
                    .build();

            assertThat(condition.getAttributeName()).isEqualTo("region");
            assertThat(condition.getOperator()).isEqualTo("contains");
            assertThat(condition.getAttributeValue()).isEqualTo("Europe");
        }

        @Test
        @DisplayName("Should represent multiple conditions for same flag")
        void shouldRepresentMultipleConditionsForSameFlag() {
            FeatureFlagCondition condition1 = FeatureFlagCondition.builder()
                    .featureFlagId(100L)
                    .attributeName("country")
                    .operator("eq")
                    .attributeValue("US")
                    .priority(1)
                    .build();

            FeatureFlagCondition condition2 = FeatureFlagCondition.builder()
                    .featureFlagId(100L)
                    .attributeName("tier")
                    .operator("eq")
                    .attributeValue("premium")
                    .priority(2)
                    .build();

            assertThat(condition1.getFeatureFlagId()).isEqualTo(condition2.getFeatureFlagId());
            assertThat(condition1.getPriority()).isLessThan(condition2.getPriority());
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should get id")
        void shouldGetId() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .id(ID)
                    .build();

            assertThat(condition.getId()).isEqualTo(ID);
        }

        @Test
        @DisplayName("Should get feature flag id")
        void shouldGetFeatureFlagId() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .featureFlagId(FEATURE_FLAG_ID)
                    .build();

            assertThat(condition.getFeatureFlagId()).isEqualTo(FEATURE_FLAG_ID);
        }

        @Test
        @DisplayName("Should get feature flag")
        void shouldGetFeatureFlag() {
            FeatureFlag flag = FeatureFlag.builder().id(FEATURE_FLAG_ID).build();
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .featureFlag(flag)
                    .build();

            assertThat(condition.getFeatureFlag()).isEqualTo(flag);
        }

        @Test
        @DisplayName("Should get attribute name")
        void shouldGetAttributeName() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeName(ATTRIBUTE_NAME)
                    .build();

            assertThat(condition.getAttributeName()).isEqualTo(ATTRIBUTE_NAME);
        }

        @Test
        @DisplayName("Should get operator")
        void shouldGetOperator() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .operator(OPERATOR)
                    .build();

            assertThat(condition.getOperator()).isEqualTo(OPERATOR);
        }

        @Test
        @DisplayName("Should get attribute value")
        void shouldGetAttributeValue() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .attributeValue(ATTRIBUTE_VALUE)
                    .build();

            assertThat(condition.getAttributeValue()).isEqualTo(ATTRIBUTE_VALUE);
        }

        @Test
        @DisplayName("Should get priority")
        void shouldGetPriority() {
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .priority(10)
                    .build();

            assertThat(condition.getPriority()).isEqualTo(10);
        }

        @Test
        @DisplayName("Should get created at")
        void shouldGetCreatedAt() {
            LocalDateTime now = LocalDateTime.now();
            FeatureFlagCondition condition = FeatureFlagCondition.builder()
                    .createdAt(now)
                    .build();

            assertThat(condition.getCreatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create with no args constructor")
        void shouldCreateWithNoArgsConstructor() {
            FeatureFlagCondition condition = new FeatureFlagCondition();

            assertThat(condition).isNotNull();
            assertThat(condition.getPriority()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should create with all args constructor")
        void shouldCreateWithAllArgsConstructor() {
            LocalDateTime now = LocalDateTime.now();
            FeatureFlag flag = FeatureFlag.builder().id(FEATURE_FLAG_ID).build();

            FeatureFlagCondition condition = new FeatureFlagCondition();
            condition.setId(ID);
            condition.setFeatureFlagId(FEATURE_FLAG_ID);
            condition.setFeatureFlag(flag);
            condition.setAttributeName(ATTRIBUTE_NAME);
            condition.setOperator(OPERATOR);
            condition.setAttributeValue(ATTRIBUTE_VALUE);
            condition.setPriority(5);
            condition.setCreatedAt(now);

            assertThat(condition.getId()).isEqualTo(ID);
            assertThat(condition.getFeatureFlagId()).isEqualTo(FEATURE_FLAG_ID);
            assertThat(condition.getFeatureFlag()).isEqualTo(flag);
            assertThat(condition.getAttributeName()).isEqualTo(ATTRIBUTE_NAME);
            assertThat(condition.getOperator()).isEqualTo(OPERATOR);
            assertThat(condition.getAttributeValue()).isEqualTo(ATTRIBUTE_VALUE);
            assertThat(condition.getPriority()).isEqualTo(5);
            assertThat(condition.getCreatedAt()).isEqualTo(now);
        }
    }
}
