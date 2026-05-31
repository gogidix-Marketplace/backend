package com.gogidix.shared.testing.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.Map;
import java.util.List;

/**
 * Domain entity representing a single test step within a test specification.
 * Defines an atomic action to be performed during test execution.
 */
@Data
@With
@Builder
public class TestStep {
    
    private final String name;
    private final String description;
    private final TestStepType stepType;
    private final int order;
    private final Map<String, Object> parameters;
    private final List<TestAssertion> assertions;
    private final int estimatedDurationSeconds;
    private final boolean optional;
    private final String skipCondition;
    private final TestStep prerequisiteStep;
    private final Map<String, String> metadata;
    
    /**
     * Validates if this test step is properly configured.
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
               stepType != null &&
               order >= 0 &&
               estimatedDurationSeconds > 0 &&
               hasRequiredParameters();
    }
    
    /**
     * Checks if all required parameters are present based on step type.
     */
    private boolean hasRequiredParameters() {
        if (parameters == null) return false;
        
        return switch (stepType) {
            case HTTP_REQUEST -> parameters.containsKey("url") && parameters.containsKey("method");
            case DATABASE_OPERATION -> parameters.containsKey("query") || parameters.containsKey("operation");
            case MESSAGE_PUBLISH -> parameters.containsKey("topic") || parameters.containsKey("queue");
            case MESSAGE_CONSUME -> parameters.containsKey("topic") || parameters.containsKey("queue");
            case METHOD_CALL -> parameters.containsKey("className") && parameters.containsKey("methodName");
            case FILE_OPERATION -> parameters.containsKey("filePath") && parameters.containsKey("operation");
            case API_CALL -> parameters.containsKey("endpoint");
            case WAIT_FOR_CONDITION -> parameters.containsKey("condition") && parameters.containsKey("timeout");
            case SETUP_DATA -> parameters.containsKey("dataType");
            case CLEANUP_DATA -> parameters.containsKey("dataType");
            case MOCK_SETUP -> parameters.containsKey("mockTarget");
            case CONDITIONAL_LOGIC -> parameters.containsKey("condition");
            case PARALLEL_EXECUTION -> parameters.containsKey("steps");
            case CUSTOM_ACTION -> parameters.containsKey("actionClass");
            case VERIFICATION -> parameters.containsKey("assertion") || parameters.containsKey("expected");
            case PERFORMANCE_MEASURE -> parameters.containsKey("metric");
            case SECURITY_CHECK -> parameters.containsKey("checkType");
            case CONFIGURATION -> parameters.containsKey("configKey");
            case DEBUG_LOG -> true; // No required parameters for logging
        };
    }
    
    /**
     * Gets the parameter value as a specific type.
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameter(String key, Class<T> type) {
        Object value = parameters != null ? parameters.get(key) : null;
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
    
    /**
     * Gets the parameter value as string with default.
     */
    public String getParameterAsString(String key, String defaultValue) {
        Object value = parameters != null ? parameters.get(key) : null;
        return value != null ? value.toString() : defaultValue;
    }
    
    /**
     * Gets the parameter value as integer with default.
     */
    public int getParameterAsInt(String key, int defaultValue) {
        Object value = parameters != null ? parameters.get(key) : null;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * Gets the parameter value as boolean with default.
     */
    public boolean getParameterAsBoolean(String key, boolean defaultValue) {
        Object value = parameters != null ? parameters.get(key) : null;
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof String) return Boolean.parseBoolean((String) value);
        return defaultValue;
    }
    
    /**
     * Checks if this step should be skipped based on conditions.
     */
    public boolean shouldSkip(Map<String, Object> context) {
        if (optional && skipCondition != null) {
            return evaluateSkipCondition(context);
        }
        return false;
    }
    
    /**
     * Evaluates skip condition (simplified implementation).
     */
    private boolean evaluateSkipCondition(Map<String, Object> context) {
        if (skipCondition == null || context == null) return false;
        
        // Simple condition evaluation - in real implementation would be more sophisticated
        if (skipCondition.startsWith("context.")) {
            String key = skipCondition.substring(8);
            Object value = context.get(key);
            return value == null || "false".equals(value.toString());
        }
        
        return false;
    }
    
    /**
     * Checks if this step has prerequisites.
     */
    public boolean hasPrerequisites() {
        return prerequisiteStep != null;
    }
    
    /**
     * Checks if this step can run in parallel with others.
     */
    public boolean canRunInParallel() {
        return stepType != TestStepType.DATABASE_OPERATION && // DB ops might conflict
               stepType != TestStepType.FILE_OPERATION &&     // File ops might conflict
               !hasPrerequisites() &&                         // Prerequisites create dependencies
               !optional;                                      // Optional steps run sequentially
    }
    
    /**
     * Gets the risk level of this step (for error handling).
     */
    public RiskLevel getRiskLevel() {
        return switch (stepType) {
            case DATABASE_OPERATION, FILE_OPERATION -> RiskLevel.HIGH;
            case HTTP_REQUEST, API_CALL, MESSAGE_PUBLISH -> RiskLevel.MEDIUM;
            case METHOD_CALL, SETUP_DATA, CLEANUP_DATA -> RiskLevel.LOW;
            case WAIT_FOR_CONDITION, MOCK_SETUP -> RiskLevel.MINIMAL;
            default -> RiskLevel.LOW;
        };
    }
    
    /**
     * Creates a copy of this step with different parameters.
     */
    public TestStep withUpdatedParameters(Map<String, Object> newParameters) {
        Map<String, Object> mergedParams = parameters != null ? 
            new java.util.HashMap<>(parameters) : new java.util.HashMap<>();
        mergedParams.putAll(newParameters);
        return this.withParameters(mergedParams);
    }
    
    /**
     * Creates a copy of this step with additional assertions.
     */
    public TestStep withAdditionalAssertions(List<TestAssertion> newAssertions) {
        List<TestAssertion> mergedAssertions = new java.util.ArrayList<>();
        if (assertions != null) mergedAssertions.addAll(assertions);
        mergedAssertions.addAll(newAssertions);
        return this.withAssertions(mergedAssertions);
    }
    
    /**
     * Risk levels for test step execution.
     */
    public enum RiskLevel {
        MINIMAL, LOW, MEDIUM, HIGH
    }
}