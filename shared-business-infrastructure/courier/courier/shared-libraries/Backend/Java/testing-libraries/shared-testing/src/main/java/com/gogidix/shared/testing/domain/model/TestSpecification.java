package com.gogidix.shared.testing.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Domain entity representing a test specification.
 * Defines what should be tested, how it should be tested, and expected outcomes.
 */
@Data
@With
@Builder
public class TestSpecification {
    
    private final UUID id;
    private final String name;
    private final String description;
    private final TestType testType;
    private final TestSuite testSuite;
    private final TestPriority priority;
    private final List<TestStep> steps;
    private final Map<String, Object> parameters;
    private final List<TestAssertion> assertions;
    private final TestEnvironment targetEnvironment;
    private final List<String> tags;
    private final String createdBy;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final boolean enabled;
    private final int timeoutSeconds;
    private final int retryCount;
    private final Map<String, String> metadata;
    
    /**
     * Validates if the test specification is complete and executable.
     */
    public boolean isExecutable() {
        return name != null && !name.trim().isEmpty() &&
               testType != null &&
               steps != null && !steps.isEmpty() &&
               steps.stream().allMatch(TestStep::isValid) &&
               assertions != null && !assertions.isEmpty() &&
               enabled;
    }
    
    /**
     * Gets the estimated execution time based on steps and timeout.
     */
    public int getEstimatedExecutionTimeSeconds() {
        int stepsTime = steps != null ? 
            steps.stream().mapToInt(TestStep::getEstimatedDurationSeconds).sum() : 0;
        return Math.min(stepsTime, timeoutSeconds);
    }
    
    /**
     * Checks if the test has specific requirements.
     */
    public boolean requiresDatabase() {
        return tags != null && tags.contains("database") ||
               steps != null && steps.stream().anyMatch(step -> 
                   step.getStepType() == TestStepType.DATABASE_OPERATION);
    }
    
    public boolean requiresMessageQueue() {
        return tags != null && tags.contains("messaging") ||
               steps != null && steps.stream().anyMatch(step -> 
                   step.getStepType() == TestStepType.MESSAGE_PUBLISH ||
                   step.getStepType() == TestStepType.MESSAGE_CONSUME);
    }
    
    public boolean requiresExternalService() {
        return tags != null && tags.contains("external") ||
               steps != null && steps.stream().anyMatch(step -> 
                   step.getStepType() == TestStepType.HTTP_REQUEST ||
                   step.getStepType() == TestStepType.API_CALL);
    }
    
    /**
     * Gets the complexity score of this test (1-10).
     */
    public int getComplexityScore() {
        int score = 1; // Base complexity
        
        if (steps != null) {
            score += Math.min(steps.size() / 2, 3); // Steps complexity
            score += (int) steps.stream().filter(step -> 
                step.getStepType() == TestStepType.CONDITIONAL_LOGIC).count();
        }
        
        if (assertions != null) {
            score += Math.min(assertions.size() / 3, 2); // Assertions complexity
        }
        
        if (requiresDatabase()) score += 1;
        if (requiresMessageQueue()) score += 1;
        if (requiresExternalService()) score += 2;
        
        return Math.min(score, 10);
    }
    
    /**
     * Checks if this test can run in parallel with other tests.
     */
    public boolean canRunInParallel() {
        return testType != TestType.END_TO_END && // E2E tests often conflict
               !tags.contains("sequential") && // Explicitly marked sequential
               !requiresSharedResources(); // No shared state
    }
    
    private boolean requiresSharedResources() {
        return tags != null && (
            tags.contains("shared-database") ||
            tags.contains("file-system") ||
            tags.contains("singleton")
        );
    }
    
    /**
     * Creates a copy of this specification with updated parameters.
     */
    public TestSpecification withUpdatedParameters(Map<String, Object> newParameters, String updatedBy) {
        Map<String, Object> mergedParams = parameters != null ? 
            new java.util.HashMap<>(parameters) : new java.util.HashMap<>();
        mergedParams.putAll(newParameters);
        
        return this.withParameters(mergedParams)
                  .withUpdatedAt(LocalDateTime.now());
    }
    
    /**
     * Adds a new test step.
     */
    public TestSpecification addStep(TestStep step) {
        List<TestStep> newSteps = new java.util.ArrayList<>(steps != null ? steps : List.of());
        newSteps.add(step);
        return this.withSteps(newSteps);
    }
    
    /**
     * Removes a test step by index.
     */
    public TestSpecification removeStep(int index) {
        if (steps == null || index < 0 || index >= steps.size()) {
            return this;
        }
        List<TestStep> newSteps = new java.util.ArrayList<>(steps);
        newSteps.remove(index);
        return this.withSteps(newSteps);
    }
    
    /**
     * Adds a test assertion.
     */
    public TestSpecification addAssertion(TestAssertion assertion) {
        List<TestAssertion> newAssertions = new java.util.ArrayList<>(assertions != null ? assertions : List.of());
        newAssertions.add(assertion);
        return this.withAssertions(newAssertions);
    }
    
    /**
     * Updates the priority and adjusts timeout accordingly.
     */
    public TestSpecification updatePriority(TestPriority newPriority) {
        int newTimeout = switch (newPriority) {
            case CRITICAL -> Math.max(timeoutSeconds, 300); // At least 5 minutes for critical
            case HIGH -> Math.max(timeoutSeconds, 180);     // At least 3 minutes for high
            case NORMAL -> timeoutSeconds;                   // Keep current timeout
            case LOW -> Math.min(timeoutSeconds, 60);       // Max 1 minute for low
        };
        
        return this.withPriority(newPriority)
                  .withTimeoutSeconds(newTimeout);
    }
    
    /**
     * Enables or disables the test with reason.
     */
    public TestSpecification setEnabled(boolean enabled, String reason) {
        Map<String, String> newMetadata = new java.util.HashMap<>(metadata != null ? metadata : Map.of());
        newMetadata.put(enabled ? "enabled_reason" : "disabled_reason", reason);
        newMetadata.put("status_changed_at", LocalDateTime.now().toString());
        
        return this.withEnabled(enabled)
                  .withMetadata(newMetadata)
                  .withUpdatedAt(LocalDateTime.now());
    }
    
    /**
     * Test types available in the system.
     */
    public enum TestType {
        UNIT("Unit Test", "Tests individual components in isolation"),
        INTEGRATION("Integration Test", "Tests component interactions"),
        CONTRACT("Contract Test", "Tests API contracts and interfaces"),
        END_TO_END("End-to-End Test", "Tests complete user workflows"),
        PERFORMANCE("Performance Test", "Tests system performance and scalability"),
        SECURITY("Security Test", "Tests security vulnerabilities"),
        SMOKE("Smoke Test", "Basic functionality verification"),
        REGRESSION("Regression Test", "Verifies existing functionality after changes"),
        ACCEPTANCE("Acceptance Test", "Validates business requirements"),
        LOAD("Load Test", "Tests system under expected load"),
        STRESS("Stress Test", "Tests system beyond normal capacity"),
        API("API Test", "Tests REST/GraphQL API endpoints");
        
        private final String displayName;
        private final String description;
        
        TestType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Test priority levels.
     */
    public enum TestPriority {
        CRITICAL(4, "Must pass for release"),
        HIGH(3, "Important for quality"),
        NORMAL(2, "Standard test coverage"),
        LOW(1, "Nice to have");
        
        private final int level;
        private final String description;
        
        TestPriority(int level, String description) {
            this.level = level;
            this.description = description;
        }
        
        public int getLevel() { return level; }
        public String getDescription() { return description; }
    }
    
    /**
     * Test execution environments.
     */
    public enum TestEnvironment {
        UNIT_TEST("Unit", "In-memory testing"),
        INTEGRATION_TEST("Integration", "TestContainers environment"),
        STAGING("Staging", "Pre-production environment"),
        PRODUCTION("Production", "Live environment (monitoring only)"),
        LOCAL("Local", "Developer machine"),
        CI_CD("CI/CD", "Continuous integration pipeline");
        
        private final String displayName;
        private final String description;
        
        TestEnvironment(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
}