package com.gogidix.shared.validation.domain.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Domain value object representing a group of validation rules that can be executed together.
 * Groups allow for parallel execution of independent rules and sequential execution of dependent rules.
 */
public class ValidationGroup {
    
    private final String groupId;
    private final String name;
    private final List<ValidationRule> rules;
    private final int executionOrder;
    private final boolean parallelExecutionAllowed;
    private final Set<String> dependencies;
    private final Map<String, Object> groupMetadata;
    private final boolean executed;
    private final LocalDateTime executedAt;
    private final long executionTimeMs;
    private final List<ValidationResult> results;
    
    private ValidationGroup(String groupId, String name, List<ValidationRule> rules, int executionOrder,
                           boolean parallelExecutionAllowed, Set<String> dependencies,
                           Map<String, Object> groupMetadata, boolean executed, LocalDateTime executedAt,
                           long executionTimeMs, List<ValidationResult> results) {
        this.groupId = groupId;
        this.name = name;
        this.rules = rules != null ? List.copyOf(rules) : List.of();
        this.executionOrder = executionOrder;
        this.parallelExecutionAllowed = parallelExecutionAllowed;
        this.dependencies = dependencies != null ? Set.copyOf(dependencies) : Set.of();
        this.groupMetadata = groupMetadata != null ? Map.copyOf(groupMetadata) : Map.of();
        this.executed = executed;
        this.executedAt = executedAt;
        this.executionTimeMs = executionTimeMs;
        this.results = results != null ? List.copyOf(results) : List.of();
    }
    
    /**
     * Creates a new validation group
     */
    public static ValidationGroup create(String name, List<ValidationRule> rules, 
                                        int executionOrder, boolean parallelExecutionAllowed) {
        String groupId = UUID.randomUUID().toString();
        return new ValidationGroup(
            groupId, name, rules, executionOrder, parallelExecutionAllowed,
            Set.of(), Map.of(), false, null, 0, List.of()
        );
    }
    
    /**
     * Creates a validation group with dependencies
     */
    public static ValidationGroup createWithDependencies(String name, List<ValidationRule> rules,
                                                        int executionOrder, boolean parallelExecutionAllowed,
                                                        Set<String> dependencies) {
        String groupId = UUID.randomUUID().toString();
        return new ValidationGroup(
            groupId, name, rules, executionOrder, parallelExecutionAllowed,
            dependencies, Map.of(), false, null, 0, List.of()
        );
    }
    
    /**
     * Marks the group as executed with results
     */
    public ValidationGroup markExecuted(List<ValidationResult> executionResults, long executionTimeMs) {
        return new ValidationGroup(
            groupId, name, rules, executionOrder, parallelExecutionAllowed,
            dependencies, groupMetadata, true, LocalDateTime.now(),
            executionTimeMs, executionResults
        );
    }
    
    /**
     * Adds metadata to the group
     */
    public ValidationGroup withMetadata(String key, Object value) {
        Map<String, Object> updatedMetadata = new HashMap<>(groupMetadata);
        updatedMetadata.put(key, value);
        
        return new ValidationGroup(
            groupId, name, rules, executionOrder, parallelExecutionAllowed,
            dependencies, updatedMetadata, executed, executedAt,
            executionTimeMs, results
        );
    }
    
    /**
     * Checks if all dependencies are satisfied
     */
    public boolean areDependenciesSatisfied(Set<String> executedGroupIds) {
        return executedGroupIds.containsAll(dependencies);
    }
    
    /**
     * Gets the group's execution priority (lower is higher priority)
     */
    public int getPriority() {
        // Consider both execution order and number of dependencies
        return executionOrder * 100 + dependencies.size();
    }
    
    /**
     * Checks if group execution was successful
     */
    public boolean isSuccessful() {
        if (!executed || results.isEmpty()) {
            return false;
        }
        return results.stream().allMatch(ValidationResult::isSuccess);
    }
    
    /**
     * Gets failed results from this group
     */
    public List<ValidationResult> getFailedResults() {
        return results.stream()
                .filter(ValidationResult::isFailure)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets the number of rules in this group
     */
    public int getRuleCount() {
        return rules.size();
    }
    
    /**
     * Gets the average execution time per rule
     */
    public double getAverageExecutionTimePerRule() {
        if (!executed || rules.isEmpty()) {
            return 0.0;
        }
        return (double) executionTimeMs / rules.size();
    }
    
    /**
     * Checks if the group can be executed in parallel
     */
    public boolean canExecuteInParallel() {
        return parallelExecutionAllowed && dependencies.isEmpty();
    }
    
    /**
     * Gets group execution statistics
     */
    public GroupStatistics getStatistics() {
        if (!executed) {
            return GroupStatistics.notExecuted(groupId, name, rules.size());
        }
        
        long passedCount = results.stream().filter(ValidationResult::isSuccess).count();
        long failedCount = results.stream().filter(ValidationResult::isFailure).count();
        long skippedCount = results.stream()
                .filter(r -> r.getStatus() == ValidationResult.ValidationStatus.SKIPPED).count();
        
        return new GroupStatistics(
            groupId, name, rules.size(), results.size(),
            passedCount, failedCount, skippedCount,
            executionTimeMs, executedAt
        );
    }
    
    // Getters
    public String getGroupId() { return groupId; }
    public String getName() { return name; }
    public List<ValidationRule> getRules() { return rules; }
    public int getExecutionOrder() { return executionOrder; }
    public boolean isParallelExecutionAllowed() { return parallelExecutionAllowed; }
    public Set<String> getDependencies() { return dependencies; }
    public Map<String, Object> getGroupMetadata() { return groupMetadata; }
    public boolean isExecuted() { return executed; }
    public LocalDateTime getExecutedAt() { return executedAt; }
    public long getExecutionTimeMs() { return executionTimeMs; }
    public List<ValidationResult> getResults() { return results; }
    
    /**
     * Inner class for group statistics
     */
    public static class GroupStatistics {
        private final String groupId;
        private final String groupName;
        private final long totalRules;
        private final long executedRules;
        private final long passedValidations;
        private final long failedValidations;
        private final long skippedValidations;
        private final long executionTimeMs;
        private final LocalDateTime executedAt;
        
        public GroupStatistics(String groupId, String groupName, long totalRules, long executedRules,
                             long passedValidations, long failedValidations, long skippedValidations,
                             long executionTimeMs, LocalDateTime executedAt) {
            this.groupId = groupId;
            this.groupName = groupName;
            this.totalRules = totalRules;
            this.executedRules = executedRules;
            this.passedValidations = passedValidations;
            this.failedValidations = failedValidations;
            this.skippedValidations = skippedValidations;
            this.executionTimeMs = executionTimeMs;
            this.executedAt = executedAt;
        }
        
        public static GroupStatistics notExecuted(String groupId, String groupName, long totalRules) {
            return new GroupStatistics(groupId, groupName, totalRules, 0, 0, 0, 0, 0, null);
        }
        
        // Getters
        public String getGroupId() { return groupId; }
        public String getGroupName() { return groupName; }
        public long getTotalRules() { return totalRules; }
        public long getExecutedRules() { return executedRules; }
        public long getPassedValidations() { return passedValidations; }
        public long getFailedValidations() { return failedValidations; }
        public long getSkippedValidations() { return skippedValidations; }
        public long getExecutionTimeMs() { return executionTimeMs; }
        public LocalDateTime getExecutedAt() { return executedAt; }
        
        public double getSuccessRate() {
            return executedRules > 0 ? (double) passedValidations / executedRules : 0.0;
        }
        
        public boolean isCompleted() {
            return executedRules == totalRules;
        }
    }
}