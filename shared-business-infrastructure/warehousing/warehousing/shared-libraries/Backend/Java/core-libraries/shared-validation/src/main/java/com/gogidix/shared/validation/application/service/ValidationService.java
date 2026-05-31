package com.gogidix.shared.validation.application.service;

import com.gogidix.shared.validation.application.port.in.ValidationUseCase;
import com.gogidix.shared.validation.domain.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Application service implementing validation operations.
 * Orchestrates validation execution, manages validation rules, and handles validation contexts.
 */
@Service
public class ValidationService implements ValidationUseCase {
    
    private final Map<String, ValidationRule> validationRules = new ConcurrentHashMap<>();
    private final Map<String, ValidationRuleGroup> validationGroups = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    
    @Override
    public ValidationResult validate(ValidationRequest request) {
        // Get rules for this request
        List<ValidationRule> rules = request.getRuleIds().stream()
                .map(validationRules::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        // Create validate value request
        ValidateValueRequest valueRequest = new ValidateValueRequest(
                request.getDataToValidate(),
                request.getDataType(),
                rules,
                request.getContext(),
                request.isFailFast()
        );
        
        return validateValue(valueRequest);
    }
    
    @Override
    public ValidationResult validateValue(ValidateValueRequest request) {
        LocalDateTime startTime = LocalDateTime.now();
        
        try {
            if (request.getRules() == null || request.getRules().isEmpty()) {
                return ValidationResult.skipped(null, request.getValue(), "No validation rules provided");
            }
            
            List<ValidationResult> results = new ArrayList<>();
            
            for (ValidationRule rule : request.getRules()) {
                if (!rule.isEnabled()) {
                    continue;
                }
                
                ValidationResult result = rule.executeValidation(request.getValue(), request.getContext());
                results.add(result);
                
                // Fail fast if requested and validation failed
                if (request.isFailFast() && result.isFailure()) {
                    return result;
                }
            }
            
            // Return the most severe failure, or first success if all passed
            Optional<ValidationResult> mostSevereFailure = results.stream()
                    .filter(ValidationResult::isFailure)
                    .max(Comparator.comparing(result -> result.getSeverity().getLevel()));
            
            if (mostSevereFailure.isPresent()) {
                return mostSevereFailure.get();
            }
            
            return results.isEmpty() ? 
                    ValidationResult.skipped(null, request.getValue(), "No applicable rules") :
                    results.get(0); // Return first success
            
        } catch (Exception e) {
            return ValidationResult.error(null, request.getValue(), 
                "Validation service error: " + e.getMessage(), e);
        }
    }
    
    @Override
    public BatchValidationResult validateValues(ValidateValuesRequest request) {
        LocalDateTime startTime = LocalDateTime.now();
        
        try {
            Map<String, List<ValidationResult>> fieldResults = new HashMap<>();
            
            if (request.isParallel()) {
                // Parallel validation
                List<CompletableFuture<Void>> futures = request.getValues().entrySet().stream()
                        .map(entry -> CompletableFuture.runAsync(() -> {
                            String fieldName = entry.getKey();
                            Object value = entry.getValue();
                            List<ValidationRule> rules = request.getRuleMap().get(fieldName);
                            
                            if (rules != null && !rules.isEmpty()) {
                                ValidateValueRequest valueRequest = new ValidateValueRequest(
                                        value, fieldName, rules, request.getContext(), request.isFailFast()
                                );
                                
                                List<ValidationResult> results = validateFieldWithRules(valueRequest);
                                synchronized (fieldResults) {
                                    fieldResults.put(fieldName, results);
                                }
                            }
                        }, executorService))
                        .collect(Collectors.toList());
                
                // Wait for all validations to complete
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                
            } else {
                // Sequential validation
                for (Map.Entry<String, Object> entry : request.getValues().entrySet()) {
                    String fieldName = entry.getKey();
                    Object value = entry.getValue();
                    List<ValidationRule> rules = request.getRuleMap().get(fieldName);
                    
                    if (rules != null && !rules.isEmpty()) {
                        ValidateValueRequest valueRequest = new ValidateValueRequest(
                                value, fieldName, rules, request.getContext(), false
                        );
                        
                        List<ValidationResult> results = validateFieldWithRules(valueRequest);
                        fieldResults.put(fieldName, results);
                        
                        // Fail fast for sequential validation
                        if (request.isFailFast() && results.stream().anyMatch(ValidationResult::isFailure)) {
                            break;
                        }
                    }
                }
            }
            
            // Calculate summary statistics
            int totalValidations = fieldResults.values().stream()
                    .mapToInt(List::size)
                    .sum();
            
            int passedValidations = fieldResults.values().stream()
                    .mapToInt(results -> (int) results.stream().filter(ValidationResult::isSuccess).count())
                    .sum();
            
            int failedValidations = totalValidations - passedValidations;
            
            boolean allValid = failedValidations == 0;
            
            LocalDateTime endTime = LocalDateTime.now();
            long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
            
            return new BatchValidationResult(fieldResults, allValid, totalValidations, 
                    passedValidations, failedValidations, executionTimeMs);
            
        } catch (Exception e) {
            LocalDateTime endTime = LocalDateTime.now();
            long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
            
            Map<String, List<ValidationResult>> errorResults = new HashMap<>();
            ValidationResult errorResult = ValidationResult.error(null, null, 
                "Batch validation error: " + e.getMessage(), e);
            errorResults.put("system", List.of(errorResult));
            
            return new BatchValidationResult(errorResults, false, 1, 0, 1, executionTimeMs);
        }
    }
    
    @Override
    public EntityValidationResult validateEntity(ValidateEntityRequest request) {
        LocalDateTime startTime = LocalDateTime.now();
        
        try {
            List<ValidationResult> results = new ArrayList<>();
            List<ValidationResult> crossFieldResults = new ArrayList<>();
            
            // Get entity fields using reflection (simplified implementation)
            Map<String, Object> entityFields = extractEntityFields(request.getEntity());
            
            // Validate individual fields
            for (Map.Entry<String, Object> field : entityFields.entrySet()) {
                String fieldName = field.getKey();
                Object fieldValue = field.getValue();
                
                List<ValidationRule> fieldRules = getValidationRules(fieldName, request.getEntityType());
                
                if (!fieldRules.isEmpty()) {
                    ValidateValueRequest valueRequest = new ValidateValueRequest(
                            fieldValue, fieldName, fieldRules, request.getContext(), false
                    );
                    
                    ValidationResult result = validateValue(valueRequest);
                    results.add(result);
                    
                    if (request.isFailFast() && result.isFailure()) {
                        break;
                    }
                }
            }
            
            // Validate cross-field rules if requested
            if (request.isIncludeCrossField() && !request.isFailFast()) {
                crossFieldResults = validateCrossFieldRules(request.getEntity(), request.getEntityType(), 
                        request.getContext());
                results.addAll(crossFieldResults);
            }
            
            boolean valid = results.stream().noneMatch(ValidationResult::isFailure);
            ValidationSeverity overallSeverity = getOverallSeverity(results);
            
            LocalDateTime endTime = LocalDateTime.now();
            long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
            
            return new EntityValidationResult(request.getEntity(), results, crossFieldResults, 
                    valid, overallSeverity, executionTimeMs);
            
        } catch (Exception e) {
            LocalDateTime endTime = LocalDateTime.now();
            long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
            
            List<ValidationResult> errorResults = List.of(
                ValidationResult.error(null, request.getEntity(), 
                    "Entity validation error: " + e.getMessage(), e)
            );
            
            return new EntityValidationResult(request.getEntity(), errorResults, List.of(), 
                    false, ValidationSeverity.CRITICAL, executionTimeMs);
        }
    }
    
    @Override
    public EntityValidationResult validateEntityWithCrossFields(ValidateEntityRequest request) {
        ValidateEntityRequest crossFieldRequest = new ValidateEntityRequest(
                request.getEntity(), request.getEntityType(), request.getContext(),
                request.getValidationGroups(), true, request.isFailFast()
        );
        
        return validateEntity(crossFieldRequest);
    }
    
    @Override
    public GroupValidationResult validateWithGroup(ValidateWithGroupRequest request) {
        LocalDateTime startTime = LocalDateTime.now();
        
        try {
            ValidationRuleGroup group = request.getGroup();
            
            if (!group.isApplicableToContext(request.getContext())) {
                LocalDateTime endTime = LocalDateTime.now();
                long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
                
                return new GroupValidationResult(group, List.of(), true, executionTimeMs);
            }
            
            List<ValidationRule> applicableRules = group.getApplicableRules(request.getContext());
            List<ValidationResult> results = new ArrayList<>();
            
            for (ValidationRule rule : applicableRules) {
                ValidationResult result = rule.executeValidation(request.getData(), request.getContext());
                results.add(result);
                
                if (group.isStopOnFirstFailure() && result.isFailure()) {
                    break;
                }
            }
            
            boolean groupValid = results.stream().noneMatch(ValidationResult::isFailure);
            
            LocalDateTime endTime = LocalDateTime.now();
            long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
            
            return new GroupValidationResult(group, results, groupValid, executionTimeMs);
            
        } catch (Exception e) {
            LocalDateTime endTime = LocalDateTime.now();
            long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
            
            List<ValidationResult> errorResults = List.of(
                ValidationResult.error(null, request.getData(), 
                    "Group validation error: " + e.getMessage(), e)
            );
            
            return new GroupValidationResult(request.getGroup(), errorResults, false, executionTimeMs);
        }
    }
    
    @Override
    public ConditionalValidationResult validateConditionally(ValidateConditionallyRequest request) {
        LocalDateTime startTime = LocalDateTime.now();
        
        try {
            List<ConditionalRule> applicableRules = new ArrayList<>();
            List<ValidationResult> results = new ArrayList<>();
            
            for (ConditionalRule conditionalRule : request.getConditionalRules()) {
                if (evaluateCondition(conditionalRule.getCondition(), request.getData(), 
                                    conditionalRule.getConditionParameters())) {
                    applicableRules.add(conditionalRule);
                    
                    ValidationResult result = conditionalRule.getRule()
                            .executeValidation(request.getData(), request.getContext());
                    results.add(result);
                }
            }
            
            boolean valid = results.stream().noneMatch(ValidationResult::isFailure);
            
            LocalDateTime endTime = LocalDateTime.now();
            long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
            
            return new ConditionalValidationResult(results, applicableRules, valid, executionTimeMs);
            
        } catch (Exception e) {
            LocalDateTime endTime = LocalDateTime.now();
            long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();
            
            List<ValidationResult> errorResults = List.of(
                ValidationResult.error(null, request.getData(), 
                    "Conditional validation error: " + e.getMessage(), e)
            );
            
            return new ConditionalValidationResult(errorResults, List.of(), false, executionTimeMs);
        }
    }
    
    @Override
    public List<ValidationRule> getValidationRules(String fieldName, String entityType) {
        return validationRules.values().stream()
                .filter(rule -> fieldName.equals(rule.getFieldName()))
                .filter(ValidationRule::isEnabled)
                .sorted(Comparator.comparingInt(ValidationRule::getPriority))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ValidationRuleGroup> getValidationGroups(String contextName) {
        return validationGroups.values().stream()
                .filter(group -> group.getApplicableContexts() == null || 
                               group.getApplicableContexts().isEmpty() ||
                               group.getApplicableContexts().contains(contextName))
                .filter(ValidationRuleGroup::isEnabled)
                .sorted(Comparator.comparingInt(ValidationRuleGroup::getPriority))
                .collect(Collectors.toList());
    }
    
    @Override
    public ValidationRule createValidationRule(CreateValidationRuleRequest request) {
        ValidationRule rule = ValidationRule.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .validationType(request.getValidationType())
                .severity(request.getSeverity())
                .fieldName(request.getFieldName())
                .parameters(request.getParameters())
                .errorMessage(request.getErrorMessage())
                .enabled(true)
                .priority(1)
                .createdAt(LocalDateTime.now())
                .build();
        
        validationRules.put(rule.getId().toString(), rule);
        return rule;
    }
    
    @Override
    public ValidationRule updateValidationRule(UpdateValidationRuleRequest request) {
        ValidationRule existingRule = validationRules.get(request.getRuleId());
        if (existingRule == null) {
            throw new IllegalArgumentException("Validation rule not found: " + request.getRuleId());
        }
        
        ValidationRule updatedRule = existingRule
                .withName(request.getName())
                .withSeverity(request.getSeverity())
                .withParameters(request.getParameters())
                .withErrorMessage(request.getErrorMessage())
                .withEnabled(request.isEnabled())
                .withUpdatedAt(LocalDateTime.now());
        
        validationRules.put(request.getRuleId(), updatedRule);
        return updatedRule;
    }
    
    @Override
    public boolean deleteValidationRule(String ruleId) {
        return validationRules.remove(ruleId) != null;
    }
    
    @Override
    public ValidationRuleGroup createValidationGroup(CreateValidationGroupRequest request) {
        List<ValidationRule> groupRules = request.getRuleIds().stream()
                .map(validationRules::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        ValidationRuleGroup group = ValidationRuleGroup.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .groupType(request.getGroupType())
                .rules(groupRules)
                .executionMode(request.getExecutionMode())
                .stopOnFirstFailure(request.isStopOnFirstFailure())
                .enabled(true)
                .priority(1)
                .createdAt(LocalDateTime.now())
                .build();
        
        validationGroups.put(group.getId().toString(), group);
        return group;
    }
    
    @Override
    public ValidationStatistics getValidationStatistics(StatisticsRequest request) {
        // Simplified implementation - in real scenario would query from repository
        return new ValidationStatistics(0, 0, 0, 0.0, Map.of(), Map.of());
    }
    
    // Helper methods
    
    private List<ValidationResult> validateFieldWithRules(ValidateValueRequest request) {
        List<ValidationResult> results = new ArrayList<>();
        
        for (ValidationRule rule : request.getRules()) {
            if (rule.isEnabled()) {
                ValidationResult result = rule.executeValidation(request.getValue(), request.getContext());
                results.add(result);
                
                if (request.isFailFast() && result.isFailure()) {
                    break;
                }
            }
        }
        
        return results;
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, Object> extractEntityFields(Object entity) {
        // Simplified field extraction - in real implementation would use reflection
        // or Jackson ObjectMapper to extract fields
        if (entity instanceof Map) {
            return (Map<String, Object>) entity;
        }
        
        // For now, return empty map for non-Map objects
        // In real implementation, would use reflection to extract fields
        return Map.of();
    }
    
    private List<ValidationResult> validateCrossFieldRules(Object entity, String entityType, 
                                                          ValidationContext context) {
        // Simplified implementation for cross-field validation
        List<ValidationResult> results = new ArrayList<>();
        
        // In real implementation, would get cross-field rules for entity type
        // and validate relationships between fields
        
        return results;
    }
    
    private ValidationSeverity getOverallSeverity(List<ValidationResult> results) {
        return results.stream()
                .map(ValidationResult::getSeverity)
                .max(Comparator.comparingInt(ValidationSeverity::getLevel))
                .orElse(ValidationSeverity.INFO);
    }
    
    private boolean evaluateCondition(String condition, Object data, Map<String, Object> parameters) {
        // Simplified condition evaluation
        // In real implementation, would use expression language (SpEL) or similar
        return true;
    }
}