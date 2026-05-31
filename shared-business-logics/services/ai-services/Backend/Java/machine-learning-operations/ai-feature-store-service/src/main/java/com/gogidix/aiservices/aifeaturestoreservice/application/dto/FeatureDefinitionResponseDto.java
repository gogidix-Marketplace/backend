package com.gogidix.aiservices.aifeaturestoreservice.application.dto;

import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureMetadata;
import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureType;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO for feature definition.
 */
public record FeatureDefinitionResponseDto(
        String featureName,
        FeatureType featureType,
        String description,
        String version,
        List<FeatureMetadata> metadata,
        Instant createdAt,
        Instant updatedAt
) {
}
