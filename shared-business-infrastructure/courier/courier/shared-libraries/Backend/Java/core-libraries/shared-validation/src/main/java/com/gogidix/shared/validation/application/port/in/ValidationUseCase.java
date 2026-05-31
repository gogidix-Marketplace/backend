package com.gogidix.shared.validation.application.port.in;

import com.gogidix.shared.validation.domain.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Input port for validation operations.
 * Defines contracts for validating data, managing validation rules, and handling validation contexts.
 */
public interface ValidationUseCase {
    
    /**
     * Validates data according to a validation request.
     */
    ValidationResult validate(ValidationRequest request);
    
    /**
     * Validates a single value against specified rules.
     */
    ValidationResult validateValue(ValidateValueRequest request);
    
    /**
     * Validates multiple values against their respective rules.
     */
    BatchValidationResult validateValues(ValidateValuesRequest request);
    
    /**
     * Validates an entity against all applicable rules.
     */
    EntityValidationResult validateEntity(ValidateEntityRequest request);
    
    /**
     * Validates an entity with cross-field validation rules.
     */
    EntityValidationResult validateEntityWithCrossFields(ValidateEntityRequest request);
    
    /**
     * Validates data according to a specific validation group.
     */
    GroupValidationResult validateWithGroup(ValidateWithGroupRequest request);
    
    /**
     * Validates data conditionally based on context.
     */
    ConditionalValidationResult validateConditionally(ValidateConditionallyRequest request);
    
    /**
     * Gets validation rules for a specific field or entity.
     */
    List<ValidationRule> getValidationRules(String fieldName, String entityType);
    
    /**
     * Gets validation rule groups for a specific context.
     */
    List<ValidationRuleGroup> getValidationGroups(String contextName);
    
    /**
     * Creates a new validation rule.
     */
    ValidationRule createValidationRule(CreateValidationRuleRequest request);
    
    /**
     * Updates an existing validation rule.
     */
    ValidationRule updateValidationRule(UpdateValidationRuleRequest request);
    
    /**
     * Deletes a validation rule.
     */
    boolean deleteValidationRule(String ruleId);
    
    /**
     * Creates a new validation rule group.
     */
    ValidationRuleGroup createValidationGroup(CreateValidationGroupRequest request);
    
    /**
     * Gets validation statistics for monitoring and analysis.
     */
    ValidationStatistics getValidationStatistics(StatisticsRequest request);
    
    /**
     * Request to validate a single value.
     */
    class ValidateValueRequest {
        private final Object value;
        private final String fieldName;
        private final List<ValidationRule> rules;
        private final ValidationContext context;
        private final boolean failFast;
        
        public ValidateValueRequest(Object value, String fieldName, List<ValidationRule> rules, 
                                  ValidationContext context, boolean failFast) {
            this.value = value;
            this.fieldName = fieldName;
            this.rules = rules;
            this.context = context;
            this.failFast = failFast;
        }
        
        public Object getValue() { return value; }
        public String getFieldName() { return fieldName; }
        public List<ValidationRule> getRules() { return rules; }
        public ValidationContext getContext() { return context; }
        public boolean isFailFast() { return failFast; }
    }
    
    /**
     * Request to validate multiple values.
     */
    class ValidateValuesRequest {
        private final Map<String, Object> values;
        private final Map<String, List<ValidationRule>> ruleMap;
        private final ValidationContext context;
        private final boolean failFast;
        private final boolean parallel;
        
        public ValidateValuesRequest(Map<String, Object> values, Map<String, List<ValidationRule>> ruleMap,
                                   ValidationContext context, boolean failFast, boolean parallel) {
            this.values = values;
            this.ruleMap = ruleMap;
            this.context = context;
            this.failFast = failFast;
            this.parallel = parallel;
        }
        
        public Map<String, Object> getValues() { return values; }
        public Map<String, List<ValidationRule>> getRuleMap() { return ruleMap; }
        public ValidationContext getContext() { return context; }
        public boolean isFailFast() { return failFast; }
        public boolean isParallel() { return parallel; }
    }
    
    /**
     * Request to validate an entity.
     */
    class ValidateEntityRequest {
        private final Object entity;
        private final String entityType;
        private final ValidationContext context;
        private final Set<String> validationGroups;
        private final boolean includeCrossField;
        private final boolean failFast;
        
        public ValidateEntityRequest(Object entity, String entityType, ValidationContext context,
                                   Set<String> validationGroups, boolean includeCrossField, boolean failFast) {
            this.entity = entity;
            this.entityType = entityType;
            this.context = context;
            this.validationGroups = validationGroups;
            this.includeCrossField = includeCrossField;
            this.failFast = failFast;
        }
        
        public Object getEntity() { return entity; }
        public String getEntityType() { return entityType; }
        public ValidationContext getContext() { return context; }
        public Set<String> getValidationGroups() { return validationGroups; }
        public boolean isIncludeCrossField() { return includeCrossField; }
        public boolean isFailFast() { return failFast; }
    }
    
    /**
     * Request to validate with a specific group.
     */
    class ValidateWithGroupRequest {
        private final Object data;
        private final ValidationRuleGroup group;
        private final ValidationContext context;
        private final Map<String, Object> additionalData;
        
        public ValidateWithGroupRequest(Object data, ValidationRuleGroup group, 
                                      ValidationContext context, Map<String, Object> additionalData) {
            this.data = data;
            this.group = group;
            this.context = context;
            this.additionalData = additionalData;
        }
        
        public Object getData() { return data; }
        public ValidationRuleGroup getGroup() { return group; }
        public ValidationContext getContext() { return context; }
        public Map<String, Object> getAdditionalData() { return additionalData; }
    }
    
    /**
     * Request to validate conditionally.
     */
    class ValidateConditionallyRequest {
        private final Object data;
        private final List<ConditionalRule> conditionalRules;
        private final ValidationContext context;
        
        public ValidateConditionallyRequest(Object data, List<ConditionalRule> conditionalRules, 
                                          ValidationContext context) {
            this.data = data;
            this.conditionalRules = conditionalRules;
            this.context = context;
        }
        
        public Object getData() { return data; }
        public List<ConditionalRule> getConditionalRules() { return conditionalRules; }
        public ValidationContext getContext() { return context; }
    }
    
    /**
     * Request to create a validation rule.
     */
    class CreateValidationRuleRequest {
        private final String name;
        private final ValidationType validationType;
        private final ValidationSeverity severity;
        private final String fieldName;
        private final Map<String, Object> parameters;
        private final String errorMessage;
        
        public CreateValidationRuleRequest(String name, ValidationType validationType, 
                                         ValidationSeverity severity, String fieldName,
                                         Map<String, Object> parameters, String errorMessage) {
            this.name = name;
            this.validationType = validationType;
            this.severity = severity;
            this.fieldName = fieldName;
            this.parameters = parameters;
            this.errorMessage = errorMessage;
        }
        
        public String getName() { return name; }
        public ValidationType getValidationType() { return validationType; }
        public ValidationSeverity getSeverity() { return severity; }
        public String getFieldName() { return fieldName; }
        public Map<String, Object> getParameters() { return parameters; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    /**
     * Request to update a validation rule.
     */
    class UpdateValidationRuleRequest {
        private final String ruleId;
        private final String name;
        private final ValidationSeverity severity;
        private final Map<String, Object> parameters;
        private final String errorMessage;
        private final boolean enabled;
        
        public UpdateValidationRuleRequest(String ruleId, String name, ValidationSeverity severity,
                                         Map<String, Object> parameters, String errorMessage, boolean enabled) {
            this.ruleId = ruleId;
            this.name = name;
            this.severity = severity;
            this.parameters = parameters;
            this.errorMessage = errorMessage;
            this.enabled = enabled;
        }
        
        public String getRuleId() { return ruleId; }
        public String getName() { return name; }
        public ValidationSeverity getSeverity() { return severity; }
        public Map<String, Object> getParameters() { return parameters; }
        public String getErrorMessage() { return errorMessage; }
        public boolean isEnabled() { return enabled; }
    }
    
    /**
     * Request to create a validation group.
     */
    class CreateValidationGroupRequest {
        private final String name;
        private final ValidationRuleGroup.GroupType groupType;
        private final List<String> ruleIds;
        private final ValidationRuleGroup.GroupExecutionMode executionMode;
        private final boolean stopOnFirstFailure;
        
        public CreateValidationGroupRequest(String name, ValidationRuleGroup.GroupType groupType,
                                          List<String> ruleIds, ValidationRuleGroup.GroupExecutionMode executionMode,
                                          boolean stopOnFirstFailure) {
            this.name = name;
            this.groupType = groupType;
            this.ruleIds = ruleIds;
            this.executionMode = executionMode;
            this.stopOnFirstFailure = stopOnFirstFailure;
        }
        
        public String getName() { return name; }
        public ValidationRuleGroup.GroupType getGroupType() { return groupType; }
        public List<String> getRuleIds() { return ruleIds; }
        public ValidationRuleGroup.GroupExecutionMode getExecutionMode() { return executionMode; }
        public boolean isStopOnFirstFailure() { return stopOnFirstFailure; }
    }
    
    /**
     * Result of batch validation.
     */
    class BatchValidationResult {
        private final Map<String, List<ValidationResult>> fieldResults;
        private final boolean allValid;
        private final int totalValidations;
        private final int passedValidations;
        private final int failedValidations;
        private final long executionTimeMs;
        
        public BatchValidationResult(Map<String, List<ValidationResult>> fieldResults, 
                                   boolean allValid, int totalValidations, int passedValidations,
                                   int failedValidations, long executionTimeMs) {
            this.fieldResults = fieldResults;
            this.allValid = allValid;
            this.totalValidations = totalValidations;
            this.passedValidations = passedValidations;
            this.failedValidations = failedValidations;
            this.executionTimeMs = executionTimeMs;
        }
        
        public Map<String, List<ValidationResult>> getFieldResults() { return fieldResults; }
        public boolean isAllValid() { return allValid; }
        public int getTotalValidations() { return totalValidations; }
        public int getPassedValidations() { return passedValidations; }
        public int getFailedValidations() { return failedValidations; }
        public long getExecutionTimeMs() { return executionTimeMs; }
    }
    
    /**
     * Result of entity validation.
     */
    class EntityValidationResult {
        private final Object entity;
        private final List<ValidationResult> results;
        private final List<ValidationResult> crossFieldResults;
        private final boolean valid;
        private final ValidationSeverity overallSeverity;
        private final long executionTimeMs;
        
        public EntityValidationResult(Object entity, List<ValidationResult> results,
                                    List<ValidationResult> crossFieldResults, boolean valid,
                                    ValidationSeverity overallSeverity, long executionTimeMs) {
            this.entity = entity;
            this.results = results;
            this.crossFieldResults = crossFieldResults;
            this.valid = valid;
            this.overallSeverity = overallSeverity;
            this.executionTimeMs = executionTimeMs;
        }
        
        public Object getEntity() { return entity; }
        public List<ValidationResult> getResults() { return results; }
        public List<ValidationResult> getCrossFieldResults() { return crossFieldResults; }
        public boolean isValid() { return valid; }
        public ValidationSeverity getOverallSeverity() { return overallSeverity; }
        public long getExecutionTimeMs() { return executionTimeMs; }
    }
    
    /**
     * Result of group validation.
     */
    class GroupValidationResult {
        private final ValidationRuleGroup group;
        private final List<ValidationResult> results;
        private final boolean groupValid;
        private final long executionTimeMs;
        
        public GroupValidationResult(ValidationRuleGroup group, List<ValidationResult> results,
                                   boolean groupValid, long executionTimeMs) {
            this.group = group;
            this.results = results;
            this.groupValid = groupValid;
            this.executionTimeMs = executionTimeMs;
        }
        
        public ValidationRuleGroup getGroup() { return group; }
        public List<ValidationResult> getResults() { return results; }
        public boolean isGroupValid() { return groupValid; }
        public long getExecutionTimeMs() { return executionTimeMs; }
    }
    
    /**
     * Result of conditional validation.
     */
    class ConditionalValidationResult {
        private final List<ValidationResult> results;
        private final List<ConditionalRule> applicableRules;
        private final boolean valid;
        private final long executionTimeMs;
        
        public ConditionalValidationResult(List<ValidationResult> results, List<ConditionalRule> applicableRules,
                                         boolean valid, long executionTimeMs) {
            this.results = results;
            this.applicableRules = applicableRules;
            this.valid = valid;
            this.executionTimeMs = executionTimeMs;
        }
        
        public List<ValidationResult> getResults() { return results; }
        public List<ConditionalRule> getApplicableRules() { return applicableRules; }
        public boolean isValid() { return valid; }
        public long getExecutionTimeMs() { return executionTimeMs; }
    }
    
    // Additional supporting classes
    
    class ConditionalRule {
        private final ValidationRule rule;
        private final String condition;
        private final Map<String, Object> conditionParameters;
        
        public ConditionalRule(ValidationRule rule, String condition, Map<String, Object> conditionParameters) {
            this.rule = rule;
            this.condition = condition;
            this.conditionParameters = conditionParameters;
        }
        
        public ValidationRule getRule() { return rule; }
        public String getCondition() { return condition; }
        public Map<String, Object> getConditionParameters() { return conditionParameters; }
    }
    
    class ValidationStatistics {
        private final long totalValidations;
        private final long passedValidations;
        private final long failedValidations;
        private final double averageExecutionTimeMs;
        private final Map<ValidationType, Long> validationsByType;
        private final Map<ValidationSeverity, Long> validationsBySeverity;
        
        public ValidationStatistics(long totalValidations, long passedValidations, long failedValidations,
                                   double averageExecutionTimeMs, Map<ValidationType, Long> validationsByType,
                                   Map<ValidationSeverity, Long> validationsBySeverity) {
            this.totalValidations = totalValidations;
            this.passedValidations = passedValidations;
            this.failedValidations = failedValidations;
            this.averageExecutionTimeMs = averageExecutionTimeMs;
            this.validationsByType = validationsByType;
            this.validationsBySeverity = validationsBySeverity;
        }
        
        public long getTotalValidations() { return totalValidations; }
        public long getPassedValidations() { return passedValidations; }
        public long getFailedValidations() { return failedValidations; }
        public double getAverageExecutionTimeMs() { return averageExecutionTimeMs; }
        public Map<ValidationType, Long> getValidationsByType() { return validationsByType; }
        public Map<ValidationSeverity, Long> getValidationsBySeverity() { return validationsBySeverity; }
    }
    
    class StatisticsRequest {
        private final String entityType;
        private final String fieldName;
        private final java.time.LocalDateTime fromDate;
        private final java.time.LocalDateTime toDate;
        
        public StatisticsRequest(String entityType, String fieldName, 
                               java.time.LocalDateTime fromDate, java.time.LocalDateTime toDate) {
            this.entityType = entityType;
            this.fieldName = fieldName;
            this.fromDate = fromDate;
            this.toDate = toDate;
        }
        
        public String getEntityType() { return entityType; }
        public String getFieldName() { return fieldName; }
        public java.time.LocalDateTime getFromDate() { return fromDate; }
        public java.time.LocalDateTime getToDate() { return toDate; }
    }
}