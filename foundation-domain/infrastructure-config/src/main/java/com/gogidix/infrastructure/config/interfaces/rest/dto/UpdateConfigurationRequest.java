package com.gogidix.infrastructure.config.interfaces.rest.dto;

import com.gogidix.infrastructure.config.domain.model.ConfigurationProperty;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * DTO for updating an existing configuration property.
 */
public record UpdateConfigurationRequest(
        String name,

        String description,

        String value,

        String defaultValue,

        ConfigurationProperty.ValueType valueType,

        Boolean isActive,

        ConfigurationProperty.ValidationRule validationRule,

        Set<String> tags,

        String category,

        String owner,

        Map<String, Object> metadata,

        LocalDateTime validUntil,

        String changeReason
) {
}
