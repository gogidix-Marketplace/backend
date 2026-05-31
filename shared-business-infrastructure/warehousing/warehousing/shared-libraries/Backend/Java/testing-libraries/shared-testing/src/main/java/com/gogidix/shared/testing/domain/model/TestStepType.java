package com.gogidix.shared.testing.domain.model;

/**
 * Enumeration of available test step types.
 * Defines the different kinds of actions that can be performed in test steps.
 */
public enum TestStepType {
    
    /**
     * HTTP request to external service or API.
     */
    HTTP_REQUEST("HTTP Request", "Make HTTP request to specified URL", true, false),
    
    /**
     * Database operation (query, insert, update, delete).
     */
    DATABASE_OPERATION("Database Operation", "Execute database query or operation", true, true),
    
    /**
     * Publish message to queue or topic.
     */
    MESSAGE_PUBLISH("Message Publish", "Publish message to queue/topic", true, false),
    
    /**
     * Consume message from queue or topic.
     */
    MESSAGE_CONSUME("Message Consume", "Consume message from queue/topic", true, false),
    
    /**
     * Call method on a class or object.
     */
    METHOD_CALL("Method Call", "Invoke method on class or object", false, false),
    
    /**
     * File system operation (read, write, delete).
     */
    FILE_OPERATION("File Operation", "Perform file system operation", true, true),
    
    /**
     * Call to REST API endpoint.
     */
    API_CALL("API Call", "Make REST API call", true, false),
    
    /**
     * Wait for specific condition to be met.
     */
    WAIT_FOR_CONDITION("Wait for Condition", "Wait until condition is satisfied", false, false),
    
    /**
     * Set up test data.
     */
    SETUP_DATA("Setup Data", "Create test data for the test", false, true),
    
    /**
     * Clean up test data.
     */
    CLEANUP_DATA("Cleanup Data", "Remove test data after test", false, true),
    
    /**
     * Set up mocks or stubs.
     */
    MOCK_SETUP("Mock Setup", "Configure mocks or stubs", false, false),
    
    /**
     * Conditional logic execution.
     */
    CONDITIONAL_LOGIC("Conditional Logic", "Execute based on condition", false, false),
    
    /**
     * Execute steps in parallel.
     */
    PARALLEL_EXECUTION("Parallel Execution", "Execute multiple steps simultaneously", false, false),
    
    /**
     * Custom action implementation.
     */
    CUSTOM_ACTION("Custom Action", "Execute custom test action", false, false),
    
    /**
     * Verification step (assertions).
     */
    VERIFICATION("Verification", "Verify expected outcomes", false, false),
    
    /**
     * Performance measurement.
     */
    PERFORMANCE_MEASURE("Performance Measure", "Measure performance metrics", false, false),
    
    /**
     * Security check.
     */
    SECURITY_CHECK("Security Check", "Perform security validation", true, false),
    
    /**
     * Configuration step.
     */
    CONFIGURATION("Configuration", "Configure test environment", false, true),
    
    /**
     * Logging or debugging step.
     */
    DEBUG_LOG("Debug Log", "Log debug information", false, false);
    
    private final String displayName;
    private final String description;
    private final boolean requiresExternalConnection;
    private final boolean modifiesState;
    
    TestStepType(String displayName, String description, boolean requiresExternalConnection, boolean modifiesState) {
        this.displayName = displayName;
        this.description = description;
        this.requiresExternalConnection = requiresExternalConnection;
        this.modifiesState = modifiesState;
    }
    
    /**
     * Gets the human-readable display name.
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the detailed description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if this step type requires external connections.
     */
    public boolean requiresExternalConnection() {
        return requiresExternalConnection;
    }
    
    /**
     * Checks if this step type modifies system state.
     */
    public boolean modifiesState() {
        return modifiesState;
    }
    
    /**
     * Checks if this step type is safe for parallel execution.
     */
    public boolean isSafeForParallelExecution() {
        return !modifiesState && this != WAIT_FOR_CONDITION;
    }
    
    /**
     * Gets the category of this step type.
     */
    public StepCategory getCategory() {
        return switch (this) {
            case HTTP_REQUEST, API_CALL -> StepCategory.NETWORK;
            case DATABASE_OPERATION -> StepCategory.DATABASE;
            case MESSAGE_PUBLISH, MESSAGE_CONSUME -> StepCategory.MESSAGING;
            case METHOD_CALL -> StepCategory.UNIT_TEST;
            case FILE_OPERATION -> StepCategory.FILE_SYSTEM;
            case SETUP_DATA, CLEANUP_DATA -> StepCategory.DATA_MANAGEMENT;
            case MOCK_SETUP -> StepCategory.MOCK;
            case WAIT_FOR_CONDITION, CONDITIONAL_LOGIC, PARALLEL_EXECUTION -> StepCategory.CONTROL_FLOW;
            case VERIFICATION -> StepCategory.ASSERTION;
            case PERFORMANCE_MEASURE -> StepCategory.PERFORMANCE;
            case SECURITY_CHECK -> StepCategory.SECURITY;
            case CONFIGURATION -> StepCategory.SETUP;
            case DEBUG_LOG -> StepCategory.DEBUG;
            case CUSTOM_ACTION -> StepCategory.CUSTOM;
        };
    }
    
    /**
     * Gets the estimated base duration for this step type (in seconds).
     */
    public int getEstimatedBaseDuration() {
        return switch (this) {
            case HTTP_REQUEST, API_CALL -> 5;
            case DATABASE_OPERATION -> 3;
            case MESSAGE_PUBLISH -> 2;
            case MESSAGE_CONSUME -> 10; // May need to wait
            case METHOD_CALL -> 1;
            case FILE_OPERATION -> 2;
            case WAIT_FOR_CONDITION -> 30; // Highly variable
            case SETUP_DATA, CLEANUP_DATA -> 5;
            case MOCK_SETUP -> 1;
            case CONDITIONAL_LOGIC -> 1;
            case PARALLEL_EXECUTION -> 20; // Depends on sub-steps
            case VERIFICATION -> 1;
            case PERFORMANCE_MEASURE -> 60; // Needs time for measurement
            case SECURITY_CHECK -> 10;
            case CONFIGURATION -> 3;
            case DEBUG_LOG -> 1;
            case CUSTOM_ACTION -> 10; // Unknown, conservative estimate
        };
    }
    
    /**
     * Gets step types by category.
     */
    public static TestStepType[] getByCategory(StepCategory category) {
        return java.util.Arrays.stream(values())
                .filter(type -> type.getCategory() == category)
                .toArray(TestStepType[]::new);
    }
    
    /**
     * Gets step types that are safe for parallel execution.
     */
    public static TestStepType[] getParallelSafeTypes() {
        return java.util.Arrays.stream(values())
                .filter(TestStepType::isSafeForParallelExecution)
                .toArray(TestStepType[]::new);
    }
    
    /**
     * Categories for grouping step types.
     */
    public enum StepCategory {
        NETWORK("Network Operations"),
        DATABASE("Database Operations"), 
        MESSAGING("Message Queue Operations"),
        UNIT_TEST("Unit Test Operations"),
        FILE_SYSTEM("File System Operations"),
        DATA_MANAGEMENT("Data Management"),
        MOCK("Mock and Stub Operations"),
        CONTROL_FLOW("Control Flow"),
        ASSERTION("Assertions and Verification"),
        PERFORMANCE("Performance Testing"),
        SECURITY("Security Testing"),
        SETUP("Setup and Configuration"),
        DEBUG("Debug and Logging"),
        CUSTOM("Custom Actions");
        
        private final String displayName;
        
        StepCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}