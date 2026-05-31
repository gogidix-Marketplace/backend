package com.gogidix.shared.testing.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Domain entity representing a test assertion.
 * Defines what should be verified during or after test execution.
 */
@Data
@With
@Builder
public class TestAssertion {
    
    private final String name;
    private final String description;
    private final AssertionType assertionType;
    private final String target; // What to assert on (field, response, etc.)
    private final AssertionOperator operator;
    private final Object expectedValue;
    private final Object actualValue; // Set during execution
    private final boolean optional;
    private final String failureMessage;
    private final int timeoutSeconds;
    private final Map<String, Object> parameters;
    private final AssertionResult result; // Set after execution
    
    /**
     * Validates if this assertion is properly configured.
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
               assertionType != null &&
               target != null && !target.trim().isEmpty() &&
               operator != null &&
               hasRequiredParameters();
    }
    
    /**
     * Checks if this assertion has all required parameters.
     */
    private boolean hasRequiredParameters() {
        return switch (assertionType) {
            case EQUALS, NOT_EQUALS, GREATER_THAN, LESS_THAN, CONTAINS, NOT_CONTAINS -> expectedValue != null;
            case NULL_CHECK, NOT_NULL_CHECK, EMPTY_CHECK, NOT_EMPTY_CHECK -> true; // No expected value needed
            case REGEX_MATCH -> expectedValue instanceof String;
            case JSON_PATH -> parameters != null && parameters.containsKey("jsonPath");
            case XML_PATH -> parameters != null && parameters.containsKey("xmlPath");
            case HTTP_STATUS -> expectedValue instanceof Integer;
            case RESPONSE_TIME -> expectedValue instanceof Number;
            case COLLECTION_SIZE -> expectedValue instanceof Integer;
            case CUSTOM -> parameters != null && parameters.containsKey("customAssertion");
        };
    }
    
    /**
     * Executes this assertion against the actual value.
     */
    public TestAssertion executeAssertion(Object actualValue) {
        AssertionResult result = performAssertion(actualValue);
        return this.withActualValue(actualValue).withResult(result);
    }
    
    /**
     * Performs the actual assertion logic.
     */
    private AssertionResult performAssertion(Object actualValue) {
        try {
            boolean passed = evaluateAssertion(actualValue);
            String message = passed ? "Assertion passed" : getFailureMessage(actualValue);
            return new AssertionResult(passed, message, null);
        } catch (Exception e) {
            return new AssertionResult(false, "Assertion failed with exception: " + e.getMessage(), e);
        }
    }
    
    /**
     * Evaluates the assertion based on type and operator.
     */
    private boolean evaluateAssertion(Object actualValue) {
        return switch (assertionType) {
            case EQUALS -> operator.evaluate(actualValue, expectedValue);
            case NOT_EQUALS -> !AssertionOperator.EQUALS.evaluate(actualValue, expectedValue);
            case GREATER_THAN -> operator.evaluate(actualValue, expectedValue);
            case LESS_THAN -> operator.evaluate(actualValue, expectedValue);
            case CONTAINS -> evaluateContains(actualValue);
            case NOT_CONTAINS -> !evaluateContains(actualValue);
            case NULL_CHECK -> actualValue == null;
            case NOT_NULL_CHECK -> actualValue != null;
            case EMPTY_CHECK -> evaluateEmpty(actualValue);
            case NOT_EMPTY_CHECK -> !evaluateEmpty(actualValue);
            case REGEX_MATCH -> evaluateRegex(actualValue);
            case JSON_PATH -> evaluateJsonPath(actualValue);
            case XML_PATH -> evaluateXmlPath(actualValue);
            case HTTP_STATUS -> evaluateHttpStatus(actualValue);
            case RESPONSE_TIME -> evaluateResponseTime(actualValue);
            case COLLECTION_SIZE -> evaluateCollectionSize(actualValue);
            case CUSTOM -> evaluateCustomAssertion(actualValue);
        };
    }
    
    private boolean evaluateContains(Object actualValue) {
        if (actualValue instanceof String actualString && expectedValue instanceof String expectedString) {
            return actualString.contains(expectedString);
        }
        if (actualValue instanceof java.util.Collection<?> collection) {
            return collection.contains(expectedValue);
        }
        return false;
    }
    
    private boolean evaluateEmpty(Object actualValue) {
        if (actualValue == null) return true;
        if (actualValue instanceof String s) return s.isEmpty();
        if (actualValue instanceof java.util.Collection<?> c) return c.isEmpty();
        if (actualValue instanceof Map<?, ?> m) return m.isEmpty();
        return false;
    }
    
    private boolean evaluateRegex(Object actualValue) {
        if (actualValue instanceof String actualString && expectedValue instanceof String pattern) {
            return actualString.matches(pattern);
        }
        return false;
    }
    
    private boolean evaluateJsonPath(Object actualValue) {
        // In real implementation, would use JsonPath library
        // For now, simple implementation
        String jsonPath = (String) parameters.get("jsonPath");
        return jsonPath != null && actualValue != null;
    }
    
    private boolean evaluateXmlPath(Object actualValue) {
        // In real implementation, would use XPath library
        // For now, simple implementation
        String xmlPath = (String) parameters.get("xmlPath");
        return xmlPath != null && actualValue != null;
    }
    
    private boolean evaluateHttpStatus(Object actualValue) {
        if (actualValue instanceof Integer status && expectedValue instanceof Integer expectedStatus) {
            return status.equals(expectedStatus);
        }
        return false;
    }
    
    private boolean evaluateResponseTime(Object actualValue) {
        if (actualValue instanceof Number actualTime && expectedValue instanceof Number expectedTime) {
            return actualTime.doubleValue() <= expectedTime.doubleValue();
        }
        return false;
    }
    
    private boolean evaluateCollectionSize(Object actualValue) {
        if (actualValue instanceof java.util.Collection<?> collection && expectedValue instanceof Integer expectedSize) {
            return collection.size() == expectedSize;
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    private boolean evaluateCustomAssertion(Object actualValue) {
        Object customAssertion = parameters.get("customAssertion");
        if (customAssertion instanceof Predicate<?> predicate) {
            return ((Predicate<Object>) predicate).test(actualValue);
        }
        return false;
    }
    
    private String getFailureMessage(Object actualValue) {
        if (failureMessage != null) {
            return failureMessage;
        }
        
        return String.format("Assertion '%s' failed: expected %s %s %s, but got %s",
                name, target, operator, expectedValue, actualValue);
    }
    
    /**
     * Creates a simple equality assertion.
     */
    public static TestAssertion assertEquals(String name, String target, Object expectedValue) {
        return TestAssertion.builder()
                .name(name)
                .assertionType(AssertionType.EQUALS)
                .target(target)
                .operator(AssertionOperator.EQUALS)
                .expectedValue(expectedValue)
                .build();
    }
    
    /**
     * Creates a null check assertion.
     */
    public static TestAssertion assertNull(String name, String target) {
        return TestAssertion.builder()
                .name(name)
                .assertionType(AssertionType.NULL_CHECK)
                .target(target)
                .operator(AssertionOperator.EQUALS)
                .build();
    }
    
    /**
     * Creates a not null check assertion.
     */
    public static TestAssertion assertNotNull(String name, String target) {
        return TestAssertion.builder()
                .name(name)
                .assertionType(AssertionType.NOT_NULL_CHECK)
                .target(target)
                .operator(AssertionOperator.EQUALS)
                .build();
    }
    
    /**
     * Creates an HTTP status assertion.
     */
    public static TestAssertion assertHttpStatus(String name, int expectedStatus) {
        return TestAssertion.builder()
                .name(name)
                .assertionType(AssertionType.HTTP_STATUS)
                .target("httpStatus")
                .operator(AssertionOperator.EQUALS)
                .expectedValue(expectedStatus)
                .build();
    }
    
    /**
     * Types of assertions available.
     */
    public enum AssertionType {
        EQUALS("Equals"),
        NOT_EQUALS("Not Equals"),
        GREATER_THAN("Greater Than"),
        LESS_THAN("Less Than"),
        CONTAINS("Contains"),
        NOT_CONTAINS("Not Contains"),
        NULL_CHECK("Null Check"),
        NOT_NULL_CHECK("Not Null Check"),
        EMPTY_CHECK("Empty Check"),
        NOT_EMPTY_CHECK("Not Empty Check"),
        REGEX_MATCH("Regex Match"),
        JSON_PATH("JSON Path"),
        XML_PATH("XML Path"),
        HTTP_STATUS("HTTP Status"),
        RESPONSE_TIME("Response Time"),
        COLLECTION_SIZE("Collection Size"),
        CUSTOM("Custom");
        
        private final String displayName;
        
        AssertionType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Result of an assertion execution.
     */
    public static class AssertionResult {
        private final boolean passed;
        private final String message;
        private final Exception error;
        
        public AssertionResult(boolean passed, String message, Exception error) {
            this.passed = passed;
            this.message = message;
            this.error = error;
        }
        
        public boolean isPassed() { return passed; }
        public String getMessage() { return message; }
        public Exception getError() { return error; }
        public boolean hasError() { return error != null; }
    }
}