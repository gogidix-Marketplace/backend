package com.gogidix.aiservices.aifeaturestoreservice.application.dto;

import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

/**
 * Request DTO for storing features.
 */
public record StoreFeaturesRequestDto(
        @NotBlank(message = "featureName is required")
        @Size(max = 100, message = "featureName must not exceed 100 characters")
        String featureName,

        @NotNull(message = "featureType is required")
        FeatureType featureType,

        @NotEmpty(message = "At least one entity value is required")
        List<EntityFeatureValue> values,

        String description,
        Map<String, Object> metadata
) {
    public record EntityFeatureValue(
            @NotBlank(message = "entityId is required")
            String entityId,
            Object value
    ) {}
}
