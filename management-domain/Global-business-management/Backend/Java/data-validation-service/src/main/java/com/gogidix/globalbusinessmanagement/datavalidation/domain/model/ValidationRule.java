package com.gogidix.globalbusinessmanagement.datavalidation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Domain model representing a validation rule that can be applied to data.
 * Supports multiple validation types and can be dynamically configured.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "validation_rules")
public class ValidationRule {

    @Id
    private String id;

    @Indexed
    private String name;

    @Indexed
    private String code;

    private String description;

    @Indexed
    private RuleType ruleType;

    @Indexed
    private String entityType;

    private String fieldName;

    private String jsonPath;

    private ValidationOperator operator;

    private String value;

    private List<String> allowedValues;

    private Map<String, Object> parameters;

    @Indexed
    private SeverityLevel severity;

    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    private int priority = 0;

    private String errorMessageTemplate;

    private Map<String, String> localizedErrorMessages;

    @Indexed
    @Builder.Default
    private RuleStatus status = RuleStatus.ACTIVE;

    @Builder.Default
    private int version = 1;

    @Indexed
    private String createdBy;

    private LocalDateTime createdAt;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedAt;

    private List<String> tags;

    @Builder.Default
    private boolean requiresContext = false;

    private List<String> requiredContextFields;

    /**
     * Type of validation rule
     */
    public enum RuleType {
        FIELD_VALIDATION,
        RANGE_CHECK,
        FORMAT_VALIDATION,
        REFERENCE_CHECK,
        BUSINESS_RULE,
        CROSS_FIELD_VALIDATION,
        COMPOSITE_RULE,
        CUSTOM_SCRIPT
    }

    /**
     * Validation operators
     */
    public enum ValidationOperator {
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN,
        LESS_THAN_OR_EQUAL,
        IN,
        NOT_IN,
        BETWEEN,
        REGEX,
        EMAIL,
        PHONE,
        URL,
        DATE,
        NUMERIC,
        REQUIRED,
        UNIQUE,
        EXISTS,
        LENGTH_MIN,
        LENGTH_MAX,
        LENGTH_BETWEEN
    }

    /**
     * Severity levels for validation failures
     */
    public enum SeverityLevel {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW,
        INFO
    }

    /**
     * Status of the rule
     */
    public enum RuleStatus {
        DRAFT,
        ACTIVE,
        INACTIVE,
        DEPRECATED,
        ARCHIVED
    }

    /**
     * Checks if the rule is currently active
     */
    public boolean isActive() {
        return enabled && status == RuleStatus.ACTIVE;
    }

    /**
     * Checks if the rule requires specific context fields
     */
    public boolean hasRequiredContextFields() {
        return requiresContext && requiredContextFields != null && !requiredContextFields.isEmpty();
    }

    /**
     * Creates a new version of this rule
     */
    public ValidationRule createNewVersion(String modifiedBy) {
        return ValidationRule.builder()
                .name(this.name)
                .code(this.code)
                .description(this.description)
                .ruleType(this.ruleType)
                .entityType(this.entityType)
                .fieldName(this.fieldName)
                .jsonPath(this.jsonPath)
                .operator(this.operator)
                .value(this.value)
                .allowedValues(this.allowedValues)
                .parameters(this.parameters)
                .severity(this.severity)
                .enabled(this.enabled)
                .priority(this.priority)
                .errorMessageTemplate(this.errorMessageTemplate)
                .localizedErrorMessages(this.localizedErrorMessages)
                .status(RuleStatus.DRAFT)
                .version(this.version + 1)
                .createdBy(modifiedBy)
                .createdAt(LocalDateTime.now())
                .tags(this.tags)
                .requiresContext(this.requiresContext)
                .requiredContextFields(this.requiredContextFields)
                .build();
    }
}
