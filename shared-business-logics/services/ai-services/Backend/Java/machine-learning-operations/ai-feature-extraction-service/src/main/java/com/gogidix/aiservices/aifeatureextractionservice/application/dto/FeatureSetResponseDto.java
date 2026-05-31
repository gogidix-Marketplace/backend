package com.gogidix.aiservices.aifeatureextractionservice.application.dto;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.ExtractionMethod;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureExtractionStatus;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureValue;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO for feature set.
 */
public record FeatureSetResponseDto(
        String featureSetId,
        String dataSource,
        FeatureExtractionStatus status,
        List<ExtractionMethod> extractionMethods,
        List<FeatureValue> features,
        int featureCount,
        boolean normalized,
        String errorMessage,
        Instant createdAt,
        Instant completedAt
) {
}
