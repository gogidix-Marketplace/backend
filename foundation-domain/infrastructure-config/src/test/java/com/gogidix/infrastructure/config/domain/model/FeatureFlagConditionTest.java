package com.gogidix.infrastructure.config.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FeatureFlagCondition domain model.
 */
@DisplayName("FeatureFlagCondition Tests")
class FeatureFlagConditionTest {

    @Test
    @DisplayName("Should evaluate equals operator correctly")
    void shouldEvaluateEqualsOperator() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("role")
                .operator(FeatureFlagCondition.Operator.EQUALS)
                .value("admin")
                .build();

        assertTrue(condition.evaluate(Map.of("role", "admin")));
        assertFalse(condition.evaluate(Map.of("role", "user")));
    }

    @Test
    @DisplayName("Should evaluate not equals operator correctly")
    void shouldEvaluateNotEqualsOperator() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("role")
                .operator(FeatureFlagCondition.Operator.NOT_EQUALS)
                .value("admin")
                .build();

        assertFalse(condition.evaluate(Map.of("role", "admin")));
        assertTrue(condition.evaluate(Map.of("role", "user")));
    }

    @Test
    @DisplayName("Should evaluate contains operator correctly")
    void shouldEvaluateContainsOperator() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("email")
                .operator(FeatureFlagCondition.Operator.CONTAINS)
                .value("@example.com")
                .build();

        assertTrue(condition.evaluate(Map.of("email", "user@example.com")));
        assertFalse(condition.evaluate(Map.of("email", "user@other.com")));
    }

    @Test
    @DisplayName("Should evaluate starts with operator correctly")
    void shouldEvaluateStartsWithOperator() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("region")
                .operator(FeatureFlagCondition.Operator.STARTS_WITH)
                .value("us-")
                .build();

        assertTrue(condition.evaluate(Map.of("region", "us-west")));
        assertFalse(condition.evaluate(Map.of("region", "eu-west")));
    }

    @Test
    @DisplayName("Should evaluate ends with operator correctly")
    void shouldEvaluateEndsWithOperator() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("domain")
                .operator(FeatureFlagCondition.Operator.ENDS_WITH)
                .value(".com")
                .build();

        assertTrue(condition.evaluate(Map.of("domain", "example.com")));
        assertFalse(condition.evaluate(Map.of("domain", "example.org")));
    }

    @Test
    @DisplayName("Should evaluate in operator correctly")
    void shouldEvaluateInOperator() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("tier")
                .operator(FeatureFlagCondition.Operator.IN)
                .values(Set.of("free", "basic"))
                .build();

        assertTrue(condition.evaluate(Map.of("tier", "free")));
        assertTrue(condition.evaluate(Map.of("tier", "basic")));
        assertFalse(condition.evaluate(Map.of("tier", "premium")));
    }

    @Test
    @DisplayName("Should evaluate not in operator correctly")
    void shouldEvaluateNotInOperator() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("tier")
                .operator(FeatureFlagCondition.Operator.NOT_IN)
                .values(Set.of("free", "basic"))
                .build();

        assertFalse(condition.evaluate(Map.of("tier", "free")));
        assertFalse(condition.evaluate(Map.of("tier", "basic")));
        assertTrue(condition.evaluate(Map.of("tier", "premium")));
    }

    @Test
    @DisplayName("Should evaluate greater than operator correctly")
    void shouldEvaluateGreaterThanOperator() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("age")
                .operator(FeatureFlagCondition.Operator.GREATER_THAN)
                .value("18")
                .build();

        assertTrue(condition.evaluate(Map.of("age", "25")));
        assertFalse(condition.evaluate(Map.of("age", "15")));
    }

    @Test
    @DisplayName("Should evaluate less than operator correctly")
    void shouldEvaluateLessThanOperator() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("score")
                .operator(FeatureFlagCondition.Operator.LESS_THAN)
                .value("100")
                .build();

        assertTrue(condition.evaluate(Map.of("score", "50")));
        assertFalse(condition.evaluate(Map.of("score", "150")));
    }

    @Test
    @DisplayName("Should evaluate is null operator correctly")
    void shouldEvaluateIsNullOperator() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("optional_field")
                .operator(FeatureFlagCondition.Operator.IS_NULL)
                .build();

        assertTrue(condition.evaluate(Map.of()));
        assertFalse(condition.evaluate(Map.of("optional_field", "value")));
    }

    @Test
    @DisplayName("Should evaluate is not null operator correctly")
    void shouldEvaluateIsNotNullOperator() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("required_field")
                .operator(FeatureFlagCondition.Operator.IS_NOT_NULL)
                .build();

        assertFalse(condition.evaluate(Map.of()));
        assertTrue(condition.evaluate(Map.of("required_field", "value")));
    }

    @Test
    @DisplayName("Should evaluate composite conditions with AND logic")
    void shouldEvaluateCompositeConditionsWithAndLogic() {
        List<FeatureFlagCondition> nested = List.of(
                FeatureFlagCondition.builder()
                        .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                        .attribute("role")
                        .operator(FeatureFlagCondition.Operator.EQUALS)
                        .value("admin")
                        .build(),
                FeatureFlagCondition.builder()
                        .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                        .attribute("region")
                        .operator(FeatureFlagCondition.Operator.EQUALS)
                        .value("us")
                        .build()
        );

        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.COMPOSITE)
                .logicalOperator(FeatureFlagCondition.LogicalOperator.AND)
                .nestedConditions(nested)
                .build();

        assertTrue(condition.evaluate(Map.of("role", "admin", "region", "us")));
        assertFalse(condition.evaluate(Map.of("role", "admin", "region", "eu")));
    }

    @Test
    @DisplayName("Should evaluate composite conditions with OR logic")
    void shouldEvaluateCompositeConditionsWithOrLogic() {
        List<FeatureFlagCondition> nested = List.of(
                FeatureFlagCondition.builder()
                        .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                        .attribute("role")
                        .operator(FeatureFlagCondition.Operator.EQUALS)
                        .value("admin")
                        .build(),
                FeatureFlagCondition.builder()
                        .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                        .attribute("role")
                        .operator(FeatureFlagCondition.Operator.EQUALS)
                        .value("moderator")
                        .build()
        );

        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.COMPOSITE)
                .logicalOperator(FeatureFlagCondition.LogicalOperator.OR)
                .nestedConditions(nested)
                .build();

        assertTrue(condition.evaluate(Map.of("role", "admin")));
        assertTrue(condition.evaluate(Map.of("role", "moderator")));
        assertFalse(condition.evaluate(Map.of("role", "user")));
    }

    @Test
    @DisplayName("Should evaluate negated condition")
    void shouldEvaluateNegatedCondition() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("role")
                .operator(FeatureFlagCondition.Operator.EQUALS)
                .value("admin")
                .negate(true)
                .build();

        assertFalse(condition.evaluate(Map.of("role", "admin")));
        assertTrue(condition.evaluate(Map.of("role", "user")));
    }

    @Test
    @DisplayName("Should evaluate regex operator correctly")
    void shouldEvaluateRegexOperator() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("email")
                .operator(FeatureFlagCondition.Operator.REGEX)
                .value("^[A-Za-z0-9+_.-]+@(.+)$")
                .build();

        assertTrue(condition.evaluate(Map.of("email", "user@example.com")));
        assertFalse(condition.evaluate(Map.of("email", "invalid-email")));
    }

    @Test
    @DisplayName("Should evaluate between operator correctly")
    void shouldEvaluateBetweenOperator() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("age")
                .operator(FeatureFlagCondition.Operator.BETWEEN)
                .values(Set.of("18", "65"))
                .build();

        assertTrue(condition.evaluate(Map.of("age", "25")));
        assertTrue(condition.evaluate(Map.of("age", "18")));
        assertTrue(condition.evaluate(Map.of("age", "65")));
        assertFalse(condition.evaluate(Map.of("age", "15")));
        assertFalse(condition.evaluate(Map.of("age", "70")));
    }

    @Test
    @DisplayName("Should handle missing attribute gracefully")
    void shouldHandleMissingAttributeGracefully() {
        FeatureFlagCondition condition = FeatureFlagCondition.builder()
                .type(FeatureFlagCondition.ConditionType.ATTRIBUTE)
                .attribute("missing")
                .operator(FeatureFlagCondition.Operator.EQUALS)
                .value("value")
                .build();

        assertFalse(condition.evaluate(Map.of("other", "attribute")));
    }

    @Test
    @DisplayName("Should create user role condition preset")
    void shouldCreateUserRoleConditionPreset() {
        FeatureFlagCondition condition = FeatureFlagCondition.userRoleCondition("premium")
                .build();

        assertEquals(FeatureFlagCondition.ConditionType.ATTRIBUTE, condition.getType());
        assertEquals("role", condition.getAttribute());
        assertEquals(FeatureFlagCondition.Operator.EQUALS, condition.getOperator());
        assertEquals("premium", condition.getValue());
    }

    @Test
    @DisplayName("Should create user tier condition preset")
    void shouldCreateUserTierConditionPreset() {
        FeatureFlagCondition condition = FeatureFlagCondition.userTierCondition("gold")
                .build();

        assertEquals(FeatureFlagCondition.ConditionType.ATTRIBUTE, condition.getType());
        assertEquals("tier", condition.getAttribute());
        assertEquals(FeatureFlagCondition.Operator.EQUALS, condition.getOperator());
        assertEquals("gold", condition.getValue());
    }

    @Test
    @DisplayName("Should create country condition preset")
    void shouldCreateCountryConditionPreset() {
        FeatureFlagCondition condition = FeatureFlagCondition.countryCondition("US")
                .build();

        assertEquals(FeatureFlagCondition.ConditionType.LOCATION, condition.getType());
        assertEquals("country", condition.getAttribute());
        assertEquals(FeatureFlagCondition.Operator.EQUALS, condition.getOperator());
        assertEquals("US", condition.getValue());
    }
}
