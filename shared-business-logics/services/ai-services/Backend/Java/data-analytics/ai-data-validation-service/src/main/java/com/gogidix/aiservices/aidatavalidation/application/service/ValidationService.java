package com.gogidix.aiservices.aidatavalidation.application.service;

import com.gogidix.aiservices.aidatavalidation.application.dto.request.ValidateDatasetRequest;
import com.gogidix.aiservices.aidatavalidation.application.dto.response.ValidationResponse;
import com.gogidix.aiservices.aidatavalidation.domain.aggregate.ValidationExecution;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationResult;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationRule;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationType;
import com.gogidix.aiservices.aidatavalidation.domain.port.out.ValidationRepository;
import com.gogidix.aiservices.aidatavalidation.shared.exception.ValidationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final ValidationRepository validationRepository;

    public ValidationResponse validateDataset(ValidateDatasetRequest request) {
        if (request.getDataSource() == null || request.getDataSource().trim().isEmpty()) {
            throw new IllegalArgumentException("Data source cannot be null or empty");
        }
        if (request.getSchema() == null) {
            throw new IllegalArgumentException("Schema cannot be null");
        }

        String schemaJson = convertSchemaToString(request.getSchema());
        ValidationExecution execution = ValidationExecution.create(request.getDataSource(), schemaJson);

        if (request.getTimeout() != null) {
            execution.setTimeout(request.getTimeout());
        }

        if (request.getValidationRules() != null) {
            for (String ruleId : request.getValidationRules()) {
                validationRepository.findRuleById(ruleId).ifPresent(execution::addRule);
            }
        }

        execution.start();

        ValidationResult result = ValidationResult.create(execution.getExecutionId(), request.getDataSource());
        execution.complete(result);

        ValidationExecution saved = validationRepository.save(execution);
        return toResponse(saved);
    }

    public ValidationResponse getValidationResult(String validationId) {
        ValidationExecution execution = validationRepository.findById(validationId)
                .orElseThrow(() -> new ValidationNotFoundException(validationId));
        return toResponse(execution);
    }

    public List<ValidationResponse> getPendingValidations() {
        return validationRepository.findPending().stream()
                .map(this::toResponse)
                .toList();
    }

    public void addValidationRule(String name, ValidationType type, Map<String, Object> configuration) {
        String ruleId = java.util.UUID.randomUUID().toString();
        ValidationRule rule = ValidationRule.create(ruleId, name, type);
        if (configuration != null) {
            rule.setConfiguration(configuration);
        }
        validationRepository.saveRule(rule);
    }

    public List<ValidationRule> getRulesByType(ValidationType type) {
        return validationRepository.findRulesByType(type);
    }

    public void setRuleEnabled(String ruleId, boolean enabled) {
        ValidationRule rule = validationRepository.findRuleById(ruleId)
                .orElseThrow(() -> new ValidationNotFoundException(ruleId));
        rule.setEnabled(enabled);
        validationRepository.saveRule(rule);
    }

    public void deleteValidationRule(String ruleId) {
        validationRepository.findRuleById(ruleId)
                .orElseThrow(() -> new ValidationNotFoundException(ruleId));
        validationRepository.deleteRule(ruleId);
    }

    public Map<String, Object> getStatistics() {
        return validationRepository.getStatistics();
    }

    public void cancelValidation(String validationId) {
        ValidationExecution execution = validationRepository.findById(validationId)
                .orElseThrow(() -> new ValidationNotFoundException(validationId));

        if (execution.getStatus() == ValidationExecution.Status.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed validation");
        }

        execution.fail("Validation cancelled by user");
        validationRepository.save(execution);
    }

    private String convertSchemaToString(Map<String, Object> schema) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writeValueAsString(schema);
        } catch (Exception e) {
            return "{}";
        }
    }

    private ValidationResponse toResponse(ValidationExecution execution) {
        ValidationResult result = execution.getResult();
        return ValidationResponse.builder()
                .validationId(execution.getExecutionId())
                .isValid(result != null && result.isValid())
                .errors(result != null ? result.getErrors() : List.of())
                .warnings(result != null ? result.getWarnings() : List.of())
                .statistics(result != null ? result.getStatistics() : Map.of())
                .validatedAt(result != null ? result.getValidatedAt() : execution.getCreatedAt())
                .status(execution.getStatus().name())
                .build();
    }
}
