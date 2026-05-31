package com.gogidix.infrastructure.config.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * Domain model for configuration properties.
 *
 * <p>Represents a configuration property with the following characteristics:</p>
 * <ul>
 *   <li>Tenant-aware isolation</li>
 *   <li>Environment-specific values</li>
 *   <li>Version tracking with history</li>
 *   <li>Type-safe value storage</li>
 *   <li>Validation constraints</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "configuration_properties")
public class ConfigurationProperty {

    /**
     * Unique identifier for the configuration property.
     */
    @Id
    private String id;

    /**
     * Tenant identifier for multi-tenancy support.
     */
    @Indexed
    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    /**
     * Environment for this configuration (dev, staging, prod).
     */
    @Indexed
    @NotNull(message = "Environment is required")
    private Environment environment;

    /**
     * Unique key for the configuration property within tenant scope.
     */
    @Indexed
    @NotBlank(message = "Key is required")
    private String key;

    /**
     * Human-readable name for the configuration.
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * Detailed description of the configuration property.
     */
    private String description;

    /**
     * Configuration value (stored as string, typed based on valueType).
     */
    private String value;

    /**
     * Data type of the value (STRING, INTEGER, BOOLEAN, DOUBLE, JSON).
     */
    @NotNull(message = "Value type is required")
    private ValueType valueType;

    /**
     * Default value for this configuration.
     */
    private String defaultValue;

    /**
     * Whether this property is currently active/enabled.
     */
    @Builder.Default
    private boolean active = true;

    /**
     * Whether this property is sensitive and should be encrypted.
     */
    @Builder.Default
    private boolean sensitive = false;

    /**
     * Whether this property is required.
     */
    @Builder.Default
    private boolean required = false;

    /**
     * Validation rules for the property value.
     */
    private ValidationRule validationRule;

    /**
     * Tags for categorization and search.
     */
    private Set<String> tags;

    /**
     * Category for grouping related configurations.
     */
    private String category;

    /**
     * Owner/team responsible for this configuration.
     */
    private String owner;

    /**
     * User who created this configuration.
     */
    private String createdBy;

    /**
     * User who last updated this configuration.
     */
    private String lastUpdatedBy;

    /**
     * Current version number.
     */
    @Builder.Default
    private Integer version = 1;

    /**
     * Metadata associated with this configuration.
     */
    private Map<String, Object> metadata;

    /**
     * Timestamp when this configuration was created.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * Timestamp when this configuration was last modified.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Timestamp until which this configuration is valid.
     */
    private LocalDateTime validUntil;

    /**
     * Environment enumeration.
     */
    public enum Environment {
        DEV,
        STAGING,
        PROD,
        TEST
    }

    /**
     * Value type enumeration.
     */
    public enum ValueType {
        STRING,
        INTEGER,
        BOOLEAN,
        DOUBLE,
        JSON,
        LONG,
        DATE,
        ENCRYPTED_STRING
    }

    /**
     * Validation rule for property values.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationRule {
        private String regex;
        private Double minValue;
        private Double maxValue;
        private Integer minLength;
        private Integer maxLength;
        private Set<String> allowedValues;
        private String customValidationClass;
    }

    /**
     * Converts the string value to the appropriate type.
     */
    @SuppressWarnings("unchecked")
    public <T> T getTypedValue(Class<T> type) {
        if (value == null) {
            return null;
        }

        if (type == String.class) {
            return (T) value;
        } else if (type == Integer.class || type == int.class) {
            return (T) Integer.valueOf(value);
        } else if (type == Long.class || type == long.class) {
            return (T) Long.valueOf(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return (T) Boolean.valueOf(value);
        } else if (type == Double.class || type == double.class) {
            return (T) Double.valueOf(value);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    /**
     * Validates the current value against the validation rules.
     */
    public boolean validate() {
        if (validationRule == null) {
            return true;
        }

        if (value == null || value.isEmpty()) {
            return !required;
        }

        // Regex validation
        if (validationRule.regex != null && !value.matches(validationRule.regex)) {
            return false;
        }

        // Length validation
        if (validationRule.minLength != null && value.length() < validationRule.minLength) {
            return false;
        }
        if (validationRule.maxLength != null && value.length() > validationRule.maxLength) {
            return false;
        }

        // Allowed values validation
        if (validationRule.allowedValues != null && !validationRule.allowedValues.contains(value)) {
            return false;
        }

        // Numeric range validation
        try {
            if (validationRule.minValue != null || validationRule.maxValue != null) {
                double numValue = Double.parseDouble(value);
                if (validationRule.minValue != null && numValue < validationRule.minValue) {
                    return false;
                }
                if (validationRule.maxValue != null && numValue > validationRule.maxValue) {
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * Gets the effective value, falling back to default if current value is null.
     */
    public String getEffectiveValue() {
        return value != null ? value : defaultValue;
    }

    /**
     * Validates if this configuration is valid.
     * Checks required fields, value constraints, and expiration.
     */
    public boolean isValid() {
        if (tenantId == null || tenantId.isEmpty()) {
            return false;
        }
        if (key == null || key.isEmpty()) {
            return false;
        }
        // Check if not expired
        if (validUntil != null && validUntil.isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    /**
     * Increments the version number of this configuration.
     */
    public void incrementVersion() {
        this.version = this.version != null ? this.version + 1 : 1;
    }
}
