package com.gogidix.infrastructure.config.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.Set;

/**
 * Domain model for feature flag conditions.
 *
 * <p>Conditions enable complex feature flag evaluation based on:</p>
 * <ul>
 *   <li>User attributes (role, location, tier)</li>
 *   <li>Request attributes (ip address, user agent)</li>
 *   <li>Custom context values</li>
 *   <li>Date/time conditions</li>
 *   <li>Logical operators (AND, OR, NOT)</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "feature_flag_conditions")
public class FeatureFlagCondition {

    /**
     * Unique identifier for the condition.
     */
    private String id;

    /**
     * Type of condition.
     */
    @NotNull(message = "Condition type is required")
    private ConditionType type;

    /**
     * The attribute/property to evaluate in the context.
     */
    @NotBlank(message = "Attribute is required")
    private String attribute;

    /**
     * Operator for comparison.
     */
    @NotNull(message = "Operator is required")
    private Operator operator;

    /**
     * Expected value for comparison.
     */
    private String value;

    /**
     * Expected values for multi-value operators (IN, NOT_IN).
     */
    private Set<String> values;

    /**
     * Nested conditions for complex logic.
     */
    private java.util.List<FeatureFlagCondition> nestedConditions;

    /**
     * Logical operator for nested conditions (AND, OR).
     */
    @Builder.Default
    private LogicalOperator logicalOperator = LogicalOperator.AND;

    /**
     * Whether this condition should be negated.
     */
    @Builder.Default
    private boolean negate = false;

    /**
     * Condition type enumeration.
     */
    public enum ConditionType {
        /**
         * Simple attribute comparison.
         */
        ATTRIBUTE,

        /**
         * Date/time-based condition.
         */
        DATETIME,

        /**
         * Geographic location condition.
         */
        LOCATION,

        /**
         * User segment condition.
         */
        SEGMENT,

        /**
         * Complex/nested condition.
         */
        COMPOSITE,

        /**
         * Custom condition using external evaluator.
         */
        CUSTOM
    }

    /**
     * Operator enumeration.
     */
    public enum Operator {
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN,
        LESS_THAN_OR_EQUAL,
        CONTAINS,
        NOT_CONTAINS,
        STARTS_WITH,
        ENDS_WITH,
        IN,
        NOT_IN,
        BETWEEN,
        REGEX,
        IS_NULL,
        IS_NOT_NULL
    }

    /**
     * Logical operator for combining conditions.
     */
    public enum LogicalOperator {
        AND,
        OR
    }

    /**
     * Evaluates this condition against the provided context.
     *
     * @param context The evaluation context containing user attributes and other data
     * @return true if the condition is satisfied
     */
    public boolean evaluate(Map<String, Object> context) {
        if (context == null) {
            context = Map.of();
        }

        boolean result;

        if (type == ConditionType.COMPOSITE && nestedConditions != null) {
            result = evaluateCompositeCondition(context);
        } else {
            result = evaluateSimpleCondition(context);
        }

        return negate != result; // XOR with negate
    }

    /**
     * Evaluates a composite condition with nested conditions.
     */
    private boolean evaluateCompositeCondition(Map<String, Object> context) {
        if (nestedConditions == null || nestedConditions.isEmpty()) {
            return true;
        }

        return logicalOperator == LogicalOperator.AND
                ? nestedConditions.stream().allMatch(c -> c.evaluate(context))
                : nestedConditions.stream().anyMatch(c -> c.evaluate(context));
    }

    /**
     * Evaluates a simple condition against the context.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean evaluateSimpleCondition(Map<String, Object> context) {
        Object actualValue = context.get(attribute);

        return switch (operator) {
            case IS_NULL -> actualValue == null;
            case IS_NOT_NULL -> actualValue != null;
            case EQUALS -> compareValues(actualValue, value, true);
            case NOT_EQUALS -> !compareValues(actualValue, value, true);
            case CONTAINS -> containsValue(actualValue, value);
            case NOT_CONTAINS -> !containsValue(actualValue, value);
            case STARTS_WITH -> startsWithValue(actualValue, value);
            case ENDS_WITH -> endsWithValue(actualValue, value);
            case IN -> values != null && values.contains(actualValue != null ? actualValue.toString() : null);
            case NOT_IN -> values == null || !values.contains(actualValue != null ? actualValue.toString() : null);
            case GREATER_THAN -> compareNumeric(actualValue, value) > 0;
            case GREATER_THAN_OR_EQUAL -> compareNumeric(actualValue, value) >= 0;
            case LESS_THAN -> compareNumeric(actualValue, value) < 0;
            case LESS_THAN_OR_EQUAL -> compareNumeric(actualValue, value) <= 0;
            case BETWEEN -> evaluateBetween(context);
            case REGEX -> evaluateRegex(actualValue, value);
        };
    }

    /**
     * Compares two values for equality.
     */
    private boolean compareValues(Object actual, String expected, boolean strict) {
        if (actual == null) {
            return expected == null;
        }
        return actual.toString().equals(expected);
    }

    /**
     * Checks if the actual value contains the expected value.
     */
    private boolean containsValue(Object actual, String expected) {
        if (actual == null || expected == null) {
            return false;
        }
        return actual.toString().contains(expected);
    }

    /**
     * Checks if the actual value starts with the expected value.
     */
    private boolean startsWithValue(Object actual, String expected) {
        if (actual == null || expected == null) {
            return false;
        }
        return actual.toString().startsWith(expected);
    }

    /**
     * Checks if the actual value ends with the expected value.
     */
    private boolean endsWithValue(Object actual, String expected) {
        if (actual == null || expected == null) {
            return false;
        }
        return actual.toString().endsWith(expected);
    }

    /**
     * Compares two numeric values.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private int compareNumeric(Object actual, String expected) {
        if (actual == null || expected == null) {
            return 0;
        }
        try {
            double actualNum = actual instanceof Number ? ((Number) actual).doubleValue()
                    : Double.parseDouble(actual.toString());
            double expectedNum = Double.parseDouble(expected);
            return Double.compare(actualNum, expectedNum);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Evaluates a BETWEEN condition.
     */
    private boolean evaluateBetween(Map<String, Object> context) {
        if (values == null || values.size() != 2) {
            return false;
        }
        String[] bounds = values.stream()
                .sorted()
                .toArray(String[]::new);
        return compareNumeric(context.get(attribute), bounds[0]) >= 0
                && compareNumeric(context.get(attribute), bounds[1]) <= 0;
    }

    /**
     * Evaluates a REGEX condition.
     */
    private boolean evaluateRegex(Object actual, String pattern) {
        if (actual == null || pattern == null) {
            return false;
        }
        try {
            return actual.toString().matches(pattern);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Creates a builder preset for user role conditions.
     */
    public static FeatureFlagConditionBuilder userRoleCondition(String role) {
        return FeatureFlagCondition.builder()
                .type(ConditionType.ATTRIBUTE)
                .attribute("role")
                .operator(Operator.EQUALS)
                .value(role);
    }

    /**
     * Creates a builder preset for user tier conditions.
     */
    public static FeatureFlagConditionBuilder userTierCondition(String tier) {
        return FeatureFlagCondition.builder()
                .type(ConditionType.ATTRIBUTE)
                .attribute("tier")
                .operator(Operator.EQUALS)
                .value(tier);
    }

    /**
     * Creates a builder preset for geographic conditions.
     */
    public static FeatureFlagConditionBuilder countryCondition(String country) {
        return FeatureFlagCondition.builder()
                .type(ConditionType.LOCATION)
                .attribute("country")
                .operator(Operator.EQUALS)
                .value(country);
    }
}
