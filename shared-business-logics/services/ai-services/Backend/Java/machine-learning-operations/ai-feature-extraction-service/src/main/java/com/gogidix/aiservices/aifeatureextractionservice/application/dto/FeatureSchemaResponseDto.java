package com.gogidix.aiservices.aifeatureextractionservice.application.dto;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureValue;

import java.util.List;

/**
 * Response DTO for feature schema.
 */
public record FeatureSchemaResponseDto(
        String featureSetId,
        List<FeatureValue> features,
        String schemaType
) {
}
