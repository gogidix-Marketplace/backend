package com.gogidix.shared.validation.domain.model;

import java.util.Map;
import java.util.Set;

/**
 * Domain value object for validation configuration.
 * Defines how validation should be executed.
 */
public class ValidationConfiguration {
    
    private final boolean strictMode;
    private final boolean earlyExit;
    private final boolean parallelExecution;
    private final int maxConcurrentValidations;
    private final long timeoutMs;
    private final boolean cacheResults;
    private final long cacheTtlMs;
    private final Set<String> enabledCategories;
    private final Set<String> disabledCategories;
    private final Map<String, Object> customSettings;
    private final ValidationMode validationMode;
    private final boolean collectMetrics;
    private final boolean auditTrail;
    
    private ValidationConfiguration(boolean strictMode, boolean earlyExit, boolean parallelExecution,
                                   int maxConcurrentValidations, long timeoutMs, boolean cacheResults,
                                   long cacheTtlMs, Set<String> enabledCategories, Set<String> disabledCategories,
                                   Map<String, Object> customSettings, ValidationMode validationMode,
                                   boolean collectMetrics, boolean auditTrail) {
        this.strictMode = strictMode;
        this.earlyExit = earlyExit;
        this.parallelExecution = parallelExecution;
        this.maxConcurrentValidations = maxConcurrentValidations;
        this.timeoutMs = timeoutMs;
        this.cacheResults = cacheResults;
        this.cacheTtlMs = cacheTtlMs;
        this.enabledCategories = enabledCategories != null ? Set.copyOf(enabledCategories) : Set.of();
        this.disabledCategories = disabledCategories != null ? Set.copyOf(disabledCategories) : Set.of();
        this.customSettings = customSettings != null ? Map.copyOf(customSettings) : Map.of();
        this.validationMode = validationMode != null ? validationMode : ValidationMode.STANDARD;
        this.collectMetrics = collectMetrics;
        this.auditTrail = auditTrail;
    }
    
    /**
     * Creates a default configuration
     */
    public static ValidationConfiguration defaultConfig() {
        return new ValidationConfiguration(
            false, // strictMode
            false, // earlyExit
            true,  // parallelExecution
            10,    // maxConcurrentValidations
            30000, // timeoutMs (30 seconds)
            true,  // cacheResults
            300000, // cacheTtlMs (5 minutes)
            Set.of(), // enabledCategories (all enabled)
            Set.of(), // disabledCategories
            Map.of(), // customSettings
            ValidationMode.STANDARD,
            true,  // collectMetrics
            false  // auditTrail
        );
    }
    
    /**
     * Creates a strict configuration for production
     */
    public static ValidationConfiguration strictConfig() {
        return new ValidationConfiguration(
            true,  // strictMode
            true,  // earlyExit
            false, // parallelExecution
            1,     // maxConcurrentValidations
            60000, // timeoutMs (60 seconds)
            false, // cacheResults
            0,     // cacheTtlMs
            Set.of(), // enabledCategories
            Set.of(), // disabledCategories
            Map.of(), // customSettings
            ValidationMode.STRICT,
            true,  // collectMetrics
            true   // auditTrail
        );
    }
    
    /**
     * Creates a performance configuration
     */
    public static ValidationConfiguration performanceConfig() {
        return new ValidationConfiguration(
            false, // strictMode
            true,  // earlyExit
            true,  // parallelExecution
            50,    // maxConcurrentValidations
            10000, // timeoutMs (10 seconds)
            true,  // cacheResults
            600000, // cacheTtlMs (10 minutes)
            Set.of(), // enabledCategories
            Set.of(), // disabledCategories
            Map.of(), // customSettings
            ValidationMode.PERFORMANCE,
            false, // collectMetrics
            false  // auditTrail
        );
    }
    
    /**
     * Builder for custom configuration
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Checks if a category is enabled
     */
    public boolean isCategoryEnabled(String category) {
        if (enabledCategories.isEmpty()) {
            return !disabledCategories.contains(category);
        }
        return enabledCategories.contains(category) && !disabledCategories.contains(category);
    }
    
    /**
     * Gets effective timeout considering mode
     */
    public long getEffectiveTimeout() {
        return validationMode == ValidationMode.PERFORMANCE ? 
               Math.min(timeoutMs, 5000) : timeoutMs;
    }
    
    /**
     * Gets effective concurrent validations considering mode
     */
    public int getEffectiveConcurrentValidations() {
        if (validationMode == ValidationMode.STRICT) {
            return 1;
        }
        if (validationMode == ValidationMode.PERFORMANCE) {
            return Math.max(maxConcurrentValidations, 20);
        }
        return maxConcurrentValidations;
    }
    
    // Getters
    public boolean isStrictMode() { return strictMode; }
    public boolean isEarlyExit() { return earlyExit; }
    public boolean isParallelExecution() { return parallelExecution; }
    public int getMaxConcurrentValidations() { return maxConcurrentValidations; }
    public long getTimeoutMs() { return timeoutMs; }
    public boolean isCacheResults() { return cacheResults; }
    public long getCacheTtlMs() { return cacheTtlMs; }
    public Set<String> getEnabledCategories() { return enabledCategories; }
    public Set<String> getDisabledCategories() { return disabledCategories; }
    public Map<String, Object> getCustomSettings() { return customSettings; }
    public ValidationMode getValidationMode() { return validationMode; }
    public boolean isCollectMetrics() { return collectMetrics; }
    public boolean isAuditTrail() { return auditTrail; }
    
    /**
     * Validation execution mode
     */
    public enum ValidationMode {
        STANDARD("Standard", "Default validation mode"),
        STRICT("Strict", "Strict validation with all checks enabled"),
        PERFORMANCE("Performance", "Optimized for performance"),
        DEVELOPMENT("Development", "Development mode with detailed logging"),
        TESTING("Testing", "Testing mode with mocked dependencies");
        
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
     * Builder for ValidationConfiguration
     */
    public static class Builder {
        private boolean strictMode = false;
        private boolean earlyExit = false;
        private boolean parallelExecution = true;
        private int maxConcurrentValidations = 10;
        private long timeoutMs = 30000;
        private boolean cacheResults = true;
        private long cacheTtlMs = 300000;
        private Set<String> enabledCategories = Set.of();
        private Set<String> disabledCategories = Set.of();
        private Map<String, Object> customSettings = Map.of();
        private ValidationMode validationMode = ValidationMode.STANDARD;
        private boolean collectMetrics = true;
        private boolean auditTrail = false;
        
        public Builder strictMode(boolean strictMode) {
            this.strictMode = strictMode;
            return this;
        }
        
        public Builder earlyExit(boolean earlyExit) {
            this.earlyExit = earlyExit;
            return this;
        }
        
        public Builder parallelExecution(boolean parallelExecution) {
            this.parallelExecution = parallelExecution;
            return this;
        }
        
        public Builder maxConcurrentValidations(int maxConcurrentValidations) {
            this.maxConcurrentValidations = maxConcurrentValidations;
            return this;
        }
        
        public Builder timeoutMs(long timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }
        
        public Builder cacheResults(boolean cacheResults) {
            this.cacheResults = cacheResults;
            return this;
        }
        
        public Builder cacheTtlMs(long cacheTtlMs) {
            this.cacheTtlMs = cacheTtlMs;
            return this;
        }
        
        public Builder enabledCategories(Set<String> enabledCategories) {
            this.enabledCategories = enabledCategories;
            return this;
        }
        
        public Builder disabledCategories(Set<String> disabledCategories) {
            this.disabledCategories = disabledCategories;
            return this;
        }
        
        public Builder customSettings(Map<String, Object> customSettings) {
            this.customSettings = customSettings;
            return this;
        }
        
        public Builder validationMode(ValidationMode validationMode) {
            this.validationMode = validationMode;
            return this;
        }
        
        public Builder collectMetrics(boolean collectMetrics) {
            this.collectMetrics = collectMetrics;
            return this;
        }
        
        public Builder auditTrail(boolean auditTrail) {
            this.auditTrail = auditTrail;
            return this;
        }
        
        public ValidationConfiguration build() {
            return new ValidationConfiguration(
                strictMode, earlyExit, parallelExecution, maxConcurrentValidations,
                timeoutMs, cacheResults, cacheTtlMs, enabledCategories, disabledCategories,
                customSettings, validationMode, collectMetrics, auditTrail
            );
        }
    }
}