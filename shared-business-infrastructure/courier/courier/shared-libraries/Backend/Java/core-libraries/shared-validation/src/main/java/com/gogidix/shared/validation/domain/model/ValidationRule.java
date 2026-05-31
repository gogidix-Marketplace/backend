package com.gogidix.shared.validation.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Domain entity representing a validation rule.
 * Defines the validation logic and metadata for various data types.
 */
@Data
@With
@Builder
public class ValidationRule {
    
    private final UUID id;
    private final String name;
    private final String description;
    private final ValidationType validationType;
    private final ValidationSeverity severity;
    private final String fieldName;
    private final String regex;
    private final Object minValue;
    private final Object maxValue;
    private final Integer minLength;
    private final Integer maxLength;
    private final Set<String> allowedValues;
    private final Map<String, Object> parameters;
    private final String errorMessage;
    private final String errorCode;
    private final boolean enabled;
    private final int priority;
    private final Set<String> tags;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String createdBy;
    private final String updatedBy;
    private final ValidationRuleGroup group;
    private final Set<String> applicableContexts;
    
    /**
     * Validates if this validation rule is properly configured.
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
               validationType != null &&
               severity != null &&
               hasRequiredParameters() &&
               errorMessage != null && !errorMessage.trim().isEmpty();
    }
    
    /**
     * Checks if this validation rule has all required parameters.
     */
    private boolean hasRequiredParameters() {
        return switch (validationType) {
            case EMAIL, URL, PHONE, CREDIT_CARD, SSN -> true; // Built-in patterns
            case REGEX -> regex != null && !regex.trim().isEmpty();
            case NUMERIC_RANGE -> minValue != null || maxValue != null;
            case LENGTH_RANGE -> minLength != null || maxLength != null;
            case ENUM_VALUES -> allowedValues != null && !allowedValues.isEmpty();
            case DATE_RANGE, DATE_FORMAT -> parameters != null && !parameters.isEmpty();
            case CUSTOM -> parameters != null && parameters.containsKey("validator");
            case REQUIRED, NOT_NULL, NOT_EMPTY, NOT_BLANK -> true;
            case BUSINESS_LOGIC -> parameters != null && parameters.containsKey("businessRule");
            case CROSS_FIELD -> parameters != null && parameters.containsKey("relatedFields");
            case CONDITIONAL -> parameters != null && parameters.containsKey("condition");
            case DEPENDENT -> parameters != null && parameters.containsKey("dependsOn");
        };
    }
    
    /**
     * Executes this validation rule against the given value.
     */
    public ValidationResult executeValidation(Object value, ValidationContext context) {
        if (!enabled) {
            return ValidationResult.skipped(this, value, "Rule is disabled");
        }
        
        if (!isApplicableToContext(context)) {
            return ValidationResult.skipped(this, value, "Rule not applicable to context");
        }
        
        try {
            boolean passed = performValidation(value, context);
            return passed ? 
                ValidationResult.passed(this, value) : 
                ValidationResult.failed(this, value, getErrorMessage(value, context));
        } catch (Exception e) {
            return ValidationResult.error(this, value, "Validation error: " + e.getMessage(), e);
        }
    }
    
    /**
     * Performs the actual validation logic.
     */
    private boolean performValidation(Object value, ValidationContext context) {
        // Handle null/empty checks first
        if (value == null) {
            return validationType != ValidationType.REQUIRED && validationType != ValidationType.NOT_NULL;
        }
        
        String stringValue = String.valueOf(value).trim();
        if (stringValue.isEmpty()) {
            return validationType != ValidationType.NOT_EMPTY && validationType != ValidationType.NOT_BLANK;
        }
        
        return switch (validationType) {
            case EMAIL -> validateEmail(stringValue);
            case URL -> validateUrl(stringValue);
            case PHONE -> validatePhone(stringValue);
            case CREDIT_CARD -> validateCreditCard(stringValue);
            case SSN -> validateSSN(stringValue);
            case REGEX -> validateRegex(stringValue);
            case NUMERIC_RANGE -> validateNumericRange(value);
            case LENGTH_RANGE -> validateLengthRange(stringValue);
            case ENUM_VALUES -> validateEnumValues(stringValue);
            case DATE_RANGE -> validateDateRange(value);
            case DATE_FORMAT -> validateDateFormat(stringValue);
            case REQUIRED, NOT_NULL, NOT_EMPTY, NOT_BLANK -> true; // Already handled above
            case CUSTOM -> validateCustom(value, context);
            case BUSINESS_LOGIC -> validateBusinessLogic(value, context);
            case CROSS_FIELD -> validateCrossField(value, context);
            case CONDITIONAL -> validateConditional(value, context);
            case DEPENDENT -> validateDependent(value, context);
        };
    }
    
    private boolean validateEmail(String value) {
        return EmailValidationPattern.isValid(value);
    }
    
    private boolean validateUrl(String value) {
        try {
            new java.net.URL(value);
            return true;
        } catch (java.net.MalformedURLException e) {
            return false;
        }
    }
    
    private boolean validatePhone(String value) {
        return PhoneValidationPattern.isValid(value);
    }
    
    private boolean validateCreditCard(String value) {
        // Luhn algorithm implementation
        return CreditCardValidationPattern.isValid(value);
    }
    
    private boolean validateSSN(String value) {
        return SSNValidationPattern.isValid(value);
    }
    
    private boolean validateRegex(String value) {
        return regex != null && value.matches(regex);
    }
    
    private boolean validateNumericRange(Object value) {
        Number num;
        if (value instanceof Number) {
            num = (Number) value;
        } else {
            try {
                num = Double.parseDouble(String.valueOf(value));
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        double numValue = num.doubleValue();
        
        if (minValue instanceof Number min && numValue < min.doubleValue()) {
            return false;
        }
        
        if (maxValue instanceof Number max && numValue > max.doubleValue()) {
            return false;
        }
        
        return true;
    }
    
    private boolean validateLengthRange(String value) {
        int length = value.length();
        
        if (minLength != null && length < minLength) {
            return false;
        }
        
        if (maxLength != null && length > maxLength) {
            return false;
        }
        
        return true;
    }
    
    private boolean validateEnumValues(String value) {
        return allowedValues != null && allowedValues.contains(value);
    }
    
    private boolean validateDateRange(Object value) {
        // Implementation for date range validation
        return true; // Simplified for now
    }
    
    private boolean validateDateFormat(String value) {
        String format = (String) parameters.get("dateFormat");
        if (format == null) return false;
        
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(format);
            formatter.parse(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    private boolean validateCustom(Object value, ValidationContext context) {
        Object validator = parameters.get("validator");
        if (validator instanceof Predicate<?> predicate) {
            return ((Predicate<Object>) predicate).test(value);
        }
        return false;
    }
    
    private boolean validateBusinessLogic(Object value, ValidationContext context) {
        // Implementation for business logic validation
        return true; // Simplified for now
    }
    
    private boolean validateCrossField(Object value, ValidationContext context) {
        // Implementation for cross-field validation
        return true; // Simplified for now
    }
    
    private boolean validateConditional(Object value, ValidationContext context) {
        // Implementation for conditional validation
        String condition = (String) parameters.get("condition");
        if (condition == null) return true;
        return true; // Simplified
    }
    
    private boolean validateDependent(Object value, ValidationContext context) {
        // Implementation for dependent field validation
        String dependsOn = (String) parameters.get("dependsOn");
        if (dependsOn == null) return true;
        return true; // Simplified
    }
    
    /**
     * Checks if this rule is applicable to the given context.
     */
    private boolean isApplicableToContext(ValidationContext context) {
        if (applicableContexts == null || applicableContexts.isEmpty()) {
            return true; // Rule applies to all contexts
        }
        return applicableContexts.contains(context.getContextName());
    }
    
    private String getErrorMessage(Object value, ValidationContext context) {
        return errorMessage.replace("${value}", String.valueOf(value))
                         .replace("${field}", fieldName != null ? fieldName : "field");
    }
    
    /**
     * Creates a required field validation rule.
     */
    public static ValidationRule required(String fieldName) {
        return ValidationRule.builder()
                .id(UUID.randomUUID())
                .name("Required " + fieldName)
                .validationType(ValidationType.REQUIRED)
                .severity(ValidationSeverity.ERROR)
                .fieldName(fieldName)
                .errorMessage(fieldName + " is required")
                .errorCode("FIELD_REQUIRED")
                .enabled(true)
                .priority(1)
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates an email validation rule.
     */
    public static ValidationRule email(String fieldName) {
        return ValidationRule.builder()
                .id(UUID.randomUUID())
                .name("Email " + fieldName)
                .validationType(ValidationType.EMAIL)
                .severity(ValidationSeverity.ERROR)
                .fieldName(fieldName)
                .errorMessage("Invalid email format for " + fieldName)
                .errorCode("INVALID_EMAIL")
                .enabled(true)
                .priority(2)
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates a length range validation rule.
     */
    public static ValidationRule lengthRange(String fieldName, Integer minLength, Integer maxLength) {
        return ValidationRule.builder()
                .id(UUID.randomUUID())
                .name("Length Range " + fieldName)
                .validationType(ValidationType.LENGTH_RANGE)
                .severity(ValidationSeverity.ERROR)
                .fieldName(fieldName)
                .minLength(minLength)
                .maxLength(maxLength)
                .errorMessage(fieldName + " must be between " + minLength + " and " + maxLength + " characters")
                .errorCode("INVALID_LENGTH")
                .enabled(true)
                .priority(3)
                .createdAt(LocalDateTime.now())
                .build();
    }
}