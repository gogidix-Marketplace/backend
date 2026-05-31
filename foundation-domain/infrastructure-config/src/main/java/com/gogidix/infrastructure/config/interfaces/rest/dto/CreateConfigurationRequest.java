package com.gogidix.infrastructure.config.interfaces.rest.dto;

import com.gogidix.infrastructure.config.domain.model.ConfigurationProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * DTO for creating a new configuration property.
 */
public record CreateConfigurationRequest(
        @NotBlank(message = "Tenant ID is required")
        String tenantId,

        @NotNull(message = "Environment is required")
        ConfigurationProperty.Environment environment,

        @NotBlank(message = "Key is required")
        String key,

        @NotBlank(message = "Name is required")
        String name,

        String description,

        String value,

        String defaultValue,

        @NotNull(message = "Value type is required")
        ConfigurationProperty.ValueType valueType,

        Boolean isActive,

        Boolean isSensitive,

        Boolean isRequired,

        ConfigurationProperty.ValidationRule validationRule,

        Set<String> tags,

        String category,

        String owner,

        Map<String, Object> metadata,

        LocalDateTime validUntil,

        String changeReason
) {
}
