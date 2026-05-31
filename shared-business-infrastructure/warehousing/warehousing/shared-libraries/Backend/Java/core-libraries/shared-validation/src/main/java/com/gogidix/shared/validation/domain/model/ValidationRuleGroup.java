package com.gogidix.shared.validation.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Domain entity representing a group of related validation rules.
 * Allows for organizing and managing validation rules as cohesive units.
 */
@Data
@With
@Builder
public class ValidationRuleGroup {
    
    private final UUID id;
    private final String name;
    private final String description;
    private final GroupType groupType;
    private final List<ValidationRule> rules;
    private final GroupExecutionMode executionMode;
    private final boolean stopOnFirstFailure;
    private final int priority;
    private final Set<String> tags;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String createdBy;
    private final String updatedBy;
    private final boolean enabled;
    private final Set<String> applicableContexts;
    
    /**
     * Type of validation rule group.
     */
    public enum GroupType {
        FIELD_GROUP("Field Group", "Rules for a specific field"),
        ENTITY_GROUP("Entity Group", "Rules for an entire entity"),
        BUSINESS_RULE_GROUP("Business Rule Group", "Business logic validation rules"),
        SECURITY_GROUP("Security Group", "Security-related validation rules"),
        FORMAT_GROUP("Format Group", "Format and pattern validation rules"),
        CROSS_FIELD_GROUP("Cross-Field Group", "Rules that validate across multiple fields"),
        CONDITIONAL_GROUP("Conditional Group", "Rules that apply conditionally"),
        WORKFLOW_GROUP("Workflow Group", "Rules for workflow states");
        
        private final String displayName;
        private final String description;
        
        GroupType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Mode for executing rules within a group.
     */
    public enum GroupExecutionMode {
        SEQUENTIAL("Sequential", "Execute rules one by one"),
        PARALLEL("Parallel", "Execute rules concurrently"),
        PRIORITY_BASED("Priority Based", "Execute rules by priority order"),
        CONDITIONAL("Conditional", "Execute rules based on conditions");
        
        private final String displayName;
        private final String description;
        
        GroupExecutionMode(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Validates if this rule group is properly configured.
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
               groupType != null &&
               rules != null && !rules.isEmpty() &&
               rules.stream().allMatch(ValidationRule::isValid);
    }
    
    /**
     * Gets the number of rules in this group.
     */
    public int getRuleCount() {
        return rules != null ? rules.size() : 0;
    }
    
    /**
     * Gets the number of enabled rules in this group.
     */
    public int getEnabledRuleCount() {
        return rules != null ? (int) rules.stream().filter(ValidationRule::isEnabled).count() : 0;
    }
    
    /**
     * Gets the highest priority among all rules in this group.
     */
    public int getHighestPriority() {
        return rules != null ? 
               rules.stream().mapToInt(ValidationRule::getPriority).max().orElse(0) : 0;
    }
    
    /**
     * Gets the most severe severity level among all rules in this group.
     */
    public ValidationSeverity getMostSevereSeverity() {
        if (rules == null || rules.isEmpty()) {
            return ValidationSeverity.INFO;
        }
        
        ValidationSeverity mostSevere = ValidationSeverity.INFO;
        for (ValidationRule rule : rules) {
            if (rule.getSeverity().isMoreSevereThan(mostSevere)) {
                mostSevere = rule.getSeverity();
            }
        }
        return mostSevere;
    }
    
    /**
     * Checks if this group is applicable to a given context.
     */
    public boolean isApplicableToContext(ValidationContext context) {
        if (!enabled) {
            return false;
        }
        
        if (applicableContexts == null || applicableContexts.isEmpty()) {
            return true;
        }
        
        return context != null && 
               context.getContextName() != null && 
               applicableContexts.contains(context.getContextName());
    }
    
    /**
     * Gets rules that are applicable to a given context.
     */
    public List<ValidationRule> getApplicableRules(ValidationContext context) {
        if (rules == null) {
            return List.of();
        }
        
        return rules.stream()
                   .filter(ValidationRule::isEnabled)
                   .filter(rule -> rule.getApplicableContexts() == null || 
                                 rule.getApplicableContexts().isEmpty() ||
                                 (context != null && context.getContextName() != null &&
                                  rule.getApplicableContexts().contains(context.getContextName())))
                   .toList();
    }
    
    /**
     * Creates a field validation group.
     */
    public static ValidationRuleGroup fieldGroup(String fieldName, List<ValidationRule> rules) {
        return ValidationRuleGroup.builder()
                .id(UUID.randomUUID())
                .name(fieldName + " Validation")
                .description("Validation rules for " + fieldName + " field")
                .groupType(GroupType.FIELD_GROUP)
                .rules(rules)
                .executionMode(GroupExecutionMode.SEQUENTIAL)
                .stopOnFirstFailure(false)
                .priority(1)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates an entity validation group.
     */
    public static ValidationRuleGroup entityGroup(String entityName, List<ValidationRule> rules) {
        return ValidationRuleGroup.builder()
                .id(UUID.randomUUID())
                .name(entityName + " Entity Validation")
                .description("Validation rules for " + entityName + " entity")
                .groupType(GroupType.ENTITY_GROUP)
                .rules(rules)
                .executionMode(GroupExecutionMode.PRIORITY_BASED)
                .stopOnFirstFailure(false)
                .priority(2)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates a business rule validation group.
     */
    public static ValidationRuleGroup businessRuleGroup(String businessRuleName, List<ValidationRule> rules) {
        return ValidationRuleGroup.builder()
                .id(UUID.randomUUID())
                .name(businessRuleName + " Business Rules")
                .description("Business validation rules for " + businessRuleName)
                .groupType(GroupType.BUSINESS_RULE_GROUP)
                .rules(rules)
                .executionMode(GroupExecutionMode.SEQUENTIAL)
                .stopOnFirstFailure(true)
                .priority(3)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }
}