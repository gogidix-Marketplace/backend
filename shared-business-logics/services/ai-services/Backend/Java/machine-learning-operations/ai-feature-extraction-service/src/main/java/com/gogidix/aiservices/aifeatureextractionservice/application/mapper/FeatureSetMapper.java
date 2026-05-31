package com.gogidix.aiservices.aifeatureextractionservice.application.mapper;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureSet;
import com.gogidix.aiservices.aifeatureextractionservice.application.dto.FeatureSetResponseDto;
import com.gogidix.aiservices.aifeatureextractionservice.application.dto.FeatureSchemaResponseDto;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between domain models and DTOs.
 */
@Component
public class FeatureSetMapper {

    public FeatureSetResponseDto toResponseDto(FeatureSet featureSet) {
        return new FeatureSetResponseDto(
                featureSet.getId(),
                featureSet.getDataSource(),
                featureSet.getStatus(),
                featureSet.getExtractionMethods(),
                featureSet.getFeatures(),
                featureSet.getFeatureCount() != null ? featureSet.getFeatureCount() : 0,
                Boolean.TRUE.equals(featureSet.getNormalized()),
                featureSet.getErrorMessage(),
                featureSet.getCreatedAt(),
                featureSet.getCompletedAt()
        );
    }

    public FeatureSchemaResponseDto toSchemaDto(FeatureSet featureSet) {
        return new FeatureSchemaResponseDto(
                featureSet.getId(),
                featureSet.getFeatures(),
                "numerical"
        );
    }
}
