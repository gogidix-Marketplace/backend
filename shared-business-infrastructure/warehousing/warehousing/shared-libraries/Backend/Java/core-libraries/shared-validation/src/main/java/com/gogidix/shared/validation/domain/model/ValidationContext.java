package com.gogidix.shared.validation.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Domain entity representing the context in which validation is performed.
 * Provides environment and metadata for validation execution.
 */
@Data
@With
@Builder
public class ValidationContext {
    
    private final UUID id;
    private final String contextName;
    private final String description;
    private final ContextType contextType;
    private final String userId;
    private final String sessionId;
    private final String requestId;
    private final String entityType;
    private final String entityId;
    private final String operationType; // CREATE, UPDATE, DELETE, etc.
    private final Map<String, Object> contextData;
    private final Map<String, Object> environmentVariables;
    private final Set<String> enabledFeatures;
    private final LocalDateTime timestamp;
    private final String locale;
    private final String timezone;
    private final ValidationMode mode;
    private final SecurityLevel securityLevel;
    private final Map<String, Object> customProperties;
    
    /**
     * Type of validation context.
     */
    public enum ContextType {
        WEB_REQUEST("Web Request", "HTTP request validation"),
        API_CALL("API Call", "REST API validation"),
        BATCH_PROCESSING("Batch Processing", "Batch job validation"),
        BACKGROUND_TASK("Background Task", "Async task validation"),
        DATA_IMPORT("Data Import", "Data import validation"),
        USER_INPUT("User Input", "User form validation"),
        SYSTEM_INTERNAL("System Internal", "Internal system validation"),
        INTEGRATION("Integration", "External system integration"),
        TEST("Test", "Testing environment validation"),
        MIGRATION("Migration", "Data migration validation");
        
        private final String displayName;
        private final String description;
        
        ContextType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Mode of validation execution.
     */
    public enum ValidationMode {
        STRICT("Strict", "All validations must pass"),
        LENIENT("Lenient", "Allow warnings to pass"),
        FAIL_FAST("Fail Fast", "Stop on first error"),
        COLLECT_ALL("Collect All", "Collect all validation errors"),
        DRY_RUN("Dry Run", "Validation without side effects"),
        PREVIEW("Preview", "Show what would be validated");
        
        private final String displayName;
        private final String description;
        
        ValidationMode(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Security level for validation context.
     */
    public enum SecurityLevel {
        LOW("Low", "Standard validation"),
        NORMAL("Normal", "Normal security validation"),
        HIGH("High", "Enhanced security validation"),
        CRITICAL("Critical", "Critical security validation");
        
        private final String displayName;
        private final String description;
        
        SecurityLevel(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Gets a value from the context data.
     */
    public Object getContextValue(String key) {
        return contextData != null ? contextData.get(key) : null;
    }
    
    /**
     * Gets a typed value from the context data.
     */
    @SuppressWarnings("unchecked")
    public <T> T getContextValue(String key, Class<T> type) {
        Object value = getContextValue(key);
        return type.isInstance(value) ? (T) value : null;
    }
    
    /**
     * Gets an environment variable.
     */
    public String getEnvironmentVariable(String key) {
        return environmentVariables != null ? (String) environmentVariables.get(key) : null;
    }
    
    /**
     * Checks if a feature is enabled in this context.
     */
    public boolean isFeatureEnabled(String feature) {
        return enabledFeatures != null && enabledFeatures.contains(feature);
    }
    
    /**
     * Checks if this context is for a specific operation type.
     */
    public boolean isOperationType(String operation) {
        return operationType != null && operationType.equalsIgnoreCase(operation);
    }
    
    /**
     * Checks if this context is in strict validation mode.
     */
    public boolean isStrictMode() {
        return mode == ValidationMode.STRICT;
    }
    
    /**
     * Checks if this context should fail fast on errors.
     */
    public boolean isFailFast() {
        return mode == ValidationMode.FAIL_FAST;
    }
    
    /**
     * Checks if this context should collect all errors.
     */
    public boolean isCollectAll() {
        return mode == ValidationMode.COLLECT_ALL;
    }
    
    /**
     * Checks if this context is a dry run.
     */
    public boolean isDryRun() {
        return mode == ValidationMode.DRY_RUN;
    }
    
    /**
     * Creates a child context for nested validation.
     */
    public ValidationContext createChildContext(String contextName) {
        return ValidationContext.builder()
                .id(UUID.randomUUID())
                .contextName(contextName)
                .contextType(this.contextType)
                .userId(this.userId)
                .sessionId(this.sessionId)
                .requestId(this.requestId)
                .entityType(this.entityType)
                .mode(this.mode)
                .securityLevel(this.securityLevel)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates a web request validation context.
     */
    public static ValidationContext webRequest(String userId, String requestId) {
        return ValidationContext.builder()
                .id(UUID.randomUUID())
                .contextName("Web Request")
                .contextType(ContextType.WEB_REQUEST)
                .userId(userId)
                .requestId(requestId)
                .mode(ValidationMode.STRICT)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates an API call validation context.
     */
    public static ValidationContext apiCall(String requestId, String entityType) {
        return ValidationContext.builder()
                .id(UUID.randomUUID())
                .contextName("API Call")
                .contextType(ContextType.API_CALL)
                .requestId(requestId)
                .entityType(entityType)
                .mode(ValidationMode.FAIL_FAST)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates a batch processing validation context.
     */
    public static ValidationContext batchProcessing(String sessionId) {
        return ValidationContext.builder()
                .id(UUID.randomUUID())
                .contextName("Batch Processing")
                .contextType(ContextType.BATCH_PROCESSING)
                .sessionId(sessionId)
                .mode(ValidationMode.COLLECT_ALL)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates a test validation context.
     */
    public static ValidationContext test(String testName) {
        return ValidationContext.builder()
                .id(UUID.randomUUID())
                .contextName("Test: " + testName)
                .contextType(ContextType.TEST)
                .mode(ValidationMode.STRICT)
                .timestamp(LocalDateTime.now())
                .build();
    }
}