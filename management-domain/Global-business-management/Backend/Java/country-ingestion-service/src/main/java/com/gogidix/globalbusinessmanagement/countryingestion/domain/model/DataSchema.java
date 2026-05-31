package com.gogidix.globalbusinessmanagement.countryingestion.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Domain model representing a data schema for validating incoming country data.
 * Defines field mappings, validation rules, and transformation logic.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "data_schemas")
public class DataSchema {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Schema ID is required")
    private String schemaId;

    @Indexed
    @NotBlank(message = "Schema name is required")
    private String schemaName;

    private String description;

    @Indexed
    @NotNull(message = "Schema version is required")
    private String version;

    @Indexed
    @Builder.Default
    private Boolean active = true;

    @Indexed
    private SchemaType schemaType;

    @Indexed
    private String targetEntity;

    @Valid
    @Builder.Default
    private List<SchemaField> fields = new ArrayList<>();

    @Builder.Default
    private Map<String, String> fieldMappings = new HashMap<>();

    @Builder.Default
    private Map<String, ValidationRule> validationRules = new HashMap<>();

    @Builder.Default
    private Map<String, TransformationRule> transformationRules = new HashMap<>();

    @Indexed
    @Builder.Default
    private Boolean strictValidation = false;

    @Builder.Default
    private Boolean allowUnknownFields = false;

    @Builder.Default
    private Boolean stopOnFirstError = false;

    @Indexed
    private String createdBy;

    @Indexed
    private LocalDateTime createdDate;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedDate;

    @Indexed
    private String organizationId;

    private String tenantId;

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    @DBRef(lazy = true)
    private List<IngestionBatch> usedInBatches;

    /**
     * Schema type enumeration.
     */
    public enum SchemaType {
        COUNTRY_DATA,
        REGIONAL_DATA,
        ECONOMIC_DATA,
        DEMOGRAPHIC_DATA,
        TRADE_DATA,
        CUSTOM
    }

    /**
     * Inner class representing a field definition in the schema.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SchemaField {
        private String name;
        private String displayName;
        private FieldType type;
        private boolean required;
        private boolean unique;
        private Integer minLength;
        private Integer maxLength;
        private String pattern;
        private Object minValue;
        private Object maxValue;
        private String defaultValue;
        private List<String> allowedValues;
        private String description;
        private int order;

        public enum FieldType {
            STRING,
            INTEGER,
            LONG,
            DECIMAL,
            BOOLEAN,
            DATE,
            DATETIME,
            ENUM,
            ARRAY,
            OBJECT
        }
    }

    /**
     * Inner class representing a validation rule.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationRule {
        private String ruleName;
        private RuleType ruleType;
        private String errorMessage;
        private Map<String, Object> parameters;
        private boolean enabled;

        public enum RuleType {
            REQUIRED,
            PATTERN,
            RANGE,
            LENGTH,
            EMAIL,
            URL,
            CUSTOM,
            CROSS_FIELD,
            BUSINESS_RULE
        }
    }

    /**
     * Inner class representing a transformation rule.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransformationRule {
        private String ruleName;
        private TransformationType type;
        private Map<String, Object> parameters;
        private String expression;
        private boolean enabled;

        public enum TransformationType {
            TRIM,
            TO_UPPERCASE,
            TO_LOWERCASE,
            REPLACE,
            FORMAT_DATE,
            CALCULATE,
            LOOKUP,
            MAP_VALUE,
            CONCAT,
            SPLIT,
            DEFAULT_VALUE,
            CUSTOM_SCRIPT
        }
    }

    /**
     * Gets a field definition by name.
     */
    public SchemaField getField(String fieldName) {
        return fields.stream()
                .filter(f -> f.getName().equals(fieldName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if a field is required.
     */
    public boolean isFieldRequired(String fieldName) {
        SchemaField field = getField(fieldName);
        return field != null && field.isRequired();
    }

    /**
     * Gets the mapped field name for a source field.
     */
    public String getMappedFieldName(String sourceField) {
        return fieldMappings.getOrDefault(sourceField, sourceField);
    }

    /**
     * Adds a field mapping.
     */
    public void addFieldMapping(String sourceField, String targetField) {
        fieldMappings.put(sourceField, targetField);
    }

    /**
     * Adds a validation rule.
     */
    public void addValidationRule(String fieldName, ValidationRule rule) {
        validationRules.put(fieldName, rule);
    }

    /**
     * Activates the schema.
     */
    public void activate() {
        this.active = true;
        this.lastModifiedDate = LocalDateTime.now();
    }

    /**
     * Deactivates the schema.
     */
    public void deactivate() {
        this.active = false;
        this.lastModifiedDate = LocalDateTime.now();
    }

    /**
     * Creates a new version of the schema.
     */
    public DataSchema newVersion(String newVersion) {
        return DataSchema.builder()
                .schemaId(this.schemaId + "_v" + newVersion)
                .schemaName(this.schemaName)
                .description(this.description)
                .version(newVersion)
                .schemaType(this.schemaType)
                .targetEntity(this.targetEntity)
                .fields(new ArrayList<>(this.fields))
                .fieldMappings(new HashMap<>(this.fieldMappings))
                .validationRules(new HashMap<>(this.validationRules))
                .transformationRules(new HashMap<>(this.transformationRules))
                .strictValidation(this.strictValidation)
                .allowUnknownFields(this.allowUnknownFields)
                .build();
    }

    /**
     * Validates a data record against this schema.
     */
    public List<String> validate(Map<String, Object> data) {
        List<String> errors = new ArrayList<>();

        for (SchemaField field : fields) {
            if (field.isRequired() && !data.containsKey(field.getName())) {
                errors.add(String.format("Required field '%s' is missing", field.getName()));
            }

            Object value = data.get(field.getName());
            if (value != null) {
                errors.addAll(validateFieldValue(field, value));
            }
        }

        return errors;
    }

    /**
     * Validates a single field value.
     */
    private List<String> validateFieldValue(SchemaField field, Object value) {
        List<String> errors = new ArrayList<>();
        String stringValue = value.toString();

        // Pattern validation
        if (field.getPattern() != null && !field.getPattern().isEmpty()) {
            if (!stringValue.matches(field.getPattern())) {
                errors.add(String.format("Field '%s' does not match pattern '%s'",
                        field.getName(), field.getPattern()));
            }
        }

        // Length validation
        if (field.getMinLength() != null && stringValue.length() < field.getMinLength()) {
            errors.add(String.format("Field '%s' is too short. Minimum length: %d",
                    field.getName(), field.getMinLength()));
        }
        if (field.getMaxLength() != null && stringValue.length() > field.getMaxLength()) {
            errors.add(String.format("Field '%s' is too long. Maximum length: %d",
                    field.getName(), field.getMaxLength()));
        }

        return errors;
    }
}
