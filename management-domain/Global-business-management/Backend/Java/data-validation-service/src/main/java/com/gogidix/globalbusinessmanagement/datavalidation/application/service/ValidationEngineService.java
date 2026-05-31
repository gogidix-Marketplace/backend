package com.gogidix.globalbusinessmanagement.datavalidation.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.ValidationRequestDTO;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.ValidationResponseDTO;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.ValidationResultDTO;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationResult;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.repository.ValidationResultRepository;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.repository.ValidationRuleRepository;
import com.gogidix.globalbusinessmanagement.datavalidation.infrastructure.mapper.ValidationResultMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * Service for executing validation rules against data
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationEngineService {

    private final ValidationRuleRepository ruleRepository;
    private final ValidationResultRepository resultRepository;
    private final ValidationResultMapper resultMapper;
    private final ObjectMapper objectMapper;

    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(new String[]{"http", "https"});

    /**
     * Validate an entity against specified rules
     */
    @Transactional
    public ValidationResponseDTO validate(ValidationRequestDTO request) {
        log.info("Validating entity {} with ID {} against {} rules",
                request.getEntityType(), request.getEntityId(), request.getRuleCodes().size());

        long startTime = System.currentTimeMillis();
        String validationId = UUID.randomUUID().toString();

        // Get rules to apply
        List<ValidationRule> rules = getRulesToApply(request.getRuleCodes());

        List<ValidationResult> results = new ArrayList<>();
        int passedCount = 0;
        int failedCount = 0;

        // Parse entity data
        Map<String, Object> entityData;
        try {
            if (StringUtils.isNotBlank(request.getEntityData())) {
                entityData = objectMapper.readValue(request.getEntityData(), Map.class);
            } else {
                entityData = new HashMap<>();
            }
        } catch (Exception e) {
            log.error("Failed to parse entity data", e);
            return ValidationResponseDTO.builder()
                    .validationId(validationId)
                    .success(false)
                    .passed(false)
                    .totalRules(request.getRuleCodes().size())
                    .passedRules(0)
                    .failedRules(request.getRuleCodes().size())
                    .message("Invalid entity data: " + e.getMessage())
                    .executionTimeMs(System.currentTimeMillis() - startTime)
                    .build();
        }

        // Execute each rule
        for (ValidationRule rule : rules) {
            ValidationResult result = executeRule(validationId, request, rule, entityData);
            results.add(result);
            resultRepository.save(result);

            if (result.isPassed()) {
                passedCount++;
            } else {
                failedCount++;
            }
        }

        long executionTime = System.currentTimeMillis() - startTime;
        boolean allPassed = failedCount == 0;

        log.info("Validation {} completed: {} passed, {} failed in {}ms",
                validationId, passedCount, failedCount, executionTime);

        return ValidationResponseDTO.builder()
                .validationId(validationId)
                .success(true)
                .passed(allPassed)
                .totalRules(rules.size())
                .passedRules(passedCount)
                .failedRules(failedCount)
                .results(resultMapper.toDtoList(results))
                .executionTimeMs(executionTime)
                .build();
    }

    /**
     * Execute a single validation rule
     */
    private ValidationResult executeRule(String validationId, ValidationRequestDTO request,
                                         ValidationRule rule, Map<String, Object> entityData) {
        log.debug("Executing rule: {} for entity: {}", rule.getCode(), request.getEntityId());

        long startTime = System.currentTimeMillis();
        ValidationResult.ValidationResultBuilder builder = ValidationResult.builder()
                .validationId(validationId)
                .entityType(request.getEntityType())
                .entityId(request.getEntityId())
                .ruleCode(rule.getCode())
                .ruleName(rule.getName())
                .severity(rule.getSeverity())
                .status(ValidationResult.ValidationStatus.IN_PROGRESS);

        try {
            // Get the field value to validate
            String actualValue = getFieldValue(entityData, rule);
            builder.actualValue(actualValue);

            // Execute the validation
            boolean passed = executeValidation(rule, actualValue, entityData, request.getContext());
            builder.passed(passed);
            builder.status(passed ? ValidationResult.ValidationStatus.PASSED : ValidationResult.ValidationStatus.FAILED);

            if (!passed) {
                String errorMessage = generateErrorMessage(rule, actualValue);
                builder.errorMessage(errorMessage);
                builder.errorCode(rule.getCode());
            }

        } catch (Exception e) {
            log.error("Error executing rule: {}", rule.getCode(), e);
            builder.passed(false)
                    .status(ValidationResult.ValidationStatus.ERROR)
                    .errorMessage("Validation error: " + e.getMessage())
                    .errorCode("VALIDATION_ERROR");
        }

        builder.executionTimeMs(System.currentTimeMillis() - startTime);
        builder.validatedAt(LocalDateTime.now());
        builder.createdAt(LocalDateTime.now());
        builder.validatedBy(request.getValidatedBy());
        builder.tenantId(request.getTenantId());

        return builder.build();
    }

    /**
     * Get field value from entity data using JSON path or direct field name
     */
    private String getFieldValue(Map<String, Object> entityData, ValidationRule rule) {
        Object value;

        if (StringUtils.isNotBlank(rule.getJsonPath())) {
            try {
                value = JsonPath.read(entityData, rule.getJsonPath());
            } catch (PathNotFoundException e) {
                return null;
            }
        } else if (StringUtils.isNotBlank(rule.getFieldName())) {
            value = getNestedValue(entityData, rule.getFieldName());
        } else {
            return null;
        }

        return value != null ? value.toString() : null;
    }

    /**
     * Get nested value from map using dot notation
     */
    private Object getNestedValue(Map<String, Object> data, String path) {
        if (data == null || path == null) {
            return null;
        }

        String[] parts = path.split("\\.");
        Object current = data;

        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(part);
            } else {
                return null;
            }
        }

        return current;
    }

    /**
     * Execute validation based on rule operator
     */
    private boolean executeValidation(ValidationRule rule, String actualValue,
                                     Map<String, Object> entityData, Map<String, Object> context) {
        if (rule.getOperator() == null) {
            return false;
        }

        switch (rule.getOperator()) {
            case REQUIRED:
                return StringUtils.isNotBlank(actualValue);

            case EQUALS:
                return Objects.equals(actualValue, rule.getValue());

            case NOT_EQUALS:
                return !Objects.equals(actualValue, rule.getValue());

            case GREATER_THAN:
                return compareNumbers(actualValue, rule.getValue()) > 0;

            case GREATER_THAN_OR_EQUAL:
                return compareNumbers(actualValue, rule.getValue()) >= 0;

            case LESS_THAN:
                return compareNumbers(actualValue, rule.getValue()) < 0;

            case LESS_THAN_OR_EQUAL:
                return compareNumbers(actualValue, rule.getValue()) <= 0;

            case IN:
                return rule.getAllowedValues() != null && rule.getAllowedValues().contains(actualValue);

            case NOT_IN:
                return rule.getAllowedValues() == null || !rule.getAllowedValues().contains(actualValue);

            case BETWEEN:
                if (rule.getAllowedValues() != null && rule.getAllowedValues().size() >= 2) {
                    double min = Double.parseDouble(rule.getAllowedValues().get(0));
                    double max = Double.parseDouble(rule.getAllowedValues().get(1));
                    double actual = parseDouble(actualValue);
                    return actual >= min && actual <= max;
                }
                return false;

            case REGEX:
                try {
                    Pattern pattern = Pattern.compile(rule.getValue());
                    return pattern.matcher(actualValue != null ? actualValue : "").matches();
                } catch (PatternSyntaxException e) {
                    log.error("Invalid regex pattern: {}", rule.getValue(), e);
                    return false;
                }

            case EMAIL:
                return EMAIL_VALIDATOR.isValid(actualValue);

            case PHONE:
                return isValidPhoneNumber(actualValue);

            case URL:
                return URL_VALIDATOR.isValid(actualValue);

            case DATE:
                return isValidDate(actualValue);

            case NUMERIC:
                return isNumeric(actualValue);

            case EXISTS:
                return actualValue != null;

            case LENGTH_MIN:
                if (rule.getValue() != null) {
                    int minLength = Integer.parseInt(rule.getValue());
                    return actualValue != null && actualValue.length() >= minLength;
                }
                return false;

            case LENGTH_MAX:
                if (rule.getValue() != null) {
                    int maxLength = Integer.parseInt(rule.getValue());
                    return actualValue != null && actualValue.length() <= maxLength;
                }
                return false;

            case LENGTH_BETWEEN:
                if (rule.getAllowedValues() != null && rule.getAllowedValues().size() >= 2) {
                    int minLength = Integer.parseInt(rule.getAllowedValues().get(0));
                    int maxLength = Integer.parseInt(rule.getAllowedValues().get(1));
                    int length = actualValue != null ? actualValue.length() : 0;
                    return length >= minLength && length <= maxLength;
                }
                return false;

            default:
                log.warn("Unsupported validation operator: {}", rule.getOperator());
                return false;
        }
    }

    /**
     * Compare two numeric values
     */
    private int compareNumbers(String value1, String value2) {
        double num1 = parseDouble(value1);
        double num2 = parseDouble(value2);
        return Double.compare(num1, num2);
    }

    /**
     * Parse a string to double
     */
    private double parseDouble(String value) {
        if (value == null) {
            return 0;
        }
        try {
            return new BigDecimal(value).doubleValue();
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Validate phone number
     */
    private boolean isValidPhoneNumber(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        // Basic phone validation: 10-15 digits, optional + prefix, optional hyphens/spaces
        return value.replaceAll("[+\\s-]", "").matches("\\d{10,15}");
    }

    /**
     * Validate date
     */
    private boolean isValidDate(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        try {
            LocalDate.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            try {
                LocalDateTime.parse(value);
                return true;
            } catch (DateTimeParseException e2) {
                return false;
            }
        }
    }

    /**
     * Check if string is numeric
     */
    private boolean isNumeric(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Generate error message for failed validation
     */
    private String generateErrorMessage(ValidationRule rule, String actualValue) {
        if (StringUtils.isNotBlank(rule.getErrorMessageTemplate())) {
            return rule.getErrorMessageTemplate()
                    .replace("{field}", rule.getFieldName() != null ? rule.getFieldName() : "field")
                    .replace("{value}", actualValue != null ? actualValue : "null")
                    .replace("{expected}", rule.getValue() != null ? rule.getValue() : "specified");
        }

        return getDefaultErrorMessage(rule, actualValue);
    }

    /**
     * Get default error message for operator
     */
    private String getDefaultErrorMessage(ValidationRule rule, String actualValue) {
        String fieldName = rule.getFieldName() != null ? rule.getFieldName() : "Field";

        return switch (rule.getOperator()) {
            case REQUIRED -> fieldName + " is required";
            case EQUALS -> fieldName + " must equal " + rule.getValue();
            case NOT_EQUALS -> fieldName + " must not equal " + rule.getValue();
            case GREATER_THAN -> fieldName + " must be greater than " + rule.getValue();
            case GREATER_THAN_OR_EQUAL -> fieldName + " must be at least " + rule.getValue();
            case LESS_THAN -> fieldName + " must be less than " + rule.getValue();
            case LESS_THAN_OR_EQUAL -> fieldName + " must be at most " + rule.getValue();
            case IN -> fieldName + " must be one of: " + (rule.getAllowedValues() != null ? rule.getAllowedValues() : "[]");
            case NOT_IN -> fieldName + " must not be one of: " + (rule.getAllowedValues() != null ? rule.getAllowedValues() : "[]");
            case REGEX -> fieldName + " format is invalid";
            case EMAIL -> "Invalid email format";
            case PHONE -> "Invalid phone number format";
            case URL -> "Invalid URL format";
            case DATE -> "Invalid date format";
            case NUMERIC -> fieldName + " must be numeric";
            case EXISTS -> fieldName + " must exist";
            case LENGTH_MIN -> fieldName + " must be at least " + rule.getValue() + " characters";
            case LENGTH_MAX -> fieldName + " must be at most " + rule.getValue() + " characters";
            default -> "Validation failed for " + fieldName;
        };
    }

    /**
     * Get rules to apply
     */
    private List<ValidationRule> getRulesToApply(List<String> ruleCodes) {
        return ruleCodes.stream()
                .map(code -> ruleRepository.findByCode(code))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(ValidationRule::isActive)
                .sorted((r1, r2) -> Integer.compare(r2.getPriority(), r1.getPriority()))
                .collect(Collectors.toList());
    }
}
