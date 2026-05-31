package com.gogidix.infrastructure.config.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConfigurationProperty domain model.
 */
@DisplayName("ConfigurationProperty Tests")
class ConfigurationPropertyTest {

    @Test
    @DisplayName("Should create valid configuration property")
    void shouldCreateValidConfigurationProperty() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value("test-value")
                .valueType(ConfigurationProperty.ValueType.STRING)
                .active(true)
                .build();

        assertNotNull(config);
        assertEquals("tenant-1", config.getTenantId());
        assertEquals("test.key", config.getKey());
        assertEquals("Test Key", config.getName());
        assertEquals("test-value", config.getValue());
    }

    @Test
    @DisplayName("Should validate string value correctly")
    void shouldValidateStringValue() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value("test-value")
                .valueType(ConfigurationProperty.ValueType.STRING)
                .validationRule(ConfigurationProperty.ValidationRule.builder()
                        .minLength(5)
                        .maxLength(20)
                        .build())
                .build();

        assertTrue(config.validate());
    }

    @Test
    @DisplayName("Should fail validation for value shorter than minimum")
    void shouldFailValidationForShortValue() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value("abc")
                .valueType(ConfigurationProperty.ValueType.STRING)
                .validationRule(ConfigurationProperty.ValidationRule.builder()
                        .minLength(5)
                        .build())
                .build();

        assertFalse(config.validate());
    }

    @Test
    @DisplayName("Should validate numeric range")
    void shouldValidateNumericRange() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value("50")
                .valueType(ConfigurationProperty.ValueType.INTEGER)
                .validationRule(ConfigurationProperty.ValidationRule.builder()
                        .minValue(10.0)
                        .maxValue(100.0)
                        .build())
                .build();

        assertTrue(config.validate());
    }

    @Test
    @DisplayName("Should fail validation for value outside range")
    void shouldFailValidationForValueOutsideRange() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value("150")
                .valueType(ConfigurationProperty.ValueType.INTEGER)
                .validationRule(ConfigurationProperty.ValidationRule.builder()
                        .maxValue(100.0)
                        .build())
                .build();

        assertFalse(config.validate());
    }

    @Test
    @DisplayName("Should validate allowed values")
    void shouldValidateAllowedValues() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value("option1")
                .valueType(ConfigurationProperty.ValueType.STRING)
                .validationRule(ConfigurationProperty.ValidationRule.builder()
                        .allowedValues(Set.of("option1", "option2", "option3"))
                        .build())
                .build();

        assertTrue(config.validate());
    }

    @Test
    @DisplayName("Should fail validation for value not in allowed set")
    void shouldFailValidationForValueNotInAllowedSet() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value("invalid")
                .valueType(ConfigurationProperty.ValueType.STRING)
                .validationRule(ConfigurationProperty.ValidationRule.builder()
                        .allowedValues(Set.of("option1", "option2"))
                        .build())
                .build();

        assertFalse(config.validate());
    }

    @Test
    @DisplayName("Should return effective value with fallback to default")
    void shouldReturnEffectiveValueWithDefault() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value(null)
                .defaultValue("default-value")
                .valueType(ConfigurationProperty.ValueType.STRING)
                .build();

        assertEquals("default-value", config.getEffectiveValue());
    }

    @Test
    @DisplayName("Should get typed value for string")
    void shouldGetTypedStringValue() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value("test-value")
                .valueType(ConfigurationProperty.ValueType.STRING)
                .build();

        assertEquals("test-value", config.getTypedValue(String.class));
    }

    @Test
    @DisplayName("Should get typed value for integer")
    void shouldGetTypedIntegerValue() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value("42")
                .valueType(ConfigurationProperty.ValueType.INTEGER)
                .build();

        assertEquals(42, config.getTypedValue(Integer.class));
    }

    @Test
    @DisplayName("Should get typed value for boolean")
    void shouldGetTypedBooleanValue() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value("true")
                .valueType(ConfigurationProperty.ValueType.BOOLEAN)
                .build();

        assertTrue(config.getTypedValue(Boolean.class));
    }

    @Test
    @DisplayName("Should get typed value for double")
    void shouldGetTypedDoubleValue() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value("3.14")
                .valueType(ConfigurationProperty.ValueType.DOUBLE)
                .build();

        assertEquals(3.14, config.getTypedValue(Double.class));
    }

    @Test
    @DisplayName("Should validate regex pattern")
    void shouldValidateRegexPattern() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value("user@example.com")
                .valueType(ConfigurationProperty.ValueType.STRING)
                .validationRule(ConfigurationProperty.ValidationRule.builder()
                        .regex("^[A-Za-z0-9+_.-]+@(.+)$")
                        .build())
                .build();

        assertTrue(config.validate());
    }

    @Test
    @DisplayName("Should fail regex validation")
    void shouldFailRegexValidation() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value("invalid-email")
                .valueType(ConfigurationProperty.ValueType.STRING)
                .validationRule(ConfigurationProperty.ValidationRule.builder()
                        .regex("^[A-Za-z0-9+_.-]+@(.+)$")
                        .build())
                .build();

        assertFalse(config.validate());
    }

    @Test
    @DisplayName("Should handle null value for required field")
    void shouldHandleNullValueForRequiredField() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value(null)
                .valueType(ConfigurationProperty.ValueType.STRING)
                .required(true)
                .validationRule(new ConfigurationProperty.ValidationRule(null, null, null, null, null, null, null))
                .build();

        assertFalse(config.validate());
    }

    @Test
    @DisplayName("Should pass validation for non-required null value")
    void shouldPassValidationForNonRequiredNullValue() {
        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId("tenant-1")
                .environment(ConfigurationProperty.Environment.DEV)
                .key("test.key")
                .name("Test Key")
                .value(null)
                .valueType(ConfigurationProperty.ValueType.STRING)
                .required(false)
                .build();

        assertTrue(config.validate());
    }
}
